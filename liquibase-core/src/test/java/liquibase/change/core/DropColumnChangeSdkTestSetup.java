package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertNotNull;

public class DropColumnChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        DropColumnChange change = (DropColumnChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName(change.getColumnName()).setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("other_col").setType("int"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropColumnChange change = (DropColumnChange) getChange();

        assertNotNull(diffResult.getMissingObject(new Column(Table.class, change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumnName()), getDatabase()));
    }
}
