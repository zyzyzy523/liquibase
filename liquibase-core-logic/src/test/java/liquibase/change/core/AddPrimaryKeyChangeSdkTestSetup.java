package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class AddPrimaryKeyChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        AddPrimaryKeyChange change = (AddPrimaryKeyChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        for (String columnName : change.getColumnNames().split(",")) {
            createTableChange.addColumn(new ColumnConfig().setName(columnName.trim()).setType("int").setConstraints(new ConstraintsConfig().setNullable(false)));
        }
        createTableChange.addColumn(new ColumnConfig().setName("not_id").setType("int"));

        execute(createTableChange);

        return null;
    }
}
