package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class UpdateDataChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        UpdateDataChange change = (UpdateDataChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());

        for (ColumnConfig column : change.getColumns()) {
            createTableChange.addColumn(new ColumnConfig().setName(column.getName()).setType("int"));
        }
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        execute(createTableChange);

        return null;
    }
}
