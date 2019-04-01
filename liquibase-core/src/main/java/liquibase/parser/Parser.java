package liquibase.parser;

import liquibase.ExtensibleObject;
import liquibase.SingletonObject;
import liquibase.exception.ParseException;
import liquibase.plugin.Plugin;

/**
 * Parsers take a file and convert it into {@link ParsedNode} object.
 * They should not do any processing or fixing of the parsedNode, just output something that directly matches the file.
 *
 * @see AbstractParser
 * @see ParserFactory
 */
public interface Parser extends Plugin, ExtensibleObject, SingletonObject {

    int getPriority(String relativeTo, String path, Class objectType);

    /**
     * Parses a source into a specified object.
     */
    <ObjectType> ObjectType parse(String relativeTo, String sourcePath, Class<ObjectType> objectType) throws ParseException;

}
