package liquibase.ext.vertica.change;

import liquibase.change.AddColumnConfig;

public class ColumnConfigVertica extends AddColumnConfig {
    //    private String encoding = null;
    private Integer accessrank = null;

//    public String getEncoding() {
//        return encoding;
//    }

//    public void setEncoding(String encoding) {
//        this.encoding = encoding;
//    }

    public ColumnConfigVertica() {
        super();
        this.accessrank = null;
    }

    public ColumnConfigVertica(Integer accessrank) {
        super();
        this.accessrank = accessrank;
    }

    public Integer getAccessrank() {
        return accessrank;
    }

    public void setAccessrank(Integer accessrank) {
        this.accessrank = accessrank;
    }
}
