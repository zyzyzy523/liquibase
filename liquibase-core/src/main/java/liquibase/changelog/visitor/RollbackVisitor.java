package liquibase.changelog.visitor;

import liquibase.Scope;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.filter.ChangeSetFilterResult;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.logging.LogType;

import java.util.Set;

public class RollbackVisitor implements ChangeSetVisitor {

    private Database database;

    private ChangeExecListener execListener;

    /**
     * @deprecated - please use the constructor with ChangeExecListener, which can be null.
     */
    @Deprecated
    public RollbackVisitor(Database database) {
        this.database = database;
    }

    public RollbackVisitor(Database database, ChangeExecListener listener) {
        this(database);
        this.execListener = listener;
    }

    @Override
    public Direction getDirection() {
        return ChangeSetVisitor.Direction.REVERSE;
    }

    @Override
    public void visit(ChangeSet changeSet, ChangeLog changeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException {
        Scope.getCurrentScope().getLog(getClass()).info(LogType.USER_MESSAGE, "Rolling Back Changeset:" + changeSet);
        changeSet.rollback(this.database, this.execListener);
        this.database.removeRanStatus(changeSet);
        sendRollbackEvent(changeSet, changeLog, database);
        this.database.commit();

    }

    private void sendRollbackEvent(ChangeSet changeSet, ChangeLog changeLog, Database database2) {
        if (execListener != null) {
            execListener.rolledBack(changeSet, changeLog, database);
        }
    }
}
