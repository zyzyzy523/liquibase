package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class RenameColumnChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    protected Change[]  prepareDatabase() throws Exception {
        RenameColumnChange change = (RenameColumnChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));
        String dataType = change.getColumnDataType();
        if (dataType == null) {
            dataType = "int";
        }
        createTableChange.addColumn(new ColumnConfig().setName(change.getOldColumnName()).setType(dataType));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        RenameColumnChange change = (RenameColumnChange) getChange();

        Column oldColumn = diffResult.getMissingObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getOldColumnName()), getDatabase());
        Column newColumn = diffResult.getUnexpectedObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getNewColumnName()), getDatabase());

        assertNotNull(oldColumn);
        assertNotNull(newColumn);
        assertEquals(oldColumn.getType().toString(), newColumn.getType().toString());
    }
}
