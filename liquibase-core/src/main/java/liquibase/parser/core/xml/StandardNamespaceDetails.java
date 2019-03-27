package liquibase.parser.core.xml;

import liquibase.parser.LiquibaseParser;
import liquibase.parser.NamespaceDetails;
import liquibase.parser.xml.XmlParser;
import liquibase.serializer.LiquibaseSerializable;
import liquibase.serializer.LiquibaseSerializer;
import liquibase.parser.xml.XmlUnparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandardNamespaceDetails implements NamespaceDetails {

    public static final String GENERIC_EXTENSION_XSD = "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd";
    private final Pattern standardUrlPattern;
    private final Pattern oldStandardUrlPattern;

    public StandardNamespaceDetails() {
        standardUrlPattern = Pattern.compile("http://www.liquibase.org/xml/ns/dbchangelog/(dbchangelog-[\\d\\.]+.xsd)");
        oldStandardUrlPattern = Pattern.compile("http://www.liquibase.org/xml/ns/migrator/(dbchangelog-[\\d\\.]+.xsd)");
    }

    @Override
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }

    @Override
    public boolean supports(LiquibaseSerializer serializer, String namespaceOrUrl) {
        return serializer instanceof XmlUnparser;
    }

    @Override
    public boolean supports(LiquibaseParser parser, String namespaceOrUrl) {
        return parser instanceof XmlParser;
    }

    @Override
    public String getShortName(String namespaceOrUrl) {
        if (namespaceOrUrl.equals(LiquibaseSerializable.STANDARD_CHANGELOG_NAMESPACE)) {
            return "";
        }
        return "ext";
    }

    @Override
    public String[] getNamespaces() {
        return new String[] {
                LiquibaseSerializable.STANDARD_CHANGELOG_NAMESPACE,
                LiquibaseSerializable.GENERIC_CHANGELOG_EXTENSION_NAMESPACE
        };
    }

    @Override
    public String getSchemaUrl(String namespaceOrUrl) {
        if (namespaceOrUrl.equals(LiquibaseSerializable.STANDARD_CHANGELOG_NAMESPACE)) {
            return "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-" + XmlParser.getSchemaVersion() + ".xsd";
        }
        return GENERIC_EXTENSION_XSD;
    }

    @Override
    public String getLocalPath(String namespaceOrUrl) {

        if (namespaceOrUrl.equals(GENERIC_EXTENSION_XSD)) {
            return "liquibase/parser/core/xml/dbchangelog-ext.xsd";
        }
        Matcher matcher = standardUrlPattern.matcher(namespaceOrUrl);
        if (matcher.matches()) {
            return "liquibase/parser/core/xml/"+matcher.group(1);
        }

        matcher = oldStandardUrlPattern.matcher(namespaceOrUrl);
        if (matcher.matches()) {
            return "liquibase/parser/core/xml/"+matcher.group(1);
        }

        return null;
    }
}
