package liquibase.parser.json

import liquibase.Scope
import liquibase.exception.ParseException
import liquibase.resource.MockResourceAccessor
import spock.lang.Specification
import spock.lang.Unroll

class JsonParserTest extends Specification {

    def "throws exception if nothing matches"() {
        when:
        new JsonParser().parse(null, "com/example/invalid.json",)

        then:
        def e = thrown(ParseException)
        e.message == "Could not find file to parse: com/example/invalid.json"
    }

    @Unroll("#featureName: #notes")
    def "can parse a simple file"() {
        when:
        def parsedNode = null
        def resourceAccessor = new MockResourceAccessor()
                .addResource("com/example/test.json", json)

        Scope.child(Scope.Attr.resourceAccessor, resourceAccessor, {
            parsedNode = new JsonParser().parse(null, "com/example/test.json",)
        })

        then:
        parsedNode.prettyPrint() == expected

        where:
        [json, expected, notes] << [
                [
                        """
{
  "rootNode": {
    "attr1": "A",
    "attr2": "B"
  }
}
""",
                        """
rootNode
    attr1: A
    attr2: B
    physicalPath: com/example/test.json
""".trim(),
                        "simple file"
                ],
                [
                        """
{
  "rootNode": {
    "child1": "Child 1",
    "child2": "Child 2",
    "parent": {
      "innerNode": "inner node value",
      "innerNode2": "inner node value 2"
    }
  }
}
""",
                        """
rootNode
    child1: Child 1
    child2: Child 2
    parent
        innerNode: inner node value
        innerNode2: inner node value 2
    physicalPath: com/example/test.json
""".trim(),
                        "nested nodes"
                ]
        ]
    }

    @Unroll
    def "invalid json throws an error"() {
        when:
        def parsedNode = null
        def resourceAccessor = new MockResourceAccessor()
                .addResource("com/example/test.json", json)

        Scope.child(Scope.Attr.resourceAccessor, resourceAccessor, {
            parsedNode = new JsonParser().parse(null, "com/example/test.json",)
        })

        then:
        def e = thrown(ParseException)
        e.message == expected

        where:
        [json, expected] << [
                [
                        "rootNode {",
                        "com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path \$"
                ]
        ]
    }
}
