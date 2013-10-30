package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class AddForeignKeyConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    public String setup() throws DatabaseException {
        AddForeignKeyConstraintChange change = (AddForeignKeyConstraintChange) getChange();

        CreateTableChange createBaseTable = new CreateTableChange();
        createBaseTable.setCatalogName(change.getBaseTableCatalogName());
        createBaseTable.setSchemaName(change.getBaseTableSchemaName());
        createBaseTable.setTableName(change.getBaseTableName());
        for (String columnName : change.getBaseColumnNames().split("\\s+,\\s+")) {
            createBaseTable.addColumn(new ColumnConfig().setName(columnName).setType("int"));
        }

        CreateTableChange createReferencedTable= new CreateTableChange();
        createReferencedTable.setCatalogName(change.getReferencedTableCatalogName());
        createReferencedTable.setSchemaName(change.getReferencedTableSchemaName());
        createReferencedTable.setTableName(change.getReferencedTableName());
        for (String columnName : change.getReferencedColumnNames().split("\\s+,\\s+")) {
            createReferencedTable.addColumn(new ColumnConfig().setName(columnName).setType("int").setConstraints(new ConstraintsConfig().setPrimaryKey(true)));
        }

        execute(createBaseTable, createReferencedTable);

        return null;
    }
}
