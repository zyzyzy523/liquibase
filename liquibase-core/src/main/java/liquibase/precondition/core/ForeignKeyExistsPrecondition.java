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
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.util.StringUtil;

public class ForeignKeyExistsPrecondition extends AbstractPrecondition {
    public String catalogName;
    public String schemaName;
    public String foreignKeyTableName;
    public String foreignKeyName;

    @Override
    public Warnings warn(Database database) {
        return new Warnings();
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }

    @Override
    public void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener) throws PreconditionFailedException, PreconditionErrorException {
        try {
            ForeignKey example = new ForeignKey();
            example.setName(foreignKeyName);
            example.setForeignKeyTable(new Table());
            if (StringUtil.trimToNull(foreignKeyTableName) != null) {
                example.getForeignKeyTable().setName(foreignKeyTableName);
            }
            example.getForeignKeyTable().setSchema(new Schema(catalogName, schemaName));

            if (!SnapshotGeneratorFactory.getInstance().has(example, database)) {
                throw new PreconditionFailedException("Foreign Key " +
                        database.escapeIndexName(catalogName, schemaName, foreignKeyName) + " does not exist",
                        changeLog,
                        this
                );
            }
        } catch (PreconditionFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "foreignKeyConstraintExists";
    }
}
