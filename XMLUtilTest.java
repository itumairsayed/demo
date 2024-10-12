import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import java.io.StringReader;

class XMLUtilTest {

    @Test
    void testParseXml() throws Exception {
        // Arrange
        String xmlFile = "src/test/resources/test.xml"; // provide a valid test XML file path

        // Act
        Document doc = XMLUtil.parseXml(xmlFile);

        // Assert
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getNodeName());
    }

    @Test
    void testParseXmlString() throws Exception {
        // Arrange
        String xmlString = "<root><child>Test</child></root>";

        // Act
        Document doc = XMLUtil.parseXmlString(xmlString);

        // Assert
        assertNotNull(doc);
        assertEquals("root", doc.getDocumentElement().getNodeName());
    }

    @Test
    void testFindNodeValue_WithValidNode() throws Exception {
        // Arrange
        String xmlString = "<root><child>Test</child></root>";
        Document doc = XMLUtil.parseXmlString(xmlString);
        Node rootNode = doc.getDocumentElement();

        // Act
        String result = XMLUtil.findNodeValue(rootNode, "child");

        // Assert
        assertEquals("Test", result);
    }

    @Test
    void testFindNodeValue_WithNoMatchingNode() throws Exception {
        // Arrange
        String xmlString = "<root><child>Test</child></root>";
        Document doc = XMLUtil.parseXmlString(xmlString);
        Node rootNode = doc.getDocumentElement();

        // Act
        String result = XMLUtil.findNodeValue(rootNode, "nonExistentNode");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAttributeValue_ValidAttribute() {
        // Arrange
        Node mockNode = mock(Node.class);
        NamedNodeMap mockAttributes = mock(NamedNodeMap.class);
        Node mockAttribute = mock(Node.class);

        when(mockNode.getAttributes()).thenReturn(mockAttributes);
        when(mockAttributes.getLength()).thenReturn(1);
        when(mockAttributes.item(0)).thenReturn(mockAttribute);
        when(mockAttribute.getNodeName()).thenReturn("attr");
        when(mockAttribute.getNodeValue()).thenReturn("value");

        // Act
        String result = XMLUtil.getAttributeValue(mockNode, "attr");

        // Assert
        assertEquals("value", result);
    }

    @Test
    void testGetAttributeValue_NoMatchingAttribute() {
        // Arrange
        Node mockNode = mock(Node.class);
        NamedNodeMap mockAttributes = mock(NamedNodeMap.class);
        Node mockAttribute = mock(Node.class);

        when(mockNode.getAttributes()).thenReturn(mockAttributes);
        when(mockAttributes.getLength()).thenReturn(1);
        when(mockAttributes.item(0)).thenReturn(mockAttribute);
        when(mockAttribute.getNodeName()).thenReturn("nonMatchingAttr");

        // Act
        String result = XMLUtil.getAttributeValue(mockNode, "attr");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetEndChildValue() {
        // Arrange
        Node mockNode = mock(Node.class);
        NodeList mockNodeList = mock(NodeList.class);
        Node mockTextNode = mock(Node.class);

        when(mockNode.getChildNodes()).thenReturn(mockNodeList);
        when(mockNodeList.getLength()).thenReturn(1);
        when(mockNodeList.item(0)).thenReturn(mockTextNode);
        when(mockTextNode.getNodeName()).thenReturn("#text");
        when(mockTextNode.getNodeValue()).thenReturn("EndValue");

        // Act
        String result = XMLUtil.getEndChildValue(mockNode);

        // Assert
        assertEquals("EndValue", result);
    }

    @Test
    void testGetEndChildValue_EmptyNodeValue() {
        // Arrange
        Node mockNode = mock(Node.class);
        NodeList mockNodeList = mock(NodeList.class);
        Node mockTextNode = mock(Node.class);

        when(mockNode.getChildNodes()).thenReturn(mockNodeList);
        when(mockNodeList.getLength()).thenReturn(1);
        when(mockNodeList.item(0)).thenReturn(mockTextNode);
        when(mockTextNode.getNodeName()).thenReturn("#text");
        when(mockTextNode.getNodeValue()).thenReturn("");

        // Act
        String result = XMLUtil.getEndChildValue(mockNode);

        // Assert
        assertNull(result);
    }
}
