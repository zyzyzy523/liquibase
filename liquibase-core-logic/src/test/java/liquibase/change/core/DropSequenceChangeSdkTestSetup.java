package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Sequence;

import static junit.framework.TestCase.assertNotNull;

public class DropSequenceChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        DropSequenceChange change = (DropSequenceChange) getChange();

        CreateSequenceChange createSequenceChange = new CreateSequenceChange();
        createSequenceChange.setCatalogName(change.getCatalogName());
        createSequenceChange.setSchemaName(change.getSchemaName());
        createSequenceChange.setSequenceName(change.getSequenceName());

        return new Change[] {createSequenceChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropSequenceChange change = (DropSequenceChange) getChange();

        assertNotNull(diffResult.getMissingObject(new Sequence(change.getCatalogName(), change.getSchemaName(), change.getSequenceName()), getDatabase()));
    }
}
