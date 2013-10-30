package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropIndexChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    public String setup() throws Exception {
        DropIndexChange change = (DropIndexChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());

        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(255)").setConstraints(new ConstraintsConfig().setNullable(false)));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        CreateIndexChange createIndexChange = new CreateIndexChange();
        createIndexChange.setIndexName(change.getIndexName());
        createIndexChange.setTableName(change.getTableName());
        createIndexChange.addColumn(new ColumnConfig().setName("name"));

        execute(createTableChange, createIndexChange);

        return null;

    }
}
