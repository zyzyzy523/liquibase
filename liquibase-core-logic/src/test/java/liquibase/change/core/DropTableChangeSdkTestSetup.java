package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropTableChangeSdkTestSetup  extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        DropTableChange change = (DropTableChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int").setConstraints(new ConstraintsConfig().setNullable(false).setPrimaryKey(true)));
        createTableChange.addColumn(new ColumnConfig().setName("not_id").setType("int"));

        execute(createTableChange);

        return null;
    }
}
