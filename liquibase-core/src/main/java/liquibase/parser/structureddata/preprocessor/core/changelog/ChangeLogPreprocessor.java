package liquibase.parser.structureddata.preprocessor.core.changelog;

import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.structureddata.preprocessor.AbstractParsedNodePreprocessor;

/**
 * {@link liquibase.changelog.ChangeLog} related preprocessing.
 */
public class ChangeLogPreprocessor extends AbstractParsedNodePreprocessor {

    private ParsedNode.ParsedNodeFilter legacyQuotingStrategyNodeFilter = new ParsedNode.ParsedNodeFilter() {
        @Override
        public boolean matches(ParsedNode node) {
            return (node.getName().equals("quotingStrategy") || node.getName().equals("objectQuotingStrategy"))
                    && node.getValue("", String.class).equalsIgnoreCase("legacy");
        }
    };

    /**
     * Remove the now-unused legacy quoting strategy.
     */
    @Override
    public void process(ParsedNode node) throws ParseException {
        node.removeChildren("schemaLocation", false);
        node.removeChildren(legacyQuotingStrategyNodeFilter, false);
        for (ParsedNode quotingStrategy : node.getChildren(legacyQuotingStrategyNodeFilter, true)) {
            quotingStrategy.remove();
        }

        ParsedNode items = node.getChild("items", true);
        for (String childName : new String[] {"changeSet", "preconditions"}) {
            for (ParsedNode changeSetNode : node.getChildren(childName, false)) {
                changeSetNode.moveTo(items);
            }
        }
    }
}
