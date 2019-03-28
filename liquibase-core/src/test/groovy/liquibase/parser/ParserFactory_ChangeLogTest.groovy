package liquibase.parser

import liquibase.Scope
import liquibase.changelog.ChangeLog
import liquibase.resource.FileSystemResourceAccessor
import liquibase.test.TestContext
import liquibase.util.StringUtil
import spock.lang.Specification
import spock.lang.Unroll

class ParserFactory_ChangeLogTest extends Specification {

    /**
     * Loads all files in the src/test/resources/liquibase/parser/tests.changelog directory
     * then compares the parsed version to the line after "Expected Output" in the file.
     *
     * Allows us to easily add files to test
     */
    @Unroll("#featureName: #file.name")
    def "can parse test changelog"() {
        when:
        def fileObject = file as File
        def resourceAccessor = new FileSystemResourceAccessor(fileObject.parentFile)
        def text = fileObject.text

        def type = fileObject.name.replaceFirst(/.*\./, "")
        def expected = getExpectedOutput(text, type)

        def changelog = null
        Scope.child(Scope.Attr.resourceAccessor, resourceAccessor, {
            changelog = Scope.currentScope.getSingleton(ParserFactory).parse(null, fileObject.name, ChangeLog)
        })

        then:
        changelog.describe() == expected.trim()

        where:
        file << findTestChangelogs()
    }

    String getExpectedOutput(String text, String type) {
        def expected = StringUtil.trimToNull(text.find(/Expected Output:\r\n.*\r\n/).replace("Expected Output:", ""))
        if (type == "yaml") {
            expected = expected.replaceFirst(/^#/, "")
        }

        return expected
    }

    File[] findTestChangelogs() {
        return new File(TestContext.getInstance().findCoreProjectRoot(), "src/test/resources/liquibase/parser/tests/changelog").listFiles()
    }

//    def "invalid attributes throw an exception"() {
//        when:
//        def scope = JUnitScope.getInstance()
//        def parserFactory = scope.getSingleton(ParserFactory)
//
//        def node = ParsedNode.createRootNode("changeLog")
//        node.addChildren([
//                changeSet: [
//                        id         : "1",
//                        author     : "test",
//                        invalidAttr: "should throw exception"
//                ]
//        ])
//        parserFactory.parse(node, ChangeLog, scope)
//
//        then:
//        def e = thrown(ParseException)
//        e.message == "Unexpected attribute 'invalidAttr' for liquibase.changelog.ChangeSet"
//
//    }
//
//    def "invalid attributes on objects that just take a value throw an exception"() {
//        when:
//        def scope = JUnitScope.getInstance()
//        def parserFactory = scope.getSingleton(ParserFactory)
//
//        def node = ParsedNode.createRootNode("changeLog")
//        node.addChildren([
//                changeSet: [
//                        id         : "1",
//                        author     : "test",
//                        executeCommand: [
//                                arg: [
//                                        value: "this",
//                                        invalidAttr: "should throw exception"
//                                ]
//
//                        ]
//                ]
//        ])
//        parserFactory.parse(node, ChangeLog, scope)
//
//        then:
//        def e = thrown(ParseException)
//        e.message == "Unexpected attribute(s) 'invalidAttr' for java.lang.String"
//
//    }
//

}
