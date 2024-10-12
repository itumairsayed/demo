import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class XsltTransformerTest {

    @InjectMocks
    private XsltTransformer xsltTransformer;

    @Mock
    private Resource eloqXsltTemplateResource;

    @Mock
    private TransformerFactory transformerFactory;

    @Mock
    private Transformer transformer;

    @Mock
    private InputStream inputStream;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock the InputStream from Resource
        when(eloqXsltTemplateResource.getInputStream()).thenReturn(inputStream);

        // Mock TransformerFactory to return a mock Transformer
        when(transformerFactory.newTransformer(any(StreamSource.class))).thenReturn(transformer);
    }

    @Test
    public void testTransformSuccess() throws Exception {
        // Mock successful transformation
        doNothing().when(transformer).transform(any(StreamSource.class), any(StreamResult.class));

        // Input string
        String inputString = "<test>input</test>";

        // Call the transform method
        String result = xsltTransformer.transform(inputString);

        // Verify the transform method was called once
        verify(transformer).transform(any(StreamSource.class), any(StreamResult.class));

        // Assert the transformation result (assuming successful transformation returns the same content)
        assertEquals("", result); // Empty since we're not testing actual XSLT logic
    }

    @Test
    public void testTransformerConfigurationException() throws Exception {
        // Mock TransformerFactory to throw TransformerConfigurationException
        when(transformerFactory.newTransformer(any(StreamSource.class)))
                .thenThrow(new TransformerConfigurationException("Mock Transformer Configuration Exception"));

        // Assert that TransformerException is thrown
        TransformerException exception = assertThrows(TransformerException.class, () -> {
            xsltTransformer.transform("<test>input</test>");
        });

        // Verify the exception message
        assertEquals("TransformerConfigurationException in creating new transformer with mXsltTemplateStreamSource.", exception.getMessage());
    }

    @Test
    public void testTransformerException() throws Exception {
        // Mock Transformer to throw TransformerException
        doThrow(new TransformerException("Mock Transformer Exception"))
                .when(transformer).transform(any(StreamSource.class), any(StreamResult.class));

        // Assert that DataConversionException is thrown
        DataConversionException exception = assertThrows(DataConversionException.class, () -> {
            xsltTransformer.transform("<test>input</test>");
        });

        // Verify the exception message
        assertEquals("TransformerException occurred in .transform method.", exception.getMessage());
    }

    @Test
    public void testGenericException() throws Exception {
        // Mock eloqXsltTemplateResource to throw a generic Exception
        when(eloqXsltTemplateResource.getInputStream()).thenThrow(new Exception("Mock Generic Exception"));

        // Assert that TransformerException is thrown
        TransformerException exception = assertThrows(TransformerException.class, () -> {
            xsltTransformer.transform("<test>input</test>");
        });

        // Verify the exception message
        assertEquals("Generic Exception caught with message: Mock Generic Exception", exception.getMessage());
    }
}
