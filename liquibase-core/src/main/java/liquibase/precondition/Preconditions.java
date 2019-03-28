package liquibase.precondition;

import liquibase.AbstractExtensibleObject;
import liquibase.Scope;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeLogEntry;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.executor.Executor;
import liquibase.executor.ExecutorService;
import liquibase.logging.LogType;
import liquibase.util.StreamUtil;

import java.util.ArrayList;
import java.util.List;

public class Preconditions extends AbstractExtensibleObject implements ChangeLogEntry {

    public enum FailOption {
        HALT("HALT"),
        CONTINUE("CONTINUE"),
        MARK_RAN("MARK_RAN"),
        WARN("WARN");

        String key;

        FailOption(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

    public enum ErrorOption {
        HALT("HALT"),
        CONTINUE("CONTINUE"),
        MARK_RAN("MARK_RAN"),
        WARN("WARN");

        String key;

        ErrorOption(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }


    public enum OnSqlOutputOption {
        IGNORE("IGNORE"),
        TEST("TEST"),
        FAIL("FAIL");

        String key;

        OnSqlOutputOption(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

    public ChangeLog changeLog;
    public List<Precondition> items = new ArrayList<>();

    public FailOption onFail = FailOption.HALT;
    public ErrorOption onError = ErrorOption.HALT;
    public OnSqlOutputOption onSqlOutput = OnSqlOutputOption.IGNORE;
    public String onFailMessage;
    public String onErrorMessage;

    @Override
    public ChangeLog getContainerChangeLog() {
        return changeLog;
    }

    public void add(Precondition precondition) {
        this.items.add(precondition);
    }

    public void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener)
            throws PreconditionFailedException, PreconditionErrorException {
        String ranOn = String.valueOf(changeLog);
        if (changeSet != null) {
            ranOn = String.valueOf(changeSet);
        }

        Executor executor = ExecutorService.getInstance().getExecutor(database);
        try {
            // Three cases for preConditions onUpdateSQL:
            // 1. TEST: preConditions should be run, as in regular update mode
            // 2. FAIL: the preConditions should fail if there are any
            // 3. IGNORE: act as if preConditions don't exist
            boolean testPrecondition = false;
            if (executor.updatesDatabase()) {
                testPrecondition = true;
            } else {
                if (this.onSqlOutput.equals(Preconditions.OnSqlOutputOption.TEST)) {
                    testPrecondition = true;
                } else if (this.onSqlOutput.equals(Preconditions.OnSqlOutputOption.FAIL)) {
                    throw new PreconditionFailedException("Unexpected precondition in updateSQL mode with onUpdateSQL value: " + onSqlOutput, changeLog, null);
                } else if (this.onSqlOutput.equals(Preconditions.OnSqlOutputOption.IGNORE)) {
                    testPrecondition = false;
                }
            }

            if (testPrecondition) {
                boolean allPassed = true;
                List<FailedPrecondition> failures = new ArrayList<>();
                for (Precondition precondition : this.items) {
                    try {
                        precondition.check(database, changeLog, changeSet, changeExecListener);
                    } catch (PreconditionFailedException e) {
                        failures.addAll(e.getFailedPreconditions());
                        allPassed = false;
                        break;
                    }
                }
                if (!allPassed) {
                    throw new PreconditionFailedException(failures);
                }
            }
        } catch (PreconditionFailedException e) {
            StringBuffer message = new StringBuffer();
            message.append("     ").append(e.getFailedPreconditions().size()).append(" preconditions failed").append(StreamUtil.getLineSeparator());
            for (FailedPrecondition invalid : e.getFailedPreconditions()) {
                message.append("     ").append(invalid.toString());
                message.append(StreamUtil.getLineSeparator());
            }

            if (onFailMessage != null) {
                message = new StringBuffer(onFailMessage);
            }
            if (this.onFail.equals(Preconditions.FailOption.WARN)) {
                Scope.getCurrentScope().getLog(getClass()).info(LogType.LOG, "Executing: " + ranOn + " despite precondition failure due to onFail='WARN':\n " + message);
                if (changeExecListener != null) {
                    changeExecListener.preconditionFailed(e, FailOption.WARN);
                }
            } else {
                if (onFailMessage == null) {
                    throw e;
                } else {
                    throw new PreconditionFailedException(onFailMessage, changeLog, null);
                }
            }
        } catch (PreconditionErrorException e) {
            StringBuffer message = new StringBuffer();
            message.append("     ").append(e.getErrorPreconditions().size()).append(" preconditions failed").append(StreamUtil.getLineSeparator());
            for (ErrorPrecondition invalid : e.getErrorPreconditions()) {
                message.append("     ").append(invalid.toString());
                message.append(StreamUtil.getLineSeparator());
            }

            if (this.onError.equals(Preconditions.ErrorOption.CONTINUE)) {
                Scope.getCurrentScope().getLog(getClass()).info(LogType.LOG, "Continuing past: " + toString() + " despite precondition error:\n " + message);
                throw e;
            } else if (this.onError.equals(Preconditions.ErrorOption.WARN)) {
                Scope.getCurrentScope().getLog(getClass()).warning(LogType.LOG, "Continuing past: " + toString() + " despite precondition error:\n " + message);
                if (changeExecListener != null) {
                    changeExecListener.preconditionErrored(e, ErrorOption.WARN);
                }
            } else {
                if (onErrorMessage == null) {
                    throw e;
                } else {
                    throw new PreconditionErrorException(onErrorMessage, e.getErrorPreconditions());
                }
            }
        }
    }
}
