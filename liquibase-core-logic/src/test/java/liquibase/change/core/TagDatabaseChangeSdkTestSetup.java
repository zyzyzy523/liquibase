package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.statement.core.CreateDatabaseChangeLogTableStatement;
import liquibase.statement.core.MarkChangeSetRanStatement;

public class TagDatabaseChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
        execute(new CreateDatabaseChangeLogTableStatement());
        execute(new MarkChangeSetRanStatement(new ChangeSet("1", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED));
        execute(new MarkChangeSetRanStatement(new ChangeSet("2", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED));
        execute(new MarkChangeSetRanStatement(new ChangeSet("3", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED));
        execute(new MarkChangeSetRanStatement(new ChangeSet("4", "test", false, false, "com/example/test.xml", null, null, new DatabaseChangeLog("com/example/test.xml")), ChangeSet.ExecType.EXECUTED));

        return null;
    }
}
