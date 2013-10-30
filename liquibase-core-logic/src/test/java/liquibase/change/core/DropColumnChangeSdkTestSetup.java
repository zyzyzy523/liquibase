package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropColumnChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        DropColumnChange change = (DropColumnChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("other_col").setType("int"));

        execute(createTableChange);

        return null;
    }
}
