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
import liquibase.structure.core.Column;
import liquibase.structure.core.Index;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.util.StringUtil;

public class IndexExistsPrecondition extends AbstractPrecondition {
    public String catalogName;
    public String schemaName;
    public String tableName;
    public String columnNames;
    public String indexName;

    @Override
    public Warnings warn(Database database) {
        return new Warnings();
    }

    @Override
    public ValidationErrors validate(Database database) {
        ValidationErrors validationErrors = new ValidationErrors();
        if (indexName == null && (tableName == null || columnNames == null)) {
            validationErrors.addError("indexName OR (tableName and columnNames) is required");
        }
        return validationErrors;
    }

    @Override
    public void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener)
            throws PreconditionFailedException, PreconditionErrorException {
        try {
            Schema schema = new Schema(catalogName, schemaName);
            Index example = new Index();
            String tableName = StringUtil.trimToNull(this.tableName);
            if (tableName != null) {
                example.setRelation(new Table()
                        .setName(database.correctObjectName(tableName, Table.class))
                        .setSchema(schema));
            }
            example.setName(database.correctObjectName(indexName, Index.class));
            if (StringUtil.trimToNull(columnNames) != null) {
                for (String column : columnNames.split("\\s*,\\s*")) {
                    example.addColumn(new Column(database.correctObjectName(column, Column.class)));
                }
            }
            if (!SnapshotGeneratorFactory.getInstance().has(example, database)) {
                String name = "";

                if (indexName != null) {
                    name += database.escapeObjectName(indexName, Index.class);
                }

                if (tableName != null) {
                    name += " on " + database.escapeObjectName(tableName, Table.class);

                    if (StringUtil.trimToNull(columnNames) != null) {
                        name += " columns " + columnNames;
                    }
                }
                throw new PreconditionFailedException("Index " + name + " does not exist", changeLog, this);
            }
        } catch (Exception e) {
            if (e instanceof PreconditionFailedException) {
                throw (((PreconditionFailedException) e));
            }
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "indexExists";
    }

    @Override
    public String toString() {
        String string = "Index Exists Precondition: ";

        if (indexName != null) {
            string += indexName;
        }

        if (tableName != null) {
            string += " on " + tableName;

            if (StringUtil.trimToNull(columnNames) != null) {
                string += " columns " + columnNames;
            }
        }

        return string;
    }
}
