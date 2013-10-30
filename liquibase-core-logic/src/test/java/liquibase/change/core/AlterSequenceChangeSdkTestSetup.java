package liquibase.change.core;

import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class AlterSequenceChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    public String setup() throws DatabaseException {
        AlterSequenceChange change = (AlterSequenceChange) getChange();

        CreateSequenceChange createSequenceChange = new CreateSequenceChange();
        createSequenceChange.setCatalogName(change.getCatalogName());
        createSequenceChange.setSchemaName(change.getSchemaName());
        createSequenceChange.setSequenceName(change.getSequenceName());

        execute(createSequenceChange);

        return null;
    }
}
