package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class AddNotNullConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        AddNotNullConstraintChange change = (AddNotNullConstraintChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType("varchar(10)"));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("varchar(10)"));

        execute(createTableChange);

        return null;
    }
}

