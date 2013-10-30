package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropNotNullConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        DropNotNullConstraintChange change = (DropNotNullConstraintChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        String columnType = change.getColumnDataType();
        if (columnType == null) {
            columnType = "int";
        }

        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(columnType).setConstraints(new ConstraintsConfig().setNullable(false)));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        execute(createTableChange);

        return null;

    }
}
