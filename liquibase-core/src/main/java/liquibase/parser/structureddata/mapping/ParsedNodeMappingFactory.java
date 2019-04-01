package liquibase.parser.structureddata.mapping;

import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.structureddata.mapping.core.DefaultExtensibleObjectParsedNodeMapping;
import liquibase.plugin.AbstractPluginFactory;

/**
 * Manages {@link ParsedNodeMapping} plugins
 */
public class ParsedNodeMappingFactory extends AbstractPluginFactory<ParsedNodeMapping> {

    protected ParsedNodeMappingFactory() {
    }

    @Override
    protected Class<ParsedNodeMapping> getPluginClass() {
        return ParsedNodeMapping.class;
    }

    @Override
    protected int getPriority(ParsedNodeMapping obj, Object... args) {
        return obj.getPriority((ParsedNode) args[0], (Class) args[1], (Class) args[2], (String) args[3]);
    }

    /**
     * Returns the correct {@link ParsedNodeMapping} for the passed attributes
     */
    public ParsedNodeMapping getMapping(ParsedNode node, Class objectType, Class containerType, String containerAttribute) {
        return getPlugin(node, objectType, containerType, containerAttribute);
    }

    /**
     * Convenience method to call {@link ParsedNodeMapping#toObject(ParsedNode, Class, Class, String)} on the correct {@link ParsedNodeMapping}.
     */
    public <ObjectType> ObjectType toObject(ParsedNode node, Class<ObjectType> objectType, Class containerType, String containerAttribute) throws ParseException {
        if (node == null) {
            return null;
        }
        ParsedNodeMapping mapping = getMapping(node, objectType, containerType, containerAttribute);
        if (mapping == null) {
            throw new ParseException("Cannot find ParsedNodeMapping for "+new DefaultExtensibleObjectParsedNodeMapping().describeParams(node, objectType, containerType, containerAttribute), node);
        }
        return (ObjectType) mapping.toObject(node, objectType, containerType, containerAttribute);
    }

    /**
     * Convenience method to call {@link ParsedNodeMapping#toParsedNode(Object, Class, String, ParsedNode)} on the correct {@link ParsedNodeMapping}.
     */
    public ParsedNode toParsedNode(Object object, Class containerType, String containerAttribute, ParsedNode parentNode) throws ParseException {
        if (object == null) {
            return null;
        }

        ParsedNodeMapping mapping = getMapping(null, object.getClass(), containerType, containerAttribute);
        if (mapping == null) {
            throw new ParseException("Cannot find ParsedNodeMapping for "+new DefaultExtensibleObjectParsedNodeMapping().describeParams(null, object.getClass(), containerType, containerAttribute), null);
        }

        return mapping.toParsedNode(object, containerType, containerAttribute, parentNode);
    }


}
