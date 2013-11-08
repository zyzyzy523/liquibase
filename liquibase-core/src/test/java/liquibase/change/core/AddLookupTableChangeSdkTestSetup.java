package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AddLookupTableChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        AddLookupTableChange change = (AddLookupTableChange) getChange();

        CreateTableChange createTableChange= new CreateTableChange();
        createTableChange.setCatalogName(change.getExistingTableCatalogName());
        createTableChange.setSchemaName(change.getExistingTableSchemaName());
        createTableChange.setTableName(change.getExistingTableName());

        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName(change.getExistingColumnName()).setType(change.getNewColumnDataType()));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        AddLookupTableChange change = (AddLookupTableChange) getChange();


        Table newTable = diffResult.getUnexpectedObject(new Table(change.getNewTableCatalogName(), change.getNewTableSchemaName(), change.getNewTableName()), getDatabase());
        assertNotNull(newTable);
        assertEquals(1, newTable.getColumns().size());
        assertNotNull(newTable.getColumn(change.getNewColumnName()));
//todo        assertEquals(change.getNewColumnDataType(), newTable.getColumn(change.getNewColumnName()).getType().toString());
        assertEquals(change.getNewColumnName().toUpperCase(), newTable.getPrimaryKey().getColumnNames().toUpperCase());

        assertNotNull(diffResult.getUnexpectedObject(new Table(change.getNewTableCatalogName(), change.getNewTableSchemaName(), change.getNewTableName()), getDatabase()));
        assertNotNull(new ForeignKey(change.getConstraintName(), change.getExistingTableCatalogName(), change.getExistingTableSchemaName(), change.getExistingTableName(), change.getExistingColumnName()));

    }
}
