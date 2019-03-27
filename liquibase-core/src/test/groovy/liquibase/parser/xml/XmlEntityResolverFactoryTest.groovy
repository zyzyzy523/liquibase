package liquibase.parser.xml

import liquibase.Scope
import spock.lang.Specification

class XmlEntityResolverFactoryTest extends Specification {

    def "correctly finds LiquibaseXmlEntityResolver"() {
        expect:
        Scope.getCurrentScope().getSingleton(XmlEntityResolverFactory).getResolver(null, null, null, systemId) instanceof LiquibaseXmlEntityResolver

        where:
        systemId << ["http://www.liquibase.org/xml/ns/test.xsd", "http://liquibase.org/xml/ns/test.xsd"]
    }
}
