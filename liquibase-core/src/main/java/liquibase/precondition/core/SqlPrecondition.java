package liquibase.precondition.core;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.exception.*;
import liquibase.executor.ExecutorService;
import liquibase.precondition.AbstractPrecondition;
import liquibase.statement.core.RawSqlStatement;

public class SqlPrecondition extends AbstractPrecondition {

    public String expectedResult;
    public String sql;

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
        DatabaseConnection connection = database.getConnection();
        try {
            Object oResult = ExecutorService.getInstance().getExecutor(database).queryForObject(new RawSqlStatement(sql.replaceFirst(";$", "")), String.class);
            String result = oResult.toString();
            if (result == null) {
                throw new PreconditionFailedException("No rows returned from SQL Precondition", changeLog, this);
            }

            String expectedResult = this.expectedResult;
            if (!expectedResult.equals(result)) {
                throw new PreconditionFailedException("SQL Precondition failed.  Expected '" + expectedResult + "' got '" + result + "'", changeLog, this);
            }

        } catch (DatabaseException e) {
            throw new PreconditionErrorException(e, changeLog, this);
        }
    }

    @Override
    public String getName() {
        return "sqlCheck";
    }

//    @Override
//    public SerializationType getSerializableFieldType(String field) {
//        if ("sql".equals(field)) {
//            return SerializationType.DIRECT_VALUE;
//        }
//        return super.getSerializableFieldType(field);
//    }
}
