package liquibase.precondition.core;

public class TableIsEmptyPrecondition extends RowCountPrecondition {

    public TableIsEmptyPrecondition() {
        this.expectedRows = 0;
    }

    @Override
    protected String getFailureMessage(int result) {
        return "Table " + tableName + " is not empty. Contains " + result + " rows";
    }

    @Override
    public String getName() {
        return "tableIsEmpty";
    }

}
