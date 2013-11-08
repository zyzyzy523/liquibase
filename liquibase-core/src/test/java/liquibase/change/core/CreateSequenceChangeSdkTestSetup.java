package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Sequence;
import liquibase.structure.core.Table;

import static junit.framework.Assert.assertNotNull;

public class CreateSequenceChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[] prepareDatabase() throws Exception {
        return null;
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        CreateSequenceChange change = (CreateSequenceChange) getChange();

        assertNotNull(diffResult.getUnexpectedObject(new Sequence(change.getCatalogName(), change.getSchemaName(), change.getSequenceName()), getDatabase()));
    }
}
