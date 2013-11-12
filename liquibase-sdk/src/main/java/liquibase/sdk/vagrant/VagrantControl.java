package liquibase.sdk.vagrant;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.sdk.Main;
import liquibase.sdk.supplier.database.ConnectionConfiguration;
import liquibase.sdk.supplier.database.ConnectionConfigurationFactory;
import liquibase.util.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.*;

public class VagrantControl {
    private final String boxName;
    private final List<String> commandArgs;
    private final File vagrantDir;
    private final File vagrantBoxDir;
    private String vagrantPath;
    private String vagrantBoxName;
    private String vagrantBoxUrl;
    private String hostName;

    public VagrantControl(List<String> commands, CommandLine arguments) {
//        vagrantPath = "C:\\HashiCorp\\Vagrant\\bin\\vagrant.bat";
        String path = new ProcessBuilder().environment().get("Path");
        vagrantPath = findVagrant(path);

        if (vagrantPath == null) {
            throw new RuntimeException("Cannot find vagrant in "+path);
        }
        System.out.println("Vagrant path: "+vagrantPath);

        this.boxName = commands.get(0);

        this.commandArgs = commands.subList(1, commands.size());

        try {
            vagrantDir = new File("vagrant");
            vagrantBoxDir = new File(vagrantDir, boxName).getCanonicalFile();
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException(e);
        }
    }

    private String findVagrant(String path) {
        for (String dir : path.split("[:;]")) {
            File batch = new File(dir, "vagrant.bat");
            File shell = new File(dir, "vagrant.sh");
            File empty = new File(dir, "vagrant");
            if (batch.exists()) {
                return batch.getAbsolutePath();
            }
            if (empty.exists()) {
                return batch.getAbsolutePath();
            }
            if (shell.exists()) {
                return batch.getAbsolutePath();
            }
        }
        return null;
    }

    public void init() {
        try {
            if (commandArgs.size() == 0) {
                throw new Main.UserError("Missing database configuration after vagrant box name");
            }

            System.out.println("Initializing vagrant in " + vagrantBoxDir.getAbsolutePath());

            System.out.println("Vagrant box name: " + boxName);
            System.out.println("Vagrant config: " + StringUtils.join(commandArgs, ", "));

            Collection<ConnectionConfiguration> databases = ConnectionConfigurationFactory.getInstance().findConfigurations(commandArgs);

            String[] boxInfo = null;
            String hostName = null;
            for (ConnectionConfiguration connectionConfig : databases) {
                String[] absoluteBox = getAbsoluteBox(connectionConfig.getVagrantBoxName());

                if (boxInfo == null) {
                    boxInfo = absoluteBox;
                } else {
                    if (!boxInfo[0].equals(absoluteBox[0])) {
                        throw new UnexpectedLiquibaseException("Configuration "+connectionConfig+" needs vagrant box "+absoluteBox[0]+", not "+boxInfo[0]+" like other configurations");
                    }
                }

                if (hostName == null) {
                    hostName = connectionConfig.getHostname();
                } else {
                    if (!hostName.equals(connectionConfig.getHostname())) {
                        throw new UnexpectedLiquibaseException("Configuration "+connectionConfig+" does not match previously defined hostname "+hostName);
                    }
                }
            }

            if (boxInfo == null) {
                throw new UnexpectedLiquibaseException("Null boxInfo");
            }

            this.vagrantBoxName = boxInfo[0];
            this.vagrantBoxUrl = boxInfo[1];
            this.hostName = hostName;

            System.out.println("Vagrant vm url: "+vagrantBoxUrl);
            System.out.println("Hostname: "+hostName);



            writeVagrantFile(databases);
            writePuppetFiles(databases);
        } catch (Exception e) {
            throw new UnexpectedLiquibaseException(e);
        }
    }

    public void provision() {
        runVagrant("provision");
    }

    public void destroy() {
        runVagrant("destroy");
    }

    public void halt() {
        runVagrant("halt");
    }

    public void reload() {
        runVagrant("reload");
    }

    public void resume() {
        runVagrant("resume");
    }

    public void status() {
        runVagrant("status");
    }

    public void suspend() {
        runVagrant("suspend");
    }

    public void up() {
        System.out.println("Starting vagrant in " + vagrantBoxDir.getAbsolutePath());

        System.out.println("Vagrant box name: " + boxName);
        System.out.println("-------------------------------------------------");

        runVagrant("up");
    }

    private void runVagrant(String... arguments) {
        List<String> finalArguments = new ArrayList<String>();
        finalArguments.add(vagrantPath);
        finalArguments.addAll(Arrays.asList(arguments));

        ProcessBuilder processBuilder = new ProcessBuilder(finalArguments.toArray(new String[finalArguments.size()]));
        processBuilder.directory(vagrantBoxDir);
        Map<String, String> env = processBuilder.environment();

        processBuilder.redirectErrorStream(true);

        int out = 0;
        try {
            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            out = process.exitValue();

        } catch (Exception e) {
            System.out.println("Error running vagrant");
            e.printStackTrace();
        }
        System.out.println("Out code: " + out);
    }

    private void writePuppetFiles(Collection<ConnectionConfiguration> databases) throws Exception {
        copyFile("liquibase/sdk/vagrant/puppet-bootstrap.sh", vagrantBoxDir);

        writePuppetFile(databases);

        writeManifestsInit(databases);


        File modulesDir = new File(vagrantBoxDir, "modules");
        copyFile("liquibase/sdk/vagrant/modules/my_firewall/manifests/pre.pp", new File(modulesDir, "my_firewall/manifests"));
        copyFile("liquibase/sdk/vagrant/modules/my_firewall/manifests/post.pp", new File(modulesDir, "my_firewall/manifests"));
    }

    private void writePuppetFile(Collection<ConnectionConfiguration> databases) throws Exception {
        Set<String> forges = new HashSet<String>();
        Set<String> modules = new HashSet<String>();

        for (ConnectionConfiguration config : databases) {
            forges.addAll(config.getPuppetForges(vagrantBoxName));
            modules.addAll(config.getPuppetModules());
        }

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("puppetForges", forges);
        context.put("puppetModules", modules);

        writeVelocityFile("liquibase/sdk/vagrant/Puppetfile.vm", vagrantBoxDir, context);
    }

    private void writeManifestsInit(Collection<ConnectionConfiguration> databases) throws Exception {
        File manifestsDir = new File(vagrantBoxDir, "manifests");
        manifestsDir.mkdirs();

        Set<String> requiredPackages = new HashSet<String>();
        requiredPackages.add("unzip");

        Set<String> puppetBlocks = new HashSet<String>();

        for (ConnectionConfiguration config : databases) {
            requiredPackages.addAll(config.getRequiredPackages(vagrantBoxName));
            String thisInit = config.getPuppetInit(vagrantBoxName);
            if (thisInit != null) {
                puppetBlocks.add(thisInit);
            }
        }

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("requiredPackages", requiredPackages);
        context.put("puppetBlocks", puppetBlocks);

        writeVelocityFile("liquibase/sdk/vagrant/manifests/init.pp.vm", manifestsDir, context);
    }

    private void copyFile(String sourcePath, File outputDir) throws Exception {
        outputDir.mkdirs();

        InputStream input = this.getClass().getClassLoader().getResourceAsStream(sourcePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String fileName = sourcePath.replaceFirst(".*/", "");
        BufferedWriter output = new BufferedWriter(new FileWriter(new File(outputDir, fileName)));

        String line;
        while ((line = reader.readLine()) != null) {
            output.write(line + "\n");
        }

        output.flush();
        output.close();
    }

    private void writeVagrantFile(Collection<ConnectionConfiguration> databases) throws Exception {

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("configVmBox", vagrantBoxName);
        context.put("configVmBoxUrl", vagrantBoxUrl);
        context.put("configVmNetworkIp", hostName);
        context.put("vmCustomizeMemory", "8192");

        writeVelocityFile("liquibase/sdk/vagrant/Vagrantfile.vm", vagrantBoxDir, context);
    }

    protected String[] getAbsoluteBox(String vagrantBoxName) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(new File(vagrantDir, "vagrant-boxes.default.properties")));
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException(e);
        }

        File file = new File(vagrantDir, "vagrant-boxes.properties");
        if (file.exists()) {
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                throw new UnexpectedLiquibaseException(e);
            }
        }

        String absoluteKey = vagrantBoxName;
        String value = properties.getProperty(absoluteKey);
        while (value != null && !value.startsWith("http")) {
            absoluteKey = properties.getProperty(absoluteKey);
            value = properties.getProperty(absoluteKey);
        }

        if (value == null) {
            throw new UnexpectedLiquibaseException("Could not determine box url for "+absoluteKey);
        }
        return new String[] { absoluteKey, value };

    }

    private void writeVelocityFile(String templatePath, File outputDir, Map<String, Object> contextParams) throws Exception {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();

        InputStream input = this.getClass().getClassLoader().getResourceAsStream(templatePath);
        if (input == null) {
            throw new IOException("Template file " + templatePath + " doesn't exist");
        }

        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> entry : contextParams.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }

        Template template = engine.getTemplate(templatePath, "UTF-8");
        outputDir.mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputDir, templatePath.replaceFirst(".*/", "").replaceFirst(".vm$", ""))));

        template.merge(context, writer);

        writer.flush();
        writer.close();
    }
}
