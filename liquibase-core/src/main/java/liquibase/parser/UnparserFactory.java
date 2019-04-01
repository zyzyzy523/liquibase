package liquibase.parser;

import liquibase.exception.ParseException;
import liquibase.plugin.AbstractPluginFactory;

import java.io.OutputStream;

/**
 * Factory for {@link Unparser} plugins.
 */
public class UnparserFactory extends AbstractPluginFactory<Unparser> {

    protected UnparserFactory() {

    }

    @Override
    protected Class<Unparser> getPluginClass() {
        return Unparser.class;
    }

    @Override
    protected int getPriority(Unparser obj, Object... args) {
        return obj.getPriority((String) args[0]);
    }

    /**
     * Returns the {@link Unparser} to use for the given path.
     */
    public Unparser getUnparser(String path) {
        return getPlugin(path);
    }

    public void unparse(Object object, String outputPath, OutputStream outputStream) throws ParseException {
        Unparser unparser = getUnparser(outputPath);

        if (unparser == null) {
            throw new ParseException("Could not find an unparser for " + outputPath, null);
        }
        unparser.unparse(object, outputPath, outputStream);
    }
}
