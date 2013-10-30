package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class RenameColumnChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    public String setup() throws Exception {
        RenameColumnChange change = (RenameColumnChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));
        String dataType = change.getColumnDataType();
        if (dataType == null) {
            dataType = "int";
        }
        createTableChange.addColumn(new ColumnConfig().setName(change.getOldColumnName()).setType(dataType));

        execute(createTableChange);

        return null;
    }
}
