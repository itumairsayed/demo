package com.test;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;



public class LoggingHandler extends BasicHandler {
    @Override
    public void invoke(MessageContext msgContext) {
        try {
            // Log request message
            Message requestMessage = msgContext.getRequestMessage();
            if (requestMessage != null) {
                SOAPEnvelope envelope = requestMessage.getSOAPEnvelope();
                String request = envelope.getAsString();
                System.out.println("Request by handler: " + extractDetails(envelope));
            } else {
                System.out.println("Request by handler: MessageContext request message is null.");
            }

            // Invoke next handler in the chain
            // No need to call super.invoke(msgContext)

            // Log response message
            Message responseMessage = msgContext.getResponseMessage();
            if (responseMessage != null) {
                SOAPEnvelope envelope = responseMessage.getSOAPEnvelope();
                String response = envelope.getAsString();
                System.out.println("Response by handler: " + extractDetails(envelope));
            } else {
                System.out.println("Response by handler: MessageContext response message is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception in LoggingHandler: " + e.getMessage());
        }
    }

    public Map<String, String> extractDetails(SOAPEnvelope envelope) {
        Map<String, String> details = new HashMap<>();
        try {
            if (envelope == null || envelope.getBody() == null) {
                return details;
            }

            SOAPBody body = (SOAPBody) envelope.getBody();
            if (body == null) {
                return details;
            }

            String bodyContent = body.getAsString();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(bodyContent.getBytes()));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            // Extract clientSessionID
            XPathExpression sessionIdExpression = xpath.compile("//clientSessionID/text()");
            NodeList sessionIdNodes = (NodeList) sessionIdExpression.evaluate(document, XPathConstants.NODESET);
            if (sessionIdNodes.getLength() > 0) {
                details.put("sessionId", sessionIdNodes.item(0).getTextContent());
            }

            // Extract claimNumber
            XPathExpression claimNumberExpression = xpath.compile("//claimNumber/text()");
            NodeList claimNumberNodes = (NodeList) claimNumberExpression.evaluate(document, XPathConstants.NODESET);
            if (claimNumberNodes.getLength() > 0) {
                details.put("claimNumber", claimNumberNodes.item(0).getTextContent());
            }

        } catch (Exception e) {
            // Handle or log the exception
            e.printStackTrace();
        }
        return details;
    }
}
