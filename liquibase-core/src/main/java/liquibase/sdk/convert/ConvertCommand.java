package liquibase.sdk.convert;

import liquibase.command.AbstractCommand;
import liquibase.command.CommandResult;
import liquibase.command.CommandValidationErrors;

public class ConvertCommand extends AbstractCommand {

    private String src;
    private String out;
    private String classpath;

    @Override
    public String getName() {
        return "convert";
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    @Override
    protected CommandResult run() throws Exception {
//        List<ResourceAccessor> openers = new ArrayList<>();
//        openers.add(new FileSystemResourceAccessor());
//        openers.add(new ClassLoaderResourceAccessor());
//        if (classpath != null) {
//            openers.add(new FileSystemResourceAccessor(new File(classpath)));
//        }
//        ResourceAccessor resourceAccessor = new CompositeResourceAccessor(openers);
//
//        ChangeLogParser sourceParser = ChangeLogParserFactory.getInstance().getParser(src, resourceAccessor);
//        ChangeLogSerializer outSerializer = ChangeLogSerializerFactory.getInstance().getSerializer(out);
//
//        ChangeLog changeLog = sourceParser.parse(src, new ChangeLogParameters());
//
//        File outFile = new File(out);
//        if (!outFile.exists()) {
//            outFile.getParentFile().mkdirs();
//        }
//
//        try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
//            outSerializer.write(changeLog.getChangeSets(), outputStream);
//        }

        return new CommandResult("Converted successfully");
    }

    @Override
    public CommandValidationErrors validate() {
        return new CommandValidationErrors(this);
    }
}
