package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class SQLFileChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[] prepareDatabase() throws Exception {
        return null;
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        //todo
    }
}
