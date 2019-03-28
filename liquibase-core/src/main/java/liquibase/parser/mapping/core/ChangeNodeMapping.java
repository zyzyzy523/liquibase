package liquibase.parser.mapping.core;

import liquibase.Scope;
import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.mapping.AbstractParsedNodeMapping;
import liquibase.parser.mapping.ParsedNodeMapping;

import java.lang.reflect.Type;

/**
 * {@link ParsedNodeMapping} for {@link Change} objects.
 * It will create the correct action implementation based on the node name using {@link ChangeFactory}.
 */
public class ChangeNodeMapping  extends AbstractParsedNodeMapping<Change> {

    @Override
    public int getPriority(ParsedNode parsedNode, Class objectType, Type containerType, String containerAttribute) {
        if (Change.class.isAssignableFrom(objectType)) {
            return PRIORITY_SPECIALIZED;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    public Change toObject(ParsedNode parsedNode, Class<Change> objectType, Class containerType, String containerAttribute) throws ParseException {
        Change change = Scope.getCurrentScope().getSingleton(ChangeFactory.class).create(parsedNode.getName());
        if (change == null) {
            throw new ParseException("Unknown change: "+parsedNode.getName(), parsedNode);
        }

        change.load(parsedNode);
        return change;
    }
}
