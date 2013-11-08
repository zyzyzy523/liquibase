package liquibase.change.core;

import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.diff.DiffResult;
import liquibase.executor.ExecutorService;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.statement.core.CreateDatabaseChangeLogTableStatement;
import liquibase.statement.core.MarkChangeSetRanStatement;
import liquibase.statement.core.RawSqlStatement;

import static org.junit.Assert.assertTrue;

public class TagDatabaseChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public Change[] prepareDatabase() throws Exception {
        execute(new CreateDatabaseChangeLogTableStatement(),
                                new MarkChangeSetRanStatement(new ChangeSet("1", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED),
                                new MarkChangeSetRanStatement(new ChangeSet("2", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED),
                                new MarkChangeSetRanStatement(new ChangeSet("3", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED),
                                new MarkChangeSetRanStatement(new ChangeSet("4", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED)
        );

        return null;
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        TagDatabaseChange change = (TagDatabaseChange) getChange();
        int rows = ExecutorService.getInstance().getExecutor(getDatabase()).queryForInt(new RawSqlStatement("select count(*) from " + getDatabase().getDatabaseChangeLogTableName() + " where tag='" + change.getTag() + "'"));
        assertTrue(rows > 0);

    }
}
