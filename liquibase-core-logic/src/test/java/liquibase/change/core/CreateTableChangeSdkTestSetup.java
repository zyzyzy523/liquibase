package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertNotNull;

public class CreateTableChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[] prepareDatabase() throws Exception {
        return null;
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        CreateTableChange change = (CreateTableChange) getChange();
        assertNotNull(diffResult.getUnexpectedObject(new Table(change.getCatalogName(), change.getSchemaName(), change.getTableName()), getDatabase()));
    }
}
