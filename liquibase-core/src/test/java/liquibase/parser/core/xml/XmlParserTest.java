package liquibase.parser.core.xml;

public class XmlParserTest {

//    @Test
//    public void testIgnoreDuplicateChangeSets() throws ChangeLogParseException, Exception {
//        XmlParser xmlParser = new XmlParser();
//        DatabaseChangeLog changeLog = xmlParser.parse("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/master.changelog.xml");
//
//        final List<ChangeSet> changeSets = new ArrayList<ChangeSet>();
//
//        new ChangeLogIterator(changeLog).run(new ChangeSetVisitor() {
//            @Override
//            public Direction getDirection() {
//                return Direction.FORWARD;
//            }
//
//            @Override
//            public void visit(ChangeSet changeSet, DatabaseChangeLog databaseChangeLog, Database database, Set<ChangeSetFilterResult> filterResults) throws LiquibaseException {
//                changeSets.add(changeSet);
//            }
//        }, new RuntimeEnvironment(new MockDatabase(), new Contexts(), new LabelExpression()));
//
//
//        Assert.assertEquals(8, changeSets.size());
//
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog4.xml::1::testuser",
//            changeSets.get(0).toString());
//
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog4.xml::1::testuser",
//            changeSets.get(1).toString());
//        Assert.assertEquals(1, changeSets.get(1).getContexts().getContexts().size());
//
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog4.xml::1::testuser",
//            changeSets.get(2).toString());
//        Assert.assertEquals(1, changeSets.get(2).getLabels().getLabels().size());
//
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog4.xml::1::testuser",
//            changeSets.get(3).toString());
//        Assert.assertEquals(2, changeSets.get(3).getLabels().getLabels().size());
//
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog4.xml::1::testuser",
//            changeSets.get(4).toString());
//        Assert.assertEquals(1, changeSets.get(4).getDbmsSet().size());
//
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog1.xml::1::testuser",
//            changeSets.get(5).toString());
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog3.xml::1::testuser",
//            changeSets.get(6).toString());
//        Assert.assertEquals("liquibase/parser/core/xml/ignoreDuplicatedChangeLogs/included.changelog2.xml::1::testuser",
//            changeSets.get(7).toString());
//    }

}
