package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.diff.ObjectDifferences;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DropNotNullConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        DropNotNullConstraintChange change = (DropNotNullConstraintChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        String columnType = change.getColumnDataType();
        if (columnType == null) {
            columnType = "int";
        }

        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(columnType).setConstraints(new ConstraintsConfig().setNullable(false)));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropNotNullConstraintChange change = (DropNotNullConstraintChange) getChange();

        ObjectDifferences diff = diffResult.getChangedObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumnName()), getDatabase());
        assertFalse((Boolean) diff.getDifference("nullable").getReferenceValue());
        assertTrue((Boolean) diff.getDifference("nullable").getComparedValue());
    }
}
