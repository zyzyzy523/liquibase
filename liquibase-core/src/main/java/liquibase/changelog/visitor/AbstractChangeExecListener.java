package liquibase.changelog.visitor;

import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.ChangeSet.ExecType;
import liquibase.changelog.ChangeSet.RunStatus;
import liquibase.changelog.ChangeLog;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.precondition.core.PreconditionContainer.ErrorOption;
import liquibase.precondition.core.PreconditionContainer.FailOption;

/*
 * Default implementation of the ChangeExecListener so that sub classes can just override the methods
 * they are interested in.
 */
public abstract class AbstractChangeExecListener implements ChangeExecListener {
    @Override
    public void willRun(ChangeSet changeSet,
                        ChangeLog changeLog, Database database,
                        RunStatus runStatus) {
    }

    @Override
    public void ran(ChangeSet changeSet, ChangeLog changeLog,
            Database database, ExecType execType) {
    }

    @Override
    public void rolledBack(ChangeSet changeSet,
                           ChangeLog changeLog, Database database) {
    }

    @Override
    public void preconditionFailed(PreconditionFailedException error,
            FailOption onFail) {
    }

    @Override
    public void preconditionErrored(PreconditionErrorException error,
            ErrorOption onError) {
    }

    @Override
    public void willRun(Change change, ChangeSet changeSet,
                        ChangeLog changeLog, Database database) {
    }

    @Override
    public void ran(Change change, ChangeSet changeSet,
                    ChangeLog changeLog, Database database) {
    }

    @Override
    public void runFailed(ChangeSet changeSet,
                          ChangeLog changeLog, Database database,
                          Exception exception) {
    }
}
