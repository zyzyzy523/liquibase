package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.View;

import static junit.framework.Assert.assertNotNull;

public class RenameViewChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[] prepareDatabase() throws Exception {
        return null;
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        RenameViewChange change = (RenameViewChange) getChange();
          assertNotNull(diffResult.getMissingObject(new View(change.getCatalogName(), change.getSchemaName(), change.getOldViewName()), getDatabase()));
        assertNotNull(diffResult.getUnexpectedObject(new View(change.getCatalogName(), change.getSchemaName(), change.getNewViewName()), getDatabase()));
    }
}
