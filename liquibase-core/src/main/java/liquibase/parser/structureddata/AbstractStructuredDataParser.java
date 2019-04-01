package liquibase.parser.structureddata;

import liquibase.Scope;
import liquibase.exception.DependencyException;
import liquibase.exception.ParseException;
import liquibase.parser.AbstractParser;
import liquibase.parser.ParsedNode;
import liquibase.parser.structureddata.mapping.ParsedNodeMappingFactory;
import liquibase.parser.structureddata.postprocessor.MappingPostprocessor;
import liquibase.parser.structureddata.postprocessor.MappingPostprocessorFactory;
import liquibase.parser.structureddata.preprocessor.ParsedNodePreprocessor;
import liquibase.parser.structureddata.preprocessor.ParsedNodePreprocessorFactory;
import liquibase.util.StringUtil;

/**
 * Base class for parser that use a {@link liquibase.parser.ParsedNode} intermediate tree.
 */
public abstract class AbstractStructuredDataParser extends AbstractParser {

    /**
     * Parse the given path into a {@link ParsedNode}
     */
    protected abstract ParsedNode parseToParsedNode(String relativeTo, String path, Class objectType) throws ParseException;

    /**
     * Outputs a pseudo version of the original version of the given parsedNode.
     * Used for error messages and other times Liquibase needs to direct users to the original version of a parsed node,
     * especially when there are no original line numbers stored.
     */
    public abstract String describeOriginal(ParsedNode parsedNode);

    /**
     * Converts the file at sourcePath to the passed objectType using the configured {@link ParsedNodePreprocessor}(s), {@link liquibase.parser.structureddata.mapping.ParsedNodeMapping} and {@link liquibase.parser.structureddata.postprocessor.MappingPostprocessor}(s).
     * If an exception is thrown, a more descriptive message will be constructed in the resulting {@link ParseException}
     */
    @Override
    public <ObjectType> ObjectType parse(String relativeTo, String sourcePath, Class<ObjectType> objectType) throws ParseException {
        try {

            ParsedNode rootNode = this.parseToParsedNode(relativeTo, sourcePath, objectType);

            return parse(rootNode, objectType);
        } catch (ParseException e) {
            String message = e.getMessage();
            ParsedNode problemNode = e.getProblemNode();

            String parseErrorMessage = "Error parsing ";

            if (problemNode != null && problemNode.getOriginalName() != null) {
                parseErrorMessage += "\"" + problemNode.getOriginalName() + "\" in ";
            }

            if (problemNode == null || problemNode.fileName == null) {
                parseErrorMessage += sourcePath;
            } else {
                parseErrorMessage += problemNode.fileName;
            }

            if (problemNode != null && problemNode.lineNumber != null) {
                parseErrorMessage += " line " + problemNode.lineNumber;
                if (problemNode.columnNumber != null) {
                    parseErrorMessage = parseErrorMessage + ", column " + problemNode.columnNumber;
                }
            }

            String near = null;
            if (problemNode != null) {
                near = this.describeOriginal(problemNode);
            }

            if (near == null) {
                message = parseErrorMessage + " " + message;
            } else {
                message = parseErrorMessage + "\n" + StringUtil.indent(near + "\n\n" + message);
            }


            if (problemNode != null) {
                Scope.getCurrentScope().getLog(getClass()).fine("Error parsing:\n" + StringUtil.indent(problemNode.prettyPrint()));
            }

            throw new ParseException(message, e, problemNode);
        }

    }

    public <ObjectType> ObjectType parse(ParsedNode parsedNode, Class<ObjectType> objectType) throws ParseException {
        try {
            Scope scope = Scope.getCurrentScope();
            for (ParsedNodePreprocessor preprocessor : scope.getSingleton(ParsedNodePreprocessorFactory.class).getPreprocessors()) {
//                MDC.put(LogUtil.MDC_PREPROCESSOR, preprocessor.getClass().getName());
//                try {
                preprocessor.process(parsedNode);
//                } finally {
//                    MDC.remove(LogUtil.MDC_PREPROCESSOR);
//                }
            }

            ObjectType returnObject = scope.getSingleton(ParsedNodeMappingFactory.class).toObject(parsedNode, objectType, null, null);

            for (MappingPostprocessor postprocessor : scope.getSingleton(MappingPostprocessorFactory.class).getPostprocessors()) {
                postprocessor.process(returnObject);
            }

            return returnObject;
        } catch (DependencyException e) {
            throw new ParseException(e, null);
        }
    }
}
