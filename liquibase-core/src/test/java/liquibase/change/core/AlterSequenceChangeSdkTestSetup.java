package liquibase.change.core;

import liquibase.change.Change;
import liquibase.diff.DiffResult;
import liquibase.diff.ObjectDifferences;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Sequence;

import static junit.framework.Assert.assertEquals;

public class AlterSequenceChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        AlterSequenceChange change = (AlterSequenceChange) getChange();

        CreateSequenceChange createSequenceChange = new CreateSequenceChange();
        createSequenceChange.setCatalogName(change.getCatalogName());
        createSequenceChange.setSchemaName(change.getSchemaName());
        createSequenceChange.setSequenceName(change.getSequenceName());

        return new Change[] {createSequenceChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        AlterSequenceChange change = (AlterSequenceChange) getChange();

        if (change.getMinValue() != null) {
            ObjectDifferences diff = diffResult.getChangedObject(new Sequence(change.getCatalogName(), change.getSchemaName(), change.getSequenceName()), getDatabase());
            assertEquals(change.getMinValue(), diff.getDifference("minValue").getComparedValue());
        }

        if (change.getMaxValue() != null) {
            ObjectDifferences diff = diffResult.getChangedObject(new Sequence(change.getCatalogName(), change.getSchemaName(), change.getSequenceName()), getDatabase());
            assertEquals(change.getMaxValue(), diff.getDifference("maxValue").getComparedValue());
        }

        if (change.getIncrementBy() != null) {
            ObjectDifferences diff = diffResult.getChangedObject(new Sequence(change.getCatalogName(), change.getSchemaName(), change.getSequenceName()), getDatabase());
            assertEquals(change.getIncrementBy(), diff.getDifference("incrementBy").getComparedValue());
        }

        if (change.isOrdered() != null) {
            ObjectDifferences diff = diffResult.getChangedObject(new Sequence(change.getCatalogName(), change.getSchemaName(), change.getSequenceName()), getDatabase());
            assertEquals(change.isOrdered(), diff.getDifference("ordered").getComparedValue());
        }
    }
}
