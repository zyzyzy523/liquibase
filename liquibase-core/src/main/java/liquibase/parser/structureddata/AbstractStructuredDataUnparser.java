package liquibase.parser.structureddata;

import liquibase.Scope;
import liquibase.exception.DependencyException;
import liquibase.exception.ParseException;
import liquibase.parser.AbstractUnparser;
import liquibase.parser.ParsedNode;
import liquibase.parser.Unparser;
import liquibase.parser.UnparserFactory;
import liquibase.parser.structureddata.mapping.ParsedNodeMappingFactory;
import liquibase.parser.structureddata.unprocessor.ParsedNodeUnprocessor;
import liquibase.parser.structureddata.unprocessor.ParsedNodeUnprocessorFactory;
import liquibase.util.StringUtil;

import java.io.OutputStream;

/**
 * Unparsers take a {@link ParsedNode} and convert it into a file.
 * They should not do any processing or fixing of the parsedNode, just output something that directly matches the node structure.
 *
 * @see AbstractUnparser
 * @see UnparserFactory
 */
public abstract class AbstractStructuredDataUnparser extends AbstractUnparser {

    /**
     * Outputs the passed node to the output stream.
     */
    abstract protected void unparse(ParsedNode node, OutputStream output) throws ParseException;

    /**
     * Converts the object to the given outputStream using the configured @link liquibase.parser.structureddata.mapping.ParsedNodeMapping}, {@link ParsedNodeUnprocessor}(s) and {@link Unparser}.
     * The passed outputPath is used to determine the unparser to use, so it must be set, even if writing to stdout or somewhere else without a real path.
     * <p/>
     * Does nothing if object is null.
     *
     * @throws ParseException
     */
    public void unparse(Object object, String outputPath, OutputStream outputStream) throws ParseException {
        if (object == null) {
            return;
        }

        if (outputPath == null) {
            throw new ParseException("No outputPath set", null);
        }
        if (outputStream == null) {
            throw new ParseException("No outputStream set", null);
        }
        try {
            Scope scope = Scope.getCurrentScope();
            ParsedNode parsedObject = scope.getSingleton(ParsedNodeMappingFactory.class).toParsedNode(object, null, null, null);

            for (ParsedNodeUnprocessor unprocessor : scope.getSingleton(ParsedNodeUnprocessorFactory.class).getUnprocessors()) {
                unprocessor.unprocess(parsedObject);
            }

            try {
                this.unparse(parsedObject, outputStream);
            } catch (ParseException e) {
                String message = e.getMessage();
                ParsedNode problemNode = e.getProblemNode();

                String parseErrorMessage = "Error unparsing ";

                if (problemNode != null && problemNode.getOriginalName() != null) {
                    parseErrorMessage += "\"" + problemNode.getOriginalName() + "\" in ";
                }

                if (problemNode == null || problemNode.fileName == null) {
                    parseErrorMessage += outputPath;
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
        } catch (DependencyException e) {
            throw new ParseException(e, null);
        }

    }

    /**
     * Outputs a pseudo version of the original version of the given parsedNode.
     * Used for error messages and other times Liquibase needs to direct users to the original version of a parsed node,
     * especially when there are no original line numbers stored.
     */
    /**
     * Default implementation lists node name, value, and children names in a generic format.
     */
    public String describeOriginal(ParsedNode parsedNode) {
        String returnString = "";
        ParsedNode nodeToPrint = parsedNode;

        if (nodeToPrint != null) {
            returnString += "near " + nodeToPrint.getOriginalName();
            if (nodeToPrint.getValue() != null) {
                returnString += "=\"" + nodeToPrint.getValue() + "\"";
            }
            if (nodeToPrint.getChildren().size() > 0) {
                returnString += " children: " + StringUtil.join(nodeToPrint.getChildren(), ", ", new StringUtil.StringUtilFormatter<ParsedNode>() {
                    @Override
                    public String toString(ParsedNode obj) {
                        return obj.getName();
                    }
                });
            }
        }

        while (nodeToPrint != null) {
            returnString += "\n" + StringUtil.indent("in " + nodeToPrint.getOriginalName());
            nodeToPrint = nodeToPrint.getOriginalParent();
        }

        return returnString;
    }}
