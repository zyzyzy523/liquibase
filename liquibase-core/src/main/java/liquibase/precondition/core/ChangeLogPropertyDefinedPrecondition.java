package liquibase.precondition.core;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.precondition.AbstractPrecondition;

public class ChangeLogPropertyDefinedPrecondition extends AbstractPrecondition {

    public String property;
    public String value;

    @Override
    public String getName() {
        return "changeLogPropertyDefined";
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
        ChangeLogParameters changeLogParameters = changeLog.getChangeLogParameters();
        if (changeLogParameters == null) {
            throw new PreconditionFailedException("No Changelog properties were set", changeLog, this);
        }
        Object propertyValue = changeLogParameters.getValue(property, changeLog);
        if (propertyValue == null) {
            throw new PreconditionFailedException("Changelog property '" + property + "' was not set", changeLog, this);
        }
        if ((value != null) && !propertyValue.toString().equals(value)) {
            throw new PreconditionFailedException("Expected changelog property '" + property + "' to have a value of '" + value + "'.  Got '" + propertyValue + "'", changeLog, this);
        }
    }
}
