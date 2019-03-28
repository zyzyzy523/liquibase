package liquibase.parser.preprocessor.core.changelog;

import liquibase.Scope;
import liquibase.change.ChangeFactory;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.preprocessor.AbstractParsedNodePreprocessor;
import liquibase.parser.preprocessor.ParsedNodePreprocessor;
import liquibase.util.CollectionUtil;

import java.util.List;
import java.util.SortedSet;

/**
 * {@link liquibase.changelog.ChangeSet} related preprocessing.
 */
public class ChangeSetPreprocessor extends AbstractParsedNodePreprocessor {

    @Override
    public Class<? extends ParsedNodePreprocessor>[] mustBeAfter() {
        return CollectionUtil.union(Class.class, super.mustBeAfter(), ChangeLogPreprocessor.class);
    }

    /**
     * Moves all nodes that match a change name to the "changes" collection.
     */
    @Override
    public void process(ParsedNode node) throws ParseException {
        List<ParsedNode> changeSets = node.getChild("changeLogEntries", true).getChildren("changeSet", false);

        SortedSet<String> allChanges = Scope.getCurrentScope().getSingleton(ChangeFactory.class).getDefinedChanges();

        for (ParsedNode changeSet : changeSets) {
            ParsedNode changes = changeSet.getChild("changes", true);

            for (ParsedNode possibleAction : changeSet.getChildren()) {
                if (allChanges.contains(possibleAction.getName())) {
                    possibleAction.moveTo(changes);
                }
            }
        }
    }
}
