package liquibase.precondition;

import liquibase.ExtensibleObject;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.visitor.ChangeExecListener;
import liquibase.database.Database;
import liquibase.exception.PreconditionErrorException;
import liquibase.exception.PreconditionFailedException;
import liquibase.exception.ValidationErrors;
import liquibase.exception.Warnings;
import liquibase.plugin.Plugin;

/**
 * Defines checks that can be performed
 */
public interface Precondition extends ExtensibleObject, Plugin {
    String getName();

    int getPriority(String preconditionName);

    Warnings warn(Database database);

    ValidationErrors validate(Database database);

    void check(Database database, ChangeLog changeLog, ChangeSet changeSet, ChangeExecListener changeExecListener) throws PreconditionFailedException, PreconditionErrorException;


}
