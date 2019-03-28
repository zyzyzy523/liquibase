package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.resource.ResourceAccessor;

public class LoadDataColumnConfig extends ColumnConfig {

    private Integer index;
    private String header;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public void load(ParsedNode parsedNode) throws ParseException {
        super.load(parsedNode);
        this.index = parsedNode.getChildValue("index", Integer.class, true);
        this.header = parsedNode.getChildValue("header", String.class, true);
    }
}
