package liquibase.parser.preprocessor.core.changelog;

import liquibase.Scope;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.preprocessor.AbstractParsedNodePreprocessor;
import liquibase.parser.preprocessor.ParsedNodePreprocessor;
import liquibase.precondition.PreconditionFactory;

import java.util.SortedSet;

/**
 * {@link liquibase.changelog.ChangeLog} related preprocessing.
 */
public class PreconditionsPreprocessor extends AbstractParsedNodePreprocessor {

    @Override
    public Class<? extends ParsedNodePreprocessor>[] mustBeBefore() {
        return new Class[] {
                ChangeLogPreprocessor.class
        };
    }

    @Override
    public void process(ParsedNode node) throws ParseException {
        //standardize capitalization
        node.renameChildren("preConditions", "preconditions");

        SortedSet<String> preconditionNames = Scope.getCurrentScope().getSingleton(PreconditionFactory.class).getNames();

        for (ParsedNode preconditionsNode : node.getChildren("preconditions", true)) {
            ParsedNode itemsNode = preconditionsNode.getChild("items", true);
            for (String preconditionName : preconditionNames) {
                preconditionsNode.moveChildren(preconditionName, itemsNode);
            }
        }
    }
}
