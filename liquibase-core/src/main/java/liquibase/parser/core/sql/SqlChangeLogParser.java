package liquibase.parser.core.sql;

import liquibase.Scope;
import liquibase.change.core.RawSQLChange;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

public class SqlChangeLogParser implements ChangeLogParser {

    @Override
    public boolean supports(String changeLogFile) {
        return changeLogFile.endsWith(".sql");
    }

    @Override
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }
    
    @Override
    public ChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters) throws ChangeLogParseException {

        ChangeLog changeLog = new ChangeLog();
        changeLog.physicalPath  = physicalChangeLogLocation;

        RawSQLChange change = new RawSQLChange();

        try {
            InputStream sqlStream = Scope.getCurrentScope().getResourceAccessor().openStream(null, physicalChangeLogLocation);
            if (sqlStream == null) {
                throw new ChangeLogParseException("File does not exist: "+physicalChangeLogLocation);
            }
            String sql = StreamUtil.readStreamAsString(sqlStream);
            change.setSql(sql);
        } catch (IOException e) {
            throw new ChangeLogParseException(e);
        }
        change.setSplitStatements(false);
        change.setStripComments(false);

        ChangeSet changeSet = new ChangeSet("raw", "includeAll", false, false, physicalChangeLogLocation, null, null, true, ObjectQuotingStrategy.LEGACY, changeLog);
        changeSet.addChange(change);

        changeLog.addChangeSet(changeSet);

        return changeLog;
    }
}
