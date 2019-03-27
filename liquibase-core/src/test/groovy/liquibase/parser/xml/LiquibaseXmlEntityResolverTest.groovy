package liquibase.parser.xml


import liquibase.util.StreamUtil
import spock.lang.Specification
import spock.lang.Unroll

class LiquibaseXmlEntityResolverTest extends Specification {

    @Unroll
    def "correctly found"() {
        when:
        def inputSource = new LiquibaseXmlEntityResolver().resolveEntity(null, null, null, systemId)

        then:
        StreamUtil.readStreamAsString(inputSource.byteStream).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")

        where:
        systemId                                                          | notes
        "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd" | "standard example"
        "http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-1.0.xsd" | "oldest standard form"
        "http://www.liquibase.org/xml/ns/migrator/dbchangelog-1.0.xsd"    | "old migrator namespace"
    }

}

