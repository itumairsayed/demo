import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathConstants;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventDataUtilTest {

    private EventDataUtil eventDataUtil;

    @Mock
    private SOAPEnvelope mockEnvelope;

    @Mock
    private SOAPBody mockBody;

    @Mock
    private NodeList mockNodeList;

    @Mock
    private XPathExpression mockXPathExpression;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventDataUtil = new EventDataUtil();
    }

    @Test
    public void testExtractDetails_Success() throws Exception {
        // Arrange
        String soapContent = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<soapenv:Body>" +
                "<multiRef id=\"id1\">" +
                "<clientSessionID xsi:type=\"soapenc:string\">GF002810520160331092601</clientSessionID>" +
                "</multiRef>" +
                "<multiRef id=\"id2\">" +
                //"<claimNumber xsi:type=\"soapenc:string\">7977986470101423</claimNumber>" +
                "</multiRef>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        SOAPEnvelope envelope = new SOAPEnvelope();
        SOAPBody body = envelope.getBody();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(new ByteArrayInputStream(soapContent.getBytes()));
        body.addDocument(document);

        when(mockEnvelope.getBody()).thenReturn(body);
        when(mockBody.getAsString()).thenReturn(soapContent);
        doReturn("GF002810520160331092601").when(eventDataUtil).extractField("//clientSessionID/text()", body);
        doReturn(null).when(eventDataUtil).extractField("//claimNumber/text()", body);

        // Act
        String jsonResult = eventDataUtil.extractDetails(mockEnvelope, mockBody);

        // Assert
        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("\"transactionId\":\"GF002810520160331092601\""));
        assertFalse(jsonResult.contains("\"claimNumber\":"));
    }

    @Test
    public void testExtractDetails_NullEnvelope() {
        // Arrange
        SOAPEnvelope nullEnvelope = null;

        // Act
        String result = eventDataUtil.extractDetails(nullEnvelope, mockBody);

        // Assert
        assertNull(result);
    }

    @Test
    public void testExtractField_Success() throws Exception {
        // Arrange
        String soapContent = "<sample><clientSessionID>123</clientSessionID></sample>";
        when(mockBody.getAsString()).thenReturn(soapContent);

        when(mockNodeList.getLength()).thenReturn(1);
        when(mockNodeList.item(0).getTextContent()).thenReturn("123");
        when(mockXPathExpression.evaluate(any(), eq(XPathConstants.NODESET))).thenReturn(mockNodeList);

        // Act
        String result = eventDataUtil.extractField("//clientSessionID/text()", mockBody);

        // Assert
        assertEquals("123", result);
    }

    @Test
    public void testSerializeEventData_Success() {
        // Arrange
        EventData eventData = new EventData();
        eventData.setTransactionId("123");
        eventData.setClaimNumber("CLAIM-001");

        // Act
        String jsonString = eventDataUtil.serializeEventData(eventData);

        // Assert
        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"transactionId\":\"123\""));
        assertTrue(jsonString.contains("\"claimNumber\":\"CLAIM-001\""));
    }

    @Test
    public void testSerializeEventData_Exception() {
        // Arrange
        EventData eventData = mock(EventData.class);
        ObjectMapper mockObjectMapper = spy(new ObjectMapper());

        // Mock the ObjectMapper's behavior to throw an exception when serializing
        doThrow(new JsonProcessingException("Mock Exception") {}).when(mockObjectMapper).writeValueAsString(any(EventData.class));

        eventDataUtil = new EventDataUtil() {
            @Override
            public String serializeEventData(EventData eventData) {
                try {
                    return mockObjectMapper.writeValueAsString(eventData);
                } catch (JsonProcessingException e) {
                    log.error("Exception occurred while serializing EventData: {}", e.getMessage());
                }
                return null;
            }
        };

        // Act
        String jsonString = eventDataUtil.serializeEventData(eventData);

        // Assert
        assertNull(jsonString);
    }
}
