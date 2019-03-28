package liquibase.parser.xml

import liquibase.Scope
import liquibase.exception.ParseException
import liquibase.resource.MockResourceAccessor
import spock.lang.Specification
import spock.lang.Unroll

class XmlParserTest extends Specification {

    static MOCK_XSD_HEADER = "xmlns='http://www.liquibase.org/xml/ns/mock' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd'"

    @Unroll
    def "throws exception if file does not exist"() {
        when:
        new XmlParser().parse(relativeTo, path,)

        then:
        def e = thrown(ParseException)
        e.message == message

        where:
        relativeTo    | path                      | message
        null          | "com/example/invalid.xml" | "Could not find file to parse: com/example/invalid.xml"
        "com/example" | "invalid.xml"             | "Could not find file to parse: invalid.xml relative to com/example"
    }

    @Unroll("#featureName: #notes")
    def "can parse a simple file"() {
        when:
        def parsedNode = null
        def resourceAccessor = new MockResourceAccessor()
                .addResource("com/example/test.xml", xml)
                .addMockXsd("rootNode")

        Scope.child(Scope.Attr.resourceAccessor, resourceAccessor, {
            parsedNode = new XmlParser().parse(null, "com/example/test.xml",)
        })

        then:
        parsedNode.toString() == expected

        where:
        xml                                                                                                                                                                                    | expected                                                                                                                                                                                                                                                                                                                                                                                  | notes
        "<rootNode $MOCK_XSD_HEADER/>"                                                                                                                                                         | "ParsedNode{rootNode, children=[ParsedNode{schemaLocation=http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd}, ParsedNode{physicalPath=com/example/test.xml}]}"                                                                                                                                                                                    | "simple file"
        "<rootNode $MOCK_XSD_HEADER attr1='A' attr2='B'/>"                                                                                                                                     | "ParsedNode{rootNode, children=[ParsedNode{schemaLocation=http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd}, ParsedNode{attr1=A}, ParsedNode{attr2=B}, ParsedNode{physicalPath=com/example/test.xml}]}"                                                                                                                                          | "has attributes"
        "<rootNode $MOCK_XSD_HEADER attr1='A' attr2='B'>Body Here</rootNode>"                                                                                                                  | "ParsedNode{rootNode=Body Here, children=[ParsedNode{schemaLocation=http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd}, ParsedNode{attr1=A}, ParsedNode{attr2=B}, ParsedNode{physicalPath=com/example/test.xml}]}"                                                                                                                                | "has attributes and a body"
        "<rootNode $MOCK_XSD_HEADER attr1='  An attribute '>    Body Here                  </rootNode>"                                                                                        | "ParsedNode{rootNode=Body Here, children=[ParsedNode{schemaLocation=http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd}, ParsedNode{attr1=An attribute}, ParsedNode{physicalPath=com/example/test.xml}]}"                                                                                                                                          | "whitespace around text is ignored"
        "<rootNode $MOCK_XSD_HEADER attr1='  '>    </rootNode>"                                                                                                                                | "ParsedNode{rootNode, children=[ParsedNode{schemaLocation=http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd}, ParsedNode{attr1}, ParsedNode{physicalPath=com/example/test.xml}]}"                                                                                                                                                                 | "empty body and attributes become null"
        "<rootNode $MOCK_XSD_HEADER><child1>Child 1</child1><child2>Child 2</child2><child1>Child 1 again</child1><parent><innerNode/><innerNode2>Inner Node</innerNode2></parent></rootNode>" | "ParsedNode{rootNode, children=[ParsedNode{schemaLocation=http://www.liquibase.org/xml/ns/mock http://www.liquibase.org/xml/ns/dbchangelog/mock.xsd}, ParsedNode{child1=Child 1}, ParsedNode{child2=Child 2}, ParsedNode{child1=Child 1 again}, ParsedNode{parent, children=[ParsedNode{innerNode}, ParsedNode{innerNode2=Inner Node}]}, ParsedNode{physicalPath=com/example/test.xml}]}" | "nested nodes"
    }

    @Unroll("#featureName: #notes")
    def "invalid XML throws an error"() {
        when:
        def parsedNode = null
        def resourceAccessor = new MockResourceAccessor()
                .addResource("com/example/test.xml", xml)
                .addMockXsd("rootNode")

        Scope.child(Scope.Attr.resourceAccessor, resourceAccessor, {
            parsedNode = new XmlParser().parse(null, "com/example/test.xml",)
        })

        then:
        def e = thrown(ParseException)
        e.message == expected

        where:
        xml                                     | expected                                                                                                                                                     | notes
        "<rootNode $MOCK_XSD_HEADER></invalid>" | "org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 223; The element type \"rootNode\" must be terminated by the matching end-tag \"</rootNode>\"." | "mismatched final tag"
        "<databaseChangeLog $MOCK_XSD_HEADER><invalid/></databaseChangeLog>" | "org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 230; cvc-elt.1: Cannot find the declaration of element 'databaseChangeLog'." | "invalid elements"
    }
}
