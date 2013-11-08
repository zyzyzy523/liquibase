package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

import static junit.framework.Assert.assertEquals;

public class EmptyChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[] prepareDatabase() throws Exception {
        return null;
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        assertEquals(0, diffResult.getMissingObjects().size());
        assertEquals(0, diffResult.getUnexpectedObjects().size());
        assertEquals(0, diffResult.getChangedObjects().size());
    }
}
