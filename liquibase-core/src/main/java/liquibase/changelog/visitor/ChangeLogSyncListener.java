package liquibase.changelog.visitor;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.ChangeLog;
import liquibase.database.Database;

public interface ChangeLogSyncListener {
    void markedRan(ChangeSet changeSet, ChangeLog changeLog, Database database);

}
