package com.test.demo;

import com.test.LoggingHandler;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoggingHandlerTestExtractDetails {

    @InjectMocks
    private LoggingHandler loggingHandler;

    @Mock
    private SOAPEnvelope requestEnvelope;

    @Mock
    private SOAPBody requestBody;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // Any teardown logic if needed
    }

    @Test
    public void testExtractDetails_withValidEnvelope() throws Exception {
        // Arrange
        String soapContent = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                             "<soapenv:Body>" +
                             "<multiRef id=\"id1\">" +
                             "<clientSessionID xsi:type=\"soapenc:string\">GF002810520160331092601</clientSessionID>" +
                             "</multiRef>" +
                             "<multiRef id=\"id2\">" +
                             "<claimNumber xsi:type=\"soapenc:string\">7977986470101423</claimNumber>" +
                             "</multiRef>" +
                             "</soapenv:Body>" +
                             "</soapenv:Envelope>";

        // Mocking
        when(requestEnvelope.getBody()).thenReturn(requestBody);
        when(requestBody.getAsString()).thenReturn(soapContent);

        // Act
        Map<String, String> details = loggingHandler.extractDetails(requestEnvelope);

        // Assert
        assertNotNull(details);
        assertEquals("GF002810520160331092601", details.get("sessionId"));
        assertEquals("7977986470101423", details.get("claimNumber"));
    }

    @Test
    public void testExtractDetails_withNullEnvelope() throws Exception {
        // Act
        Map<String, String> details = loggingHandler.extractDetails(null);

        // Assert
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }

    @Test
    public void testExtractDetails_withNullBody() throws Exception {
        // Arrange
        when(requestEnvelope.getBody()).thenReturn(null);

        // Act
        Map<String, String> details = loggingHandler.extractDetails(requestEnvelope);

        // Assert
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }

    @Test
    public void testExtractDetails_withEmptyBodyElement() throws Exception {
        // Arrange
        when(requestEnvelope.getBody()).thenReturn(requestBody);
        when(requestBody.getAsString()).thenReturn("");

        // Act
        Map<String, String> details = loggingHandler.extractDetails(requestEnvelope);

        // Assert
        assertNotNull(details);
        assertTrue(details.isEmpty());
    }

    @Test
    public void testExtractDetails_withMalformedContent() throws Exception {
        // Arrange
        String malformedContent = "<soapenv:Envelope><soapenv:Body><clientSessionID>12345</clientSessionID><claimNumber>67890</claimNumber></soapenv:Body></soapenv:Envelope>";
        when(requestEnvelope.getBody()).thenReturn(requestBody);
        when(requestBody.getAsString()).thenReturn(malformedContent);

        // Act
        Map<String, String> details = loggingHandler.extractDetails(requestEnvelope);

        // Assert
        assertNotNull(details);
        assertEquals("12345", details.get("sessionId"));
        assertEquals("67890", details.get("claimNumber"));
    }
}
