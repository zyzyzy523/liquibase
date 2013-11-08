package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.diff.ObjectDifferences;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class DropDefaultValueChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        DropDefaultValueChange change = (DropDefaultValueChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("other_col").setType("int"));
        String dataType = change.getColumnDataType();
        if (dataType == null) {
            dataType = "int";
        }
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(dataType).setDefaultValue("1"));

        return new Change[] {createTableChange };

    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropDefaultValueChange change = (DropDefaultValueChange) getChange();

        ObjectDifferences diff = diffResult.getChangedObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumnName()), getDatabase());
        assertNotNull(diff);

        assertNotNull(diff.getDifference("defaultValue").getReferenceValue());
        assertEquals("NULL", diff.getDifference("defaultValue").getComparedValue().toString());

    }
}
