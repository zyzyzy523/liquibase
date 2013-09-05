package liquibase.sdk;

import liquibase.change.Change;
import liquibase.sdk.exception.UnexpectedLiquibaseSdkException;
import liquibase.sdk.test.StandardTests;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.util.StringUtils;
import org.apache.commons.cli.*;
import org.junit.internal.TextListener;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class Main {

    private static final String ARG_PACKAGES = "packages";
    private CommandLine arguments;
    private Options commandLineOptions;
    private boolean initialized;
    private Set<String> packages = new HashSet<String>();
    private Set<Class> allClasses = new HashSet<Class>();
    private Map<Class, Set<Class>> seenExtensionClasses = new HashMap<Class, Set<Class>>();

    List<Class<?>> extensionInterfaces = Arrays.asList(Change.class, SqlGenerator.class);

    public static void main(String[] args) {
        printHeader("Liquibase Extension SDK");

        Main main = new Main();
        try {
            main.init(args);

            if (main.arguments.hasOption("help")) {
                main.printHelp();
                return;
            }
        } catch (UserError userError) {
            System.out.println(userError.getMessage());
            System.out.print("\n\n");
            main.printHelp();
        }

        if (main.seenExtensionClasses.size() == 0) {
            System.out.println("No extension classes found in "+StringUtils.join(main.packages, ","));
            return;
        }

        System.out.println("Extension classes found:");
        for (Map.Entry<Class, Set<Class>> entry : main.seenExtensionClasses.entrySet()) {
            System.out.println(StringUtils.indent(entry.getKey().getName()+" extensions:", 4));

            System.out.println(StringUtils.indent(StringUtils.join(entry.getValue(), "\n", new StringUtils.StringUtilsFormatter() {
                @Override
                public String toString(Object obj) {
                    return ((Class) obj).getName();
                }
            }), 8));
        }

        printHeader("Running Tests");

        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(new Computer(), StandardTests.class);

    }

    private static void printHeader(String header) {
        System.out.println("Liquibase Extension SDK");
        System.out.println("-------------------------------------------------");
    }

    public Main() {
        commandLineOptions = new Options();
        commandLineOptions.addOption(OptionBuilder.withArgName(ARG_PACKAGES).hasArg().withDescription("Comma separated list of packages containing extensions").isRequired(true).create("packages"));
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void init(String[] args) throws UserError {

        CommandLineParser parser = new PosixParser();
        try {
            arguments = parser.parse(commandLineOptions, args);
        } catch (ParseException e) {
            throw new UserError("Error parsing command line: " + e.getMessage());
        }


        packages.addAll(Arrays.asList(arguments.getOptionValue(ARG_PACKAGES).split("\\s*,\\s*")));

        try {
            for (String packageName : packages) {
                Enumeration<URL> dirs = this.getClass().getClassLoader().getResources(packageName.replace('.', '/'));
                while (dirs.hasMoreElements()) {
                    File dir = new File(dirs.nextElement().toURI());
                    findClasses(packageName, dir);
                }
            }
        } catch (Exception e) {
            throw new UnexpectedLiquibaseSdkException(e);
        }

        for (Class clazz : allClasses) {
            if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
                continue;
            }
            Class type = getExtensionType(clazz);
            if (type != null) {
                if (!seenExtensionClasses.containsKey(type)) {
                    seenExtensionClasses.put(type, new HashSet<Class>());
                }
                seenExtensionClasses.get(type).add(clazz);
            }
        }

        this.initialized = true;
    }

    private Class getExtensionType(Class clazz) {
        for (Class type : clazz.getInterfaces()) {
            if (extensionInterfaces.contains(type)) {
                return type;
            }
        }
        Class superclass = clazz.getSuperclass();
        if (superclass == null) {
            return null;
        }
        return getExtensionType(superclass);
    }

    private void findClasses(String packageName, File dir) throws ClassNotFoundException {
        String[] classFiles = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });
        for (String classFile : classFiles) {
            Class<?> foundClass = Class.forName(packageName + "." + classFile.replaceFirst("\\.class$", ""));
            allClasses.add(foundClass);
        }

        File[] subDirs = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File subDir : subDirs) {
            findClasses(packageName+"."+subDir.getName(), subDir);
        }

    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("liquibase-sdk", commandLineOptions);
    }

    public Set<String> getPackages() {
        return packages;
    }

    public Set<Class> getAllClasses() {
        return allClasses;
    }

    public Map<Class, Set<Class>> getSeenExtensionClasses() {
        return seenExtensionClasses;
    }

    private static class ClassComparator implements Comparator<Class<? extends Change>> {
        @Override
        public int compare(Class<? extends Change> o1, Class<? extends Change> o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public static class UserError extends Exception {

        public UserError(String message) {
            super(message);
        }
    }

}
