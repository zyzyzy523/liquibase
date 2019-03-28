package liquibase.precondition.core;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.precondition.AbstractPrecondition;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

public class TableExistsPrecondition extends AbstractPrecondition {
    public String catalogName;
    public String schemaName;
    public String tableName;

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
        try {
            String correctedTableName = database.correctObjectName(tableName, Table.class);
            if (!SnapshotGeneratorFactory.getInstance().has(new Table().setName(correctedTableName).setSchema(new Schema(catalogName, schemaName)), database)) {
                throw new PreconditionFailedException("Table " + database.escapeTableName(catalogName, schemaName, tableName) + " does not exist", changeLog, this);
            }
        } catch (PreconditionFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "tableExists";
    }
}
