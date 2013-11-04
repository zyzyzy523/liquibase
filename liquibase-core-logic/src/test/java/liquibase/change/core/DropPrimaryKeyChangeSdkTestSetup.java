package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.PrimaryKey;

import static junit.framework.TestCase.assertNotNull;

public class DropPrimaryKeyChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        DropPrimaryKeyChange change = (DropPrimaryKeyChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int").setConstraints(new ConstraintsConfig().setNullable(false).setPrimaryKey(true).setPrimaryKeyName(change.getConstraintName())));
        createTableChange.addColumn(new ColumnConfig().setName("not_id").setType("int"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropPrimaryKeyChange change = (DropPrimaryKeyChange) getChange();

        assertNotNull(diffResult.getMissingObject(new PrimaryKey(change.getConstraintName(), change.getCatalogName(), change.getSchemaName(), change.getTableName()), getDatabase()));
    }
}
