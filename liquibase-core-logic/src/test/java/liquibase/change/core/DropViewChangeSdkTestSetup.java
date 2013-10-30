package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropViewChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        DropViewChange change = (DropViewChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName("person");
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(20)"));

        CreateViewChange createViewChange = new CreateViewChange();
        createViewChange.setCatalogName(change.getCatalogName());
        createViewChange.setSchemaName(change.getSchemaName());
        createViewChange.setViewName(change.getViewName());
        createViewChange.setSelectQuery("select * from person");

        execute(createTableChange, createViewChange);

        return null;
    }
}
