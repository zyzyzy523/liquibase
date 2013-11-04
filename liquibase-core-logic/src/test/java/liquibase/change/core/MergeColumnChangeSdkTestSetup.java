package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;

import static junit.framework.TestCase.assertNotNull;

public class MergeColumnChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        MergeColumnChange change = (MergeColumnChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumn1Name()).setType("varchar(10)"));
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumn2Name()).setType("varchar(10)"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        MergeColumnChange change = (MergeColumnChange) getChange();

        assertNotNull(diffResult.getMissingObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumn1Name()), getDatabase()));
        assertNotNull(diffResult.getMissingObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumn2Name()), getDatabase()));

        assertNotNull(diffResult.getUnexpectedObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getFinalColumnName()), getDatabase()));

    }
}
