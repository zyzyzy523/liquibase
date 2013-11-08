package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Table;

import static junit.framework.TestCase.assertNotNull;

public class DropTableChangeSdkTestSetup  extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        DropTableChange change = (DropTableChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int").setConstraints(new ConstraintsConfig().setNullable(false).setPrimaryKey(true)));
        createTableChange.addColumn(new ColumnConfig().setName("not_id").setType("int"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropTableChange change = (DropTableChange) getChange();

        assertNotNull(diffResult.getMissingObject(new Table(change.getCatalogName(), change.getSchemaName(), change.getTableName()), getDatabase()));
    }
}
