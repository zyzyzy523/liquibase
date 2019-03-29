package liquibase.integration.ant.type;

import liquibase.parser.Unparser;
import org.apache.tools.ant.types.resources.FileResource;

public class ChangeLogOutputFile {
    private FileResource outputFile;
    private String encoding;
    private Unparser unparser;

    public Unparser getUnparser() {
        return unparser;
    }

    public void setUnparser(Unparser changeLogSerializer) {
        this.unparser = changeLogSerializer;
    }

    public FileResource getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(FileResource outputFile) {
        this.outputFile = outputFile;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
