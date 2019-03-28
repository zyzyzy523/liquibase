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
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.util.StringUtil;

public class PrimaryKeyExistsPrecondition extends AbstractPrecondition {

    public String catalogName;
    public String schemaName;
    public String primaryKeyName;
    public String tableName;

    @Override
    public Warnings warn(Database database) {
        return new Warnings();
    }

    @Override
    public ValidationErrors validate(Database database) {
        ValidationErrors validationErrors = new ValidationErrors();
        if ((primaryKeyName == null) && (tableName == null)) {
            validationErrors.addError("Either primaryKeyName or tableName must be set");
        }
        return validationErrors;
    }

    @Override
    public void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener)
            throws PreconditionFailedException, PreconditionErrorException {
        try {
            PrimaryKey example = new PrimaryKey();
            Table table = new Table();
            table.setSchema(new Schema(catalogName, schemaName));
            if (StringUtil.trimToNull(tableName) != null) {
                table.setName(tableName);
            }
            example.setTable(table);
            example.setName(primaryKeyName);

            if (!SnapshotGeneratorFactory.getInstance().has(example, database)) {
                if (tableName != null) {
                    throw new PreconditionFailedException("Primary Key does not exist on " + database.escapeObjectName(tableName, Table.class), changeLog, this);
                } else {
                    throw new PreconditionFailedException("Primary Key " + database.escapeObjectName(primaryKeyName, PrimaryKey.class) + " does not exist", changeLog, this);
                }
            }
        } catch (PreconditionFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "primaryKeyExists";
    }
}
