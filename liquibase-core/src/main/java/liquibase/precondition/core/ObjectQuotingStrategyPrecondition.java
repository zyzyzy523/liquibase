package liquibase.precondition.core;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.precondition.AbstractPrecondition;

public class ObjectQuotingStrategyPrecondition extends AbstractPrecondition {

    public ObjectQuotingStrategy strategy;

    @Override
    public String getName() {
        return "expectedQuotingStrategy";
    }

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
        //No longer checked since strategy is not set the same anymore
//        try {
//            if (changeLog.objectQuotingStrategy != strategy) {
//                throw new PreconditionFailedException("Quoting strategy Precondition failed: expected "
//                        + strategy +", got "+changeSet.getObjectQuotingStrategy(), changeLog, this);
//            }
//        } catch (PreconditionFailedException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new PreconditionErrorException(e, changeLog, this);
//        }
    }
}
