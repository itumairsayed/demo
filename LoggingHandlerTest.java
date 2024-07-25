package com.test.demo;

import com.test.LoggingHandler;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.message.SOAPEnvelope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoggingHandlerTest {

    @InjectMocks
    private LoggingHandler loggingHandler;

    @Mock
    private MessageContext msgContext;

    @Mock
    private Message requestMessage;

    @Mock
    private Message responseMessage;

    @Mock
    private SOAPEnvelope requestEnvelope;

    @Mock
    private SOAPEnvelope responseEnvelope;

    @Before
    public void setUp() {
        // No need to initialize mocks, Mockito will handle it with @Mock annotation
    }

    @Test
    public void testInvoke_withRequestAndResponseMessages() throws Exception {
        when(msgContext.getRequestMessage()).thenReturn(requestMessage);
        when(requestMessage.getSOAPEnvelope()).thenReturn(requestEnvelope);
        when(requestEnvelope.toString()).thenReturn("Request SOAP Content");

        when(msgContext.getResponseMessage()).thenReturn(responseMessage);
        when(responseMessage.getSOAPEnvelope()).thenReturn(responseEnvelope);
        when(responseEnvelope.toString()).thenReturn("Response SOAP Content");

        loggingHandler.invoke(msgContext);

        verify(msgContext, times(1)).getRequestMessage();
        verify(msgContext, times(1)).getResponseMessage();
        verify(requestMessage, times(1)).getSOAPEnvelope();
        verify(responseMessage, times(1)).getSOAPEnvelope();
    }

    @Test
    public void testInvoke_withNullRequestMessage() throws Exception {
        when(msgContext.getRequestMessage()).thenReturn(null);
        when(msgContext.getResponseMessage()).thenReturn(responseMessage);
        when(responseMessage.getSOAPEnvelope()).thenReturn(responseEnvelope);
        when(responseEnvelope.toString()).thenReturn("Response SOAP Content");

        loggingHandler.invoke(msgContext);

        verify(msgContext, times(1)).getRequestMessage();
        verify(msgContext, times(1)).getResponseMessage();
        verify(responseMessage, times(1)).getSOAPEnvelope();
    }

    @Test
    public void testInvoke_withNullResponseMessage() throws Exception {
        when(msgContext.getRequestMessage()).thenReturn(requestMessage);
        when(requestMessage.getSOAPEnvelope()).thenReturn(requestEnvelope);
        when(requestEnvelope.toString()).thenReturn("Request SOAP Content");

        when(msgContext.getResponseMessage()).thenReturn(null);

        loggingHandler.invoke(msgContext);

        verify(msgContext, times(1)).getRequestMessage();
        verify(msgContext, times(1)).getResponseMessage();
        verify(requestMessage, times(1)).getSOAPEnvelope();
    }

    @Test
    public void testInvoke_withNullRequestAndResponseMessages() throws Exception {
        when(msgContext.getRequestMessage()).thenReturn(null);
        when(msgContext.getResponseMessage()).thenReturn(null);

        loggingHandler.invoke(msgContext);

        verify(msgContext, times(1)).getRequestMessage();
        verify(msgContext, times(1)).getResponseMessage();
    }

    @Test
    public void testInvoke_withException() throws Exception {
        when(msgContext.getRequestMessage()).thenReturn(requestMessage);
        when(requestMessage.getSOAPEnvelope()).thenThrow(new RuntimeException("Test Exception"));

        loggingHandler.invoke(msgContext);

        verify(msgContext, times(1)).getRequestMessage();
        verify(requestMessage, times(1)).getSOAPEnvelope();
    }
}
