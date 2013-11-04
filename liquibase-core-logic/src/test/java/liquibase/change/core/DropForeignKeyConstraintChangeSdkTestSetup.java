package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.statement.ForeignKeyConstraint;
import liquibase.structure.core.ForeignKey;

import static junit.framework.TestCase.assertNotNull;

public class DropForeignKeyConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
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

        return new Change[] {createBaseTable, createFKChange };

    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropForeignKeyConstraintChange change = (DropForeignKeyConstraintChange) getChange();

        assertNotNull(diffResult.getMissingObject(new ForeignKey(change.getConstraintName(), change.getBaseTableCatalogName(), change.getBaseTableSchemaName(), change.getBaseTableName()), getDatabase()));
    }
}
