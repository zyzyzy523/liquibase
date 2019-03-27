package liquibase.parser.xml;

import liquibase.Scope;
import liquibase.plugin.AbstractPluginFactory;

/**
 * Factory to find {@link XmlEntityResolver} plugins.
 */
public class XmlEntityResolverFactory extends AbstractPluginFactory<XmlEntityResolver> {

    protected XmlEntityResolverFactory() {

    }

    @Override
    protected Class<XmlEntityResolver> getPluginClass() {
        return XmlEntityResolver.class;
    }

    @Override
    protected int getPriority(XmlEntityResolver obj, Object... args) {
        return obj.getPriority((String) args[0], (String) args[1], (String) args[2], (String) args[3]);
    }

    public XmlEntityResolver getResolver(String name, String publicId, String baseURI, String systemId) {
        return this.getPlugin(name, publicId, baseURI, systemId);
    }
}
