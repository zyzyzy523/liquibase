package liquibase.parser.structureddata.mapping.core;

import liquibase.Scope;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.structureddata.mapping.AbstractParsedNodeMapping;
import liquibase.parser.structureddata.mapping.ParsedNodeMapping;
import liquibase.precondition.Precondition;
import liquibase.precondition.PreconditionFactory;

import java.lang.reflect.Type;

/**
 * {@link ParsedNodeMapping} for {@link liquibase.precondition.Precondition} objects.
 * It will create the correct action implementation based on the node name using {@link liquibase.precondition.PreconditionFactory}.
 */
public class PreconditionNodeMapping extends AbstractParsedNodeMapping<Precondition> {

    @Override
    public int getPriority(ParsedNode parsedNode, Class objectType, Type containerType, String containerAttribute) {
        if (Precondition.class.isAssignableFrom(objectType)) {
            return PRIORITY_SPECIALIZED;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    protected Precondition createObject(ParsedNode parsedNode, Class<Precondition> objectType, Class containerType, String containerAttribute) throws ParseException {
        return Scope.getCurrentScope().getSingleton(PreconditionFactory.class).getPrecondition(parsedNode.getName());
    }
}
