import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpression;

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
        when(mockEnvelope.getBody()).thenReturn(mockBody);
        when(mockBody.getAsString()).thenReturn("<sample><clientSessionID>123</clientSessionID><claimNumber>CLAIM-001</claimNumber></sample>");
        
        when(mockBody.getChildNodes().item(0).getLocalName()).thenReturn("uploadDocument");
        
        doReturn("123").when(eventDataUtil).extractField("//clientSessionID/text()", mockBody);
        doReturn("CLAIM-001").when(eventDataUtil).extractField("//claimNumber/text()", mockBody);
        // add more stubbing for other fields
        
        // Act
        String jsonResult = eventDataUtil.extractDetails(mockEnvelope, mockBody);

        // Assert
        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("\"transactionId\":\"123\""));
        assertTrue(jsonResult.contains("\"claimNumber\":\"CLAIM-001\""));
        // add more assertions as needed
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
        when(mockBody.getAsString()).thenReturn("<sample><clientSessionID>123</clientSessionID></sample>");
        
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
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        eventDataUtil = spy(new EventDataUtil());
        doReturn(mockObjectMapper).when(eventDataUtil).getObjectMapper();
        doThrow(JsonProcessingException.class).when(mockObjectMapper).writeValueAsString(any(EventData.class));

        // Act
        String jsonString = eventDataUtil.serializeEventData(eventData);

        // Assert
        assertNull(jsonString);
    }
}
