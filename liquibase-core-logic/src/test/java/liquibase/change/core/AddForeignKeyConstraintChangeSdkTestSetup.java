package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertNotNull;

public class AddForeignKeyConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
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

        return new Change[] {createBaseTable, createReferencedTable };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        AddForeignKeyConstraintChange change = (AddForeignKeyConstraintChange) getChange();

        Table baseTable = new Table(change.getBaseTableCatalogName(), change.getBaseTableSchemaName(), change.getBaseTableName());
        Table referencedTable = new Table(change.getReferencedTableCatalogName(), change.getReferencedTableSchemaName(), change.getReferencedTableName());

        assertNotNull(diffResult.getUnexpectedObject(new ForeignKey().setName(change.getConstraintName()).setForeignKeyTable(baseTable).setPrimaryKeyTable(referencedTable).setForeignKeyColumns(change.getBaseColumnNames()).setPrimaryKeyColumns(change.getReferencedColumnNames()), getDatabase()));
    }
}
