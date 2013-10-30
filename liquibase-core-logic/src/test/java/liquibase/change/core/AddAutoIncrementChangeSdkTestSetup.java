package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class AddAutoIncrementChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        AddAutoIncrementChange change = (AddAutoIncrementChange) getChange();

        CreateTableChange createTable = new CreateTableChange();
        createTable.setCatalogName(change.getCatalogName());
        createTable.setSchemaName(change.getSchemaName());
        createTable.setTableName(change.getTableName());
        String dataType = change.getColumnDataType();
        if (dataType == null) {
            dataType = "int";
        }
        createTable.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(dataType).setConstraints(new ConstraintsConfig().setPrimaryKey(true).setNullable(false)));

        execute(createTable);

        return null;

    }
}
