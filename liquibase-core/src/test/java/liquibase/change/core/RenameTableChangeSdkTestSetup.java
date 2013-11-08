package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Table;

import static junit.framework.TestCase.assertNotNull;

public class RenameTableChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        RenameTableChange change = (RenameTableChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getOldTableName());
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("varchar(10)"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        RenameTableChange change = (RenameTableChange) getChange();

        assertNotNull(diffResult.getMissingObject(new Table(change.getCatalogName(), change.getSchemaName(), change.getOldTableName()), getDatabase()));
        assertNotNull(diffResult.getUnexpectedObject(new Table(change.getCatalogName(), change.getSchemaName(), change.getNewTableName()), getDatabase()));
    }
}
