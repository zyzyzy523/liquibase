package liquibase.sdk.standardtests.change;

import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.sdk.state.Setup;
import liquibase.statement.SqlStatement;

public abstract class StandardChangeSdkTestSetup implements Setup {

    private Change change;
    private Database database;

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    protected void execute(SqlStatement... statements) throws DatabaseException {
        if (database.getConnection() == null) {
            return;
        }
        for (SqlStatement statement : statements) {
            ExecutorService.getInstance().getExecutor(database).execute(statement);
        }

    }

    protected void execute(Change... changes) throws DatabaseException {
        if (database.getConnection() == null) {
            return;
        }
        for (Change change : changes) {
            ExecutorService.getInstance().getExecutor(database).execute(change);
        }
    }

}
