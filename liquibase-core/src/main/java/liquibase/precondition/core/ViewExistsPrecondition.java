package liquibase.precondition.core;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.changelog.ChangeLog;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.precondition.AbstractPrecondition;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.core.Schema;
import liquibase.structure.core.View;

public class ViewExistsPrecondition extends AbstractPrecondition {
    public String catalogName;
    public String schemaName;
    public String viewName;

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
        String currentSchemaName;
        String currentCatalogName;
        try {
            currentCatalogName = catalogName;
            currentSchemaName = schemaName;
            if (!SnapshotGeneratorFactory.getInstance().has(new View().setName(database.correctObjectName(viewName, View.class)).setSchema(new Schema(currentCatalogName, currentSchemaName)), database)) {
                throw new PreconditionFailedException("View " + database.escapeTableName(currentCatalogName, currentSchemaName, viewName) + " does not exist", changeLog, this);
            }
        } catch (PreconditionFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "viewExists";
    }
}
