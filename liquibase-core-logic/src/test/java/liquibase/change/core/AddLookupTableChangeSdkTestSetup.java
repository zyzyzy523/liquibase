package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class AddLookupTableChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        AddLookupTableChange change = (AddLookupTableChange) getChange();

        CreateTableChange createTableChange= new CreateTableChange();
        createTableChange.setCatalogName(change.getExistingTableCatalogName());
        createTableChange.setSchemaName(change.getExistingTableSchemaName());
        createTableChange.setTableName(change.getExistingTableName());

        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName(change.getExistingColumnName()).setType("int"));

        execute(createTableChange);

        return null;
    }
}
