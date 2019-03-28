package liquibase.parser.core.xml;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeLogParameters;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ParsedNode;

public abstract class AbstractChangeLogParser implements ChangeLogParser {

    @Override
    public ChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters) throws ChangeLogParseException {
        ParsedNode parsedNode = parseToNode(physicalChangeLogLocation, changeLogParameters);
        if (parsedNode == null) {
            return null;
        }

        ChangeLog changeLog = new ChangeLog(physicalChangeLogLocation);
        changeLog.setChangeLogParameters(changeLogParameters);
        try {
            changeLog.load(parsedNode);
        } catch (Exception e) {
            throw new ChangeLogParseException(e);
        }

        return changeLog;
    }

    protected abstract ParsedNode parseToNode(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters) throws ChangeLogParseException;
}
