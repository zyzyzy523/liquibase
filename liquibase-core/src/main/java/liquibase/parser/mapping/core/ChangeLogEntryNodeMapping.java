package liquibase.parser.mapping.core;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeLogEntry;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.mapping.AbstractParsedNodeMapping;
import liquibase.parser.mapping.ParsedNodeMapping;
import liquibase.precondition.Preconditions;

import java.lang.reflect.Type;

/**
 * {@link ParsedNodeMapping} for {@link ChangeLogEntry} objects.
 */
public class ChangeLogEntryNodeMapping extends AbstractParsedNodeMapping<ChangeLogEntry> {

    public ChangeLogEntryNodeMapping() {
    }

    @Override
    public int getPriority(ParsedNode parsedNode, Class objectType, Type containerType, String containerAttribute) {
        if (ChangeLogEntry.class.isAssignableFrom(objectType) && !ChangeLog.class.isAssignableFrom(objectType)) {
            return PRIORITY_SPECIALIZED;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    protected ChangeLogEntry createObject(ParsedNode parsedNode, Class<ChangeLogEntry> objectType, Class containerType, String containerAttribute) throws ParseException {
        if (parsedNode.getName().equalsIgnoreCase("changeSet")) {
            return new ChangeSet();
        } else if (parsedNode.getName().equalsIgnoreCase("preconditions")) {
            return new Preconditions();
        } else {
            return super.createObject(parsedNode, objectType, containerType, containerAttribute);
        }
    }
}
