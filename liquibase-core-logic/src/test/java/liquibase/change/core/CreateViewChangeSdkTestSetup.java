package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class CreateViewChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        CreateViewChange change = (CreateViewChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
//        createTableChange.setCatalogName(change.getCatalogName());
//        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName("person");
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(255)"));

        execute(createTableChange);

        return null;
    }
}
