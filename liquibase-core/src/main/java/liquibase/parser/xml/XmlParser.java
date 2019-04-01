package liquibase.parser.xml;

import liquibase.Scope;
import liquibase.exception.ParseException;
import liquibase.logging.Logger;
import liquibase.parser.ParsedNode;
import liquibase.parser.structureddata.AbstractStructuredDataParser;
import liquibase.util.StringUtil;
import org.xml.sax.*;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Standard parser for XML files. Expects files to have a .xml extension.
 * Find entities using {@link XmlEntityResolver} before looking for them on the network.
 */
public class XmlParser extends AbstractStructuredDataParser {

    public static final String LIQUIBASE_SCHEMA_VERSION = "3.6";

    private SAXParserFactory saxParserFactory;

    public XmlParser() {
        saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(true);
        saxParserFactory.setNamespaceAware(true);
    }

    public static String getSchemaVersion() {
        return LIQUIBASE_SCHEMA_VERSION;
    }

    @Override
    public int getPriority(String relativeTo, String path, Class objectType) {
        if (path.toLowerCase().endsWith(".xml")) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    public ParsedNode parseToParsedNode(String relativeTo, String path, Class objectType) throws ParseException {
        try {
            SAXParser parser = saxParserFactory.newSAXParser();
            try {
                parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
                //ok, parser must not support it
            }

            XMLReader xmlReader = parser.getXMLReader();
            XmlParserEntityResolver resolver = new XmlParserEntityResolver();
            xmlReader.setEntityResolver(resolver);


            try (InputStream inputStream = Scope.getCurrentScope().getResourceAccessor().openStream(relativeTo, path)) {

                if (inputStream == null) {
                    throw new ParseException("Could not find file to parse: " + path + (relativeTo != null ? " relative to " + relativeTo : ""), null);
                }

                XmlParserSaxHandler handler = new XmlParserSaxHandler(path);
                xmlReader.setContentHandler(handler);

                xmlReader.setErrorHandler(new ErrorHandler() {
                    @Override
                    public void warning(SAXParseException exception) throws SAXException {
                        Scope.getCurrentScope().getLog(getClass()).warning(exception.getMessage());
                        throw exception;
                    }

                    @Override
                    public void error(SAXParseException exception) throws SAXException {
                        Scope.getCurrentScope().getLog(getClass()).severe(exception.getMessage());
                        throw exception;
                    }

                    @Override
                    public void fatalError(SAXParseException exception) throws SAXException {
                        Scope.getCurrentScope().getLog(getClass()).severe(exception.getMessage());
                        throw exception;
                    }
                });

                xmlReader.parse(new InputSource(inputStream));

                ParsedNode rootNode = handler.getRootNode();

                rootNode.addChild("physicalPath").setValue(path);
                return rootNode;
            }
        } catch (ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ParseException(e, null);
        }
    }

    /**
     * Default implementation calls {@link XmlUnparser#describeOriginal(ParsedNode)}
     */
    @Override
    public String describeOriginal(ParsedNode parsedNode) {
        return new XmlUnparser().describeOriginal(parsedNode);
    }

    /**
     * {@link EntityResolver2} that finds local copies of XSD files before falling back to the network.
     */
    protected static class XmlParserEntityResolver implements EntityResolver2 {

        public XmlParserEntityResolver() {

        }

        @Override
        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
            Logger log = Scope.getCurrentScope().getLog(getClass());
            log.fine("Resolving XML entity name='" + name + "', publicId='" + publicId + "', baseURI='" + baseURI + "', systemId='" + systemId + "'");

            if (systemId == null) {
                log.fine("Unable to resolve XML entity locally. Will load from network.");
                return null;
            }

            XmlEntityResolver resolver = Scope.getCurrentScope().getSingleton(XmlEntityResolverFactory.class).getResolver(name, publicId, baseURI, systemId);
            if (resolver == null) {
                log.fine("No resolver configured for " + systemId + ". Will load from network");
                return null;
            }

            InputSource resolved;
            try {
                resolved = resolver.resolveEntity(name, publicId, baseURI, systemId);
            } catch (ParseException e) {
                throw new SAXException(e);
            }

            if (resolved == null) {
                Scope.getCurrentScope().getLog(getClass()).fine("Unable to resolve XML entity locally. Will load from network.");
            }
            return resolved;
        }

        @Override
        public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
            return null;
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return resolveEntity(null, publicId, null, systemId);
        }

    }

    protected static class XmlParserSaxHandler extends DefaultHandler {

        private final String fileName;
        private Deque<ParsedNode> nodeQueue = new ArrayDeque<>();
        private StringBuilder text = null;
        private ParsedNode rootNode;
        private Locator locator;

        public XmlParserSaxHandler(String path) {
            this.fileName = path;
        }

        public ParsedNode getRootNode() {
            return rootNode;
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            text.append(new String(ch, start, length));
        }

        @Override
        public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
            ParsedNode parentNode = null;
            if (!nodeQueue.isEmpty()) {
                parentNode = nodeQueue.peek();
            }

            ParsedNode node;
            if (parentNode == null) {
                node = ParsedNode.createRootNode(localName);
            } else {
                node = parentNode.addChild(localName);
            }
            node.fileName = fileName;
            if (locator != null) {
                node.lineNumber = locator.getLineNumber();
                node.columnNumber = locator.getColumnNumber();
            }

            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    ParsedNode child = node.addChild(attributes.getLocalName(i)).setValue(StringUtil.trimToNull(attributes.getValue(i)));
                    node.fileName = fileName;
                    if (locator != null) {
                        child.lineNumber = locator.getLineNumber();
                        child.columnNumber = locator.getColumnNumber();
                    }
                    child.addMarker(ParsedNode.Marker.isAttribute);

                }
            }

            nodeQueue.push(node);

            text = new StringBuilder();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            ParsedNode node = nodeQueue.pop();

            String seenText = this.text.toString();
            if (StringUtil.trimToNull(seenText) != null) {
                node.setValue(seenText.trim());
            }
            text = new StringBuilder();

            if (nodeQueue.isEmpty()) {
                this.rootNode = node;
            }
        }
    }
}
