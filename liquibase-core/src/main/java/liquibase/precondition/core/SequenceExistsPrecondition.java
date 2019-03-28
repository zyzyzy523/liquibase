package liquibase.precondition.core;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.exception.*;
import liquibase.precondition.AbstractPrecondition;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Sequence;

public class SequenceExistsPrecondition extends AbstractPrecondition {
    public String catalogName;
    public String schemaName;
    public String sequenceName;

    @Override
    public Warnings warn(Database database) {
        return new Warnings();
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }

    @Override
    public void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener)
            throws PreconditionFailedException, PreconditionErrorException {
        DatabaseSnapshot snapshot;
        Schema schema = new Schema(catalogName, schemaName);
        try {
            if (!SnapshotGeneratorFactory.getInstance().has(new Sequence().setName(sequenceName).setSchema(schema), database)) {
                throw new PreconditionFailedException("Sequence " + database.escapeSequenceName(catalogName, schemaName, sequenceName) + " does not exist", changeLog, this);
            }
        } catch (LiquibaseException e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "sequenceExists";
    }
}
