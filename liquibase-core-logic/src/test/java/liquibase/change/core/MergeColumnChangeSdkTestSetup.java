package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class MergeColumnChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        MergeColumnChange change = (MergeColumnChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumn1Name()).setType("varchar(10)"));
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumn2Name()).setType("varchar(10)"));

        execute(createTableChange);

        return null;
    }
}
