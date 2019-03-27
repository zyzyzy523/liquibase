package liquibase.parser.xml;

import liquibase.Scope;
import liquibase.exception.ParseException;
import liquibase.plugin.AbstractPlugin;
import org.xml.sax.InputSource;

import java.io.InputStream;

/**
 * Base class for plugins that are able to find XML Entities locally rather than over the network. Primarily used for finding XSD schemas in the classpath.
 * Lookup objects with {@link XmlEntityResolverFactory}
 */
public abstract class XmlEntityResolver extends AbstractPlugin {

    public abstract int getPriority(String name, String publicId, String baseURI, String systemId);

    /**
     * Return an InputSource for the given entity description.
     * Default implementation uses {@link #getPath(String, String, String, String)} plus the filename in the passed systemId and looks that up in the scope's resourceAccessor.
     *
     * @return null if cannot be resolved
     */
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws ParseException {
        String path = getPath(name, publicId, baseURI, systemId);

        try {
            InputStream stream = Scope.getCurrentScope().getResourceAccessor().openStream(null, path);

            if (stream == null) {
                return null;
            }

            return new InputSource(stream);
        } catch (Exception e) {
            throw new ParseException(e, null);
        }
    }

    protected String getPath(String name, String publicId, String baseURI, String systemId) {
        return systemId.replaceFirst("^https?://","");
    }
}
