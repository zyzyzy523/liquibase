package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class ModifyDataTypeChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        ModifyDataTypeChange change = (ModifyDataTypeChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(change.getNewDataType()));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("varchar(10)"));

        execute(createTableChange);

        return null;

    }
}
