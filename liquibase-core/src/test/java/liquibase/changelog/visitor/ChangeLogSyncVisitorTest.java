package liquibase.changelog.visitor;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import org.junit.Before;


public class ChangeLogSyncVisitorTest {
    private ChangeSet changeSet;
    private ChangeLog changeLog;

    @Before
    public void setUp() {
        changeSet = new ChangeSet("1", "testAuthor", false, false, "path/changelog", null, null, null);
        changeLog = new ChangeLog();
    }

//    @Test
//    public void testVisitDatabaseConstructor() throws LiquibaseException {
//        Database mockDatabase = mock(Database.class);
//        ChangeLogSyncVisitor visitor = new ChangeLogSyncVisitor(mockDatabase);
//        visitor.visit(changeSet, databaseChangeLog, mockDatabase, Collections.<ChangeSetFilterResult>emptySet());
//        verify(mockDatabase).markChangeSetExecStatus(changeSet, ChangeSet.ExecType.EXECUTED);
//    }

//    @Test
//    public void testVisitListenerConstructor() throws LiquibaseException {
//        Database mockDatabase = mock(Database.class);
//        ChangeLogSyncListener mockListener = mock(ChangeLogSyncListener.class);
//        ChangeLogSyncVisitor visitor = new ChangeLogSyncVisitor(mockDatabase, mockListener);
//        visitor.visit(changeSet, databaseChangeLog, mockDatabase, Collections.<ChangeSetFilterResult>emptySet());
//        verify(mockDatabase).markChangeSetExecStatus(changeSet, ChangeSet.ExecType.EXECUTED);
//        verify(mockListener).markedRan(changeSet, databaseChangeLog, mockDatabase);
//    }
}
