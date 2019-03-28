package liquibase.precondition.core;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.executor.ExecutorService;
import liquibase.precondition.AbstractPrecondition;
import liquibase.statement.core.TableRowCountStatement;
import liquibase.util.StringUtil;

public class RowCountPrecondition extends AbstractPrecondition {

    public String catalogName;
    public String schemaName;
    public String tableName;
    public Integer expectedRows;

    @Override
    public Warnings warn(Database database) {
        return new Warnings();
    }

    @Override
    public ValidationErrors validate(Database database) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("tableName", tableName);
        validationErrors.checkRequiredField("expectedRows", expectedRows);

        return validationErrors;
    }

    @Override
    public void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener)
            throws PreconditionFailedException, PreconditionErrorException {
        try {
            TableRowCountStatement statement = new TableRowCountStatement(catalogName, schemaName, tableName);

            int result = ExecutorService.getInstance().getExecutor(database).queryForInt(statement);
            if (result != expectedRows) {
                throw new PreconditionFailedException(getFailureMessage(result), changeLog, this);
            }

        } catch (PreconditionFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    protected String getFailureMessage(int result) {
        return "Table " + tableName + " is not empty. Contains " + result + " rows";
    }

    @Override
    public String getName() {
        return "rowCount";
    }

}
