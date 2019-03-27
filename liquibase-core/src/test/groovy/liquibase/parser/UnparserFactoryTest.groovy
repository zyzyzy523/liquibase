package liquibase.parser

import spock.lang.Specification

class UnparserFactoryTest extends Specification {

//    @Unroll
//    def "unparse object to xml"() {
//        expect:
//        def scope = JUnitScope.instance
//        def outStream = new ByteArrayOutputStream();
//        scope.getSingleton(UnparserFactory).unparse(object, outStream, "com/example/out.xml", scope)
//
//        new String(outStream.toByteArray()).replace("\r", "") == expected.replace("\r", "").trim()
//
//        where:
//        [object, expected] << [
//                [new ChangeLog(), """
//<?xml version="1.1" encoding="utf-8"?>
//<changeLog/>
//"""],
//
//                //-------
//                [new ChangeLog().each { it.changeLogEntries.add(new ChangeSet("1", "test user", "path/to/logical.xml"))}, """
//<?xml version="1.1" encoding="utf-8"?>
//<changeLog>
//    <changeSet author="test user" id="1"/>
//</changeLog>
//"""],
//
//        ]
//    }
}
