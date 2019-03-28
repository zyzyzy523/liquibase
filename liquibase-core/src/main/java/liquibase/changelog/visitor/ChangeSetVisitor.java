package liquibase.changelog.visitor;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.filter.ChangeSetFilterResult;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;

import java.util.Set;

/**
 * Called by {@link liquibase.changelog.ChangeLogIterator} when a {@link liquibase.changelog.filter.ChangeSetFilter} accept a changeSet.
 *
 * @see liquibase.changelog.visitor.SkippedChangeSetVisitor
 *
 */
public interface ChangeSetVisitor {

    public enum Direction {
        FORWARD,
        REVERSE
    }

    Direction getDirection(); 

    void visit(ChangeSet changeSet, ChangeLog changeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException;
}
