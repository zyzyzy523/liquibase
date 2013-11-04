package liquibase.serializer.core.xml;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.serializer.LiquibaseSerializable;
import liquibase.serializer.SnapshotSerializer;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.util.StringUtils;
import liquibase.util.XMLUtil;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class XMLSnapshotSerializer implements SnapshotSerializer {

    public static final String XML_NAMESPACE = "http://www.liquibase.org/xml/ns/snapshot";
    private Document currentSnapshotFileDOM;

    public XMLSnapshotSerializer() {
        try {
            this.currentSnapshotFileDOM = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new UnexpectedLiquibaseException(e);
        }
    }

    @Override
    public String[] getValidFileExtensions() {
        return new String[]{"xml"};
    }


    @Override
    public String serialize(LiquibaseSerializable object, boolean pretty) {
        StringBuffer buffer = new StringBuffer();
        int indent = -1;
        if (pretty) {
            indent = 0;
        }
        nodeToStringBuffer(createNode(object), buffer, indent);
        return buffer.toString();
    }

    @Override
    public void write(DatabaseSnapshot snapshot, OutputStream out) throws IOException {
        //todo
    }

    public Element createNode(LiquibaseSerializable object) {
        Element node = currentSnapshotFileDOM.createElementNS("http://www.liquibase.org/xml/ns/snapshot", object.getSerializedObjectName());

        for (String field : object.getSerializableFields()) {
            setValueOnNode(node, field, object.getSerializableFieldValue(field), object.getSerializableFieldType(field));
        }

        return node;
    }

    private void setValueOnNode(Element node, String objectName, Object value, LiquibaseSerializable.SerializationType serializationType) {
        if (value == null) {
            return;
        }

        if (value instanceof Collection) {
            for (Object child : (Collection) value) {
                setValueOnNode(node, objectName, child, serializationType);
            }
        } else if (value instanceof Map) {
            for (Map.Entry entry : (Set<Map.Entry>) ((Map) value).entrySet()) {
                Element mapNode = currentSnapshotFileDOM.createElementNS(XML_NAMESPACE, objectName);

                String name;
                if (entry.getKey() instanceof Class) {
                    name = ((Class) entry.getKey()).getSimpleName().replace(".","_");
                } else {
                    name = entry.getKey().toString();
                }
                setValueOnNode(mapNode, name, entry.getValue(), serializationType);
            }
        } else if (value instanceof LiquibaseSerializable) {
            node.appendChild(createNode((LiquibaseSerializable) value));
        } else {
            if (serializationType.equals(LiquibaseSerializable.SerializationType.NESTED_OBJECT)) {
                node.appendChild(createNode(objectName, value.toString()));
            } else if (serializationType.equals(LiquibaseSerializable.SerializationType.DIRECT_VALUE)) {
                node.setTextContent(value.toString());
            } else {
                node.setAttribute(objectName, value.toString());
            }
        }
    }


    // create a XML node with nodeName and simple text content
    public Element createNode(String nodeName, String nodeContent) {
        Element element = currentSnapshotFileDOM.createElementNS(XML_NAMESPACE, nodeName);
        element.setTextContent(nodeContent);
        return element;
    }


    /*
     * Creates a {@link String} using the XML element representation of this
     * change
     *
     * @param node the {@link Element} associated to this change
     * @param buffer a {@link StringBuffer} object used to hold the {@link String}
     *               representation of the change
     */
    private void nodeToStringBuffer(Node node, StringBuffer buffer, int indent) {
        if (indent >= 0) {
            if (indent > 0) {
                buffer.append("\n");
            }
            buffer.append(StringUtils.repeat(" ", indent));
        }
        buffer.append("<").append(node.getNodeName());
        SortedMap<String, String> attributeMap = new TreeMap<String, String>();
        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            attributeMap.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        boolean firstAttribute = true;
        for (Map.Entry entry : attributeMap.entrySet()) {
            String value = (String) entry.getValue();
            if (value != null) {
                if (indent >= 0 && !firstAttribute && attributeMap.size() > 2) {
                    buffer.append("\n").append(StringUtils.repeat(" ", indent)).append("        ");
                } else {
                    buffer.append(" ");
                }
                buffer.append(entry.getKey()).append("=\"").append(value).append("\"");
                firstAttribute = false;
            }
        }
        String textContent = StringUtils.trimToEmpty(XMLUtil.getTextContent(node));
        buffer.append(">").append(textContent);

        boolean sawChildren = false;
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode instanceof Element) {
                int newIndent = indent;
                if (newIndent >= 0) {
                    newIndent += 4;
                }
                nodeToStringBuffer(childNode, buffer, newIndent);
                sawChildren = true;
            }
        }
        if (indent >= 0) {
            if (sawChildren) {
                buffer.append("\n").append(StringUtils.repeat(" ", indent));
            }
        }

        if (!sawChildren && textContent.equals("")) {
            buffer.replace(buffer.length()-1, buffer.length(), "/>");
        } else {
            buffer.append("</").append(node.getNodeName()).append(">");
        }
    }

}
