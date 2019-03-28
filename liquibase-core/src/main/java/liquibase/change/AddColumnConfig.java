package liquibase.change;


import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.structure.core.Column;

public class AddColumnConfig extends ColumnConfig {

    private String afterColumn;
    private String beforeColumn;
    private Integer position;

    public AddColumnConfig(Column columnSnapshot) {
        super(columnSnapshot);
    }

    public AddColumnConfig() {
    }

    public String getAfterColumn() {
        return afterColumn;
    }

    public void setAfterColumn(String afterColumn) {
        this.afterColumn = afterColumn;
    }

    public String getBeforeColumn() {
        return beforeColumn;
    }

    public void setBeforeColumn(String beforeColumn) {
        this.beforeColumn = beforeColumn;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public void load(ParsedNode parsedNode) throws ParseException {
        super.load(parsedNode);
        this.beforeColumn = parsedNode.getChildValue("beforeColumn", String.class, true);
        this.afterColumn = parsedNode.getChildValue("afterColumn", String.class, true);
        this.position = parsedNode.getChildValue("position", Integer.class, true);
    }
}
