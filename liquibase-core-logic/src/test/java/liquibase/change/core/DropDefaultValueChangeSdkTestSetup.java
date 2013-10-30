package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropDefaultValueChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        DropDefaultValueChange change = (DropDefaultValueChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("other_col").setType("int"));
        String dataType = change.getColumnDataType();
        if (dataType == null) {
            dataType = "int";
        }
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(dataType).setDefaultValue("1"));

        execute(createTableChange);

        return null;

    }
}
