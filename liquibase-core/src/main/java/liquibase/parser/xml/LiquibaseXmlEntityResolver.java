package liquibase.parser.xml;

/**
 * Finds liquibase XSD classes.
 */
public class LiquibaseXmlEntityResolver extends XmlEntityResolver {

    @Override
    public int getPriority(String name, String publicId, String baseURI, String systemId) {
        if (systemId.startsWith("http://www.liquibase.org/") || systemId.startsWith("http://liquibase.org/")) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    protected String getPath(String name, String publicId, String baseURI, String systemId) {
        return super.getPath(name, publicId, baseURI, systemId)
                .replace("www.liquibase.org/xml/ns/migrator", "www.liquibase.org/xml/ns/dbchangelog");
    }
}
