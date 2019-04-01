package liquibase.parser;

import liquibase.Scope;
import liquibase.exception.ParseException;
import liquibase.parser.structureddata.preprocessor.ParsedNodePreprocessor;
import liquibase.plugin.AbstractPluginFactory;

/**
 * Factory for {@link Parser} plugins.
 */
public class ParserFactory extends AbstractPluginFactory<Parser> {

    protected ParserFactory() {

    }

    @Override
    protected Class<Parser> getPluginClass() {
        return Parser.class;
    }

    @Override
    protected int getPriority(Parser obj, Object... args) {
        return obj.getPriority((String) args[0], (String) args[1], (Class) args[2]);
    }

    /**
     * Returns the {@link Parser} to use for the given path.
     */
    public Parser getParser(String relativeTo, String path, Class objectType) {
        return getPlugin(relativeTo, path, objectType);
    }


    /**
     * Converts the file at sourcePath to the passed objectType using the configured {@link Parser}(s).
     * <b>This is the primary facade to use when parsing files into objects.</b>
     * If an exception is thrown, a more descriptive message will be constructed in the resulting {@link ParseException}
     */
    public <ObjectType> ObjectType parse(String relativeTo, String sourcePath, Class<ObjectType> objectType) throws ParseException {
        Parser parser = Scope.getCurrentScope().getSingleton(ParserFactory.class).getParser(relativeTo, sourcePath, objectType);
        if (parser == null) {
            throw new ParseException("Cannot find parser for " + sourcePath, null);
        }
        return parser.parse(relativeTo, sourcePath, objectType);
    }
}
