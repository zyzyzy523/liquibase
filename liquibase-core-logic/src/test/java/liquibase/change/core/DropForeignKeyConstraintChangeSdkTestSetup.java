package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropForeignKeyConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        DropForeignKeyConstraintChange change = (DropForeignKeyConstraintChange) getChange();

        CreateTableChange createBaseTable = new CreateTableChange();
        createBaseTable.setCatalogName(change.getBaseTableCatalogName());
        createBaseTable.setSchemaName(change.getBaseTableSchemaName());
        createBaseTable.setTableName(change.getBaseTableName());
        createBaseTable.addColumn(new ColumnConfig().setName("id").setType("int").setConstraints(new ConstraintsConfig().setPrimaryKey(true)));
        createBaseTable.addColumn(new ColumnConfig().setName("customer_id").setType("int"));

        AddForeignKeyConstraintChange createFKChange = new AddForeignKeyConstraintChange();
        createFKChange.setBaseTableCatalogName(change.getBaseTableCatalogName());
        createFKChange.setBaseTableSchemaName(change.getBaseTableSchemaName());
        createFKChange.setBaseTableName(change.getBaseTableName());
        createFKChange.setBaseColumnNames("customer_id");

        createFKChange.setReferencedTableCatalogName(change.getBaseTableCatalogName());
        createFKChange.setReferencedTableSchemaName(change.getBaseTableSchemaName());
        createFKChange.setReferencedTableName(change.getBaseTableName());
        createFKChange.setReferencedColumnNames("id");
        createFKChange.setConstraintName(change.getConstraintName());

        execute(createBaseTable, createFKChange);

        return null;

    }
}
