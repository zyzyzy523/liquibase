package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class RawSQLChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        RawSQLChange change = (RawSQLChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
//        createTableChange.setCatalogName(change.getCatalogName());
//        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName("person");
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(10)"));
        createTableChange.addColumn(new ColumnConfig().setName("address").setType("varchar(10)"));

        execute(createTableChange);

        return null;
    }
}
