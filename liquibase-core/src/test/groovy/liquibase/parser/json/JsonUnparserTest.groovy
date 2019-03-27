package liquibase.parser.json

import liquibase.parser.ParsedNode
import spock.lang.Specification
import spock.lang.Unroll

class JsonUnparserTest extends Specification {

    @Unroll
    def "unparse"() {
        when:
        def stream = new ByteArrayOutputStream()
        def node = ParsedNode.createRootNode("root")
        node.addChildren(children)
        if (additionalSetup) {
            additionalSetup.call(node)
        }

        new JsonUnparser().unparse(node, stream)

        then:
        new String(stream.toByteArray()).replace("\r\n", "\n").trim() == expected.replace("\r\n", "\n").trim()

        where:
        [children, additionalSetup, expected] << [
                [[:], { rootNode -> rootNode.value = "root value" }, """
{
  "root": "root value"
}
"""],

                //------
                [[child1: "child value"], null, """
{
  "root": {
    "child1": "child value"
  }
}
"""],

                //------
                [[:], { ParsedNode rootNode ->
                    rootNode.addChild("child1").value = "child1 value 1";
                    rootNode.addChild("child2").value = "child2 value 1";
                    rootNode.addChild("child1").value = "child1 value 2";
                }, """
{
  "root": [
    {
      "child1": "child1 value 1"
    },
    {
      "child2": "child2 value 1"
    },
    {
      "child1": "child1 value 2"
    }
  ]
}
"""],

                //------
                [[:], { ParsedNode rootNode ->
                    def childNode = rootNode.addChild("child1")
                    childNode.addChild("child1a").value = "child1a value 1";
                    childNode.addChild("child1b").value = "child1b value 1";
                    childNode = childNode.addChild("child1a");
                    childNode.addChild("grandChild1")
                    childNode.addChild("grandChild2").value = "grand child 2 value"
                    childNode.addChild("grandChild1").value = "grand child 1 value 2"

                    childNode = rootNode.addChild("child2");
                    childNode.addChild("child2a");
                    childNode.addChild("child2b").addChild("grandChild3");

                    rootNode.addChild("child1").value = "child1 value 2";
                }, """
{
  "root": [
    {
      "child1": [
        {
          "child1a": "child1a value 1"
        },
        {
          "child1b": "child1b value 1"
        },
        {
          "child1a": [
            {
              "grandChild1": null
            },
            {
              "grandChild2": "grand child 2 value"
            },
            {
              "grandChild1": "grand child 1 value 2"
            }
          ]
        }
      ]
    },
    {
      "child2": {
        "child2a": null,
        "child2b": {
          "grandChild3": null
        }
      }
    },
    {
      "child1": "child1 value 2"
    }
  ]
}
"""],

                //------
                [[nullChild: null], null, """
{
  "root": {
    "nullChild": null
  }
}"""],

                //------
                [[child: "child value", number: 52], null, """
{
  "root": {
    "child": "child value",
    "number": 52
  }
}
"""],

                //------
                [[child: [grandchild1: "grandchild 1 value", grandchild2: "grandchild 2 value"]], null, """
{
  "root": {
    "child": {
      "grandchild1": "grandchild 1 value",
      "grandchild2": "grandchild 2 value"
    }
  }
}
"""],

                //------
                [[child: "value with\nmultiple lines"], null, """
{
  "root": {
    "child": "value with\\nmultiple lines"
  }
}
"""],

        ]
    }
//
//    @Test
//    public void serialize_changeSet() {
//        //given
//        AddColumnChange addColumnChange = new AddColumnChange();
//        addColumnChange.setCatalogName("cat");
//        addColumnChange.addColumn((AddColumnConfig) new AddColumnConfig().setName("col1").setDefaultValueNumeric(3));
//        addColumnChange.addColumn((AddColumnConfig) new AddColumnConfig().setName("col2").setDefaultValueComputed(new DatabaseFunction("NOW()")));
//        addColumnChange.addColumn((AddColumnConfig) new AddColumnConfig().setName("col3").setDefaultValueBoolean(true));
//
//        // Get the Date object for 1970-01-01T00:00:00 in the current time zone.
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(0);
//        cal.set(1970, 0, 1, 0, 0, 0);
//
//        addColumnChange.addColumn((AddColumnConfig) new AddColumnConfig().setName("col2").setDefaultValueDate(
//                cal.getTime()));
//        addColumnChange.addColumn((AddColumnConfig) new AddColumnConfig().setName("col2").setDefaultValueSequenceNext(new SequenceNextValueFunction("seq_me")));
//        ChangeSet changeSet = new ChangeSet("1", "nvoxland", false, false, "path/to/file.json", null, null, null);
//        changeSet.setPreconditions(newSamplePreconditions());
//        changeSet.addChange(addColumnChange);
//        //when
//        String serializedJson = new JsonUnparser().serialize(changeSet, true);
//        //then
//        assertEquals("{\n" +
//                "  \"changeSet\": {\n" +
//                "    \"id\": \"1\",\n" +
//                "    \"author\": \"nvoxland\",\n" +
//                "    \"objectQuotingStrategy\": \"LEGACY\",\n" +
//                "    \"preconditions\": {\n" +
//                "      \"preConditions\": {\n" +
//                "        \"nestedPreconditions\": [\n" +
//                "          {\n" +
//                "            \"preConditions\": {\n" +
//                "              \"onError\": \"WARN\",\n" +
//                "              \"onFail\": \"CONTINUE\",\n" +
//                "              \"onSqlOutput\": \"TEST\"\n" +
//                "            }\n" +
//                "          }]\n" +
//                "        ,\n" +
//                "        \"onError\": \"CONTINUE\",\n" +
//                "        \"onFail\": \"MARK_RAN\",\n" +
//                "        \"onSqlOutput\": \"FAIL\"\n" +
//                "      }\n" +
//                "    },\n" +
//                "    \"changes\": [\n" +
//                "      {\n" +
//                "        \"addColumn\": {\n" +
//                "          \"catalogName\": \"cat\",\n" +
//                "          \"columns\": [\n" +
//                "            {\n" +
//                "              \"column\": {\n" +
//                "                \"defaultValueNumeric\": 3,\n" +
//                "                \"name\": \"col1\"\n" +
//                "              }\n" +
//                "            },\n" +
//                "            {\n" +
//                "              \"column\": {\n" +
//                "                \"defaultValueComputed\": \"NOW()\",\n" +
//                "                \"name\": \"col2\"\n" +
//                "              }\n" +
//                "            },\n" +
//                "            {\n" +
//                "              \"column\": {\n" +
//                "                \"defaultValueBoolean\": true,\n" +
//                "                \"name\": \"col3\"\n" +
//                "              }\n" +
//                "            },\n" +
//                "            {\n" +
//                "              \"column\": {\n" +
//                "                \"defaultValueDate\": \"1970-01-01T00:00:00\",\n" +
//                "                \"name\": \"col2\"\n" +
//                "              }\n" +
//                "            },\n" +
//                "            {\n" +
//                "              \"column\": {\n" +
//                "                \"defaultValueSequenceNext\": \"seq_me\",\n" +
//                "                \"name\": \"col2\"\n" +
//                "              }\n" +
//                "            }]\n" +
//                "          \n" +
//                "        }\n" +
//                "      }]\n" +
//                "    \n" +
//                "  }\n" +
//                "}\n", serializedJson);
//    }
//
//    private PreconditionContainer newSamplePreconditions() {
//        PreconditionContainer precondition = new PreconditionContainer();
//        precondition.setOnError(ErrorOption.CONTINUE);
//        precondition.setOnFail(FailOption.MARK_RAN);
//        precondition.setOnSqlOutput(OnSqlOutputOption.FAIL);
//        PreconditionContainer nestedPrecondition = new PreconditionContainer();
//        nestedPrecondition.setOnError(ErrorOption.WARN);
//        nestedPrecondition.setOnFail(FailOption.CONTINUE);
//        nestedPrecondition.setOnSqlOutput(OnSqlOutputOption.TEST);
//        precondition.addNestedPrecondition(nestedPrecondition);
//        return precondition;
//    }

}
