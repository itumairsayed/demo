import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LetterWritingHelperTest {

    @Test
    public void testGetResourceAsStringWithFileName() throws Exception {
        String fileName = "testFile.txt";
        String expectedContent = "Test content";
        Resource resourceMock = mock(ClassPathResource.class);
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes());

        when(resourceMock.getInputStream()).thenReturn(inputStream);

        // Use mock resource
        LetterWritingHelper letterWritingHelper = spy(LetterWritingHelper.class);
        doReturn(resourceMock).when(letterWritingHelper).getResourceAsString(fileName);

        String result = letterWritingHelper.getResourceAsString(fileName);

        assertEquals(expectedContent, result);
        verify(resourceMock).getInputStream();
    }

    @Test
    public void testGetResourceAsStringWithResource() throws Exception {
        String expectedContent = "Test content";
        Resource resourceMock = mock(Resource.class);
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes());

        when(resourceMock.getInputStream()).thenReturn(inputStream);

        String result = LetterWritingHelper.getResourceAsString(resourceMock);

        assertEquals(expectedContent, result);
        verify(resourceMock).getInputStream();
    }

    @Test
    public void testCreateServiceStatusWithError() {
        ServiceStatus status = LetterWritingHelper.createServiceStatus(true, "Error occurred");

        assertEquals("Letter Writing Service Execution Failed with exception: {}Error occurred", status.getMessage());
        assertEquals("PROCESS_ERROR_CODE", status.getCode()); // Ensure to define this constant in your class
        assertEquals("REQUEST_AUTOMATED_LETTER_SERVICE_ID", status.getServiceID());
    }

    @Test
    public void testCreateServiceStatusWithoutError() {
        ServiceStatus status = LetterWritingHelper.createServiceStatus(false, "Success");

        assertEquals("Success", status.getMessage());
        assertEquals("SUCCESS_CODE", status.getCode()); // Ensure to define this constant in your class
        assertEquals("REQUEST_AUTOMATED_LETTER_SERVICE_ID", status.getServiceID());
    }

    @Test
    public void testCreateServiceStatusWithCustomServiceId() {
        ServiceStatus status = LetterWritingHelper.createServiceStatus(true, "Error occurred", "CUSTOM_SERVICE_ID");

        assertEquals("Execution Failed with exception: {}Error occurred", status.getMessage());
        assertEquals("PROCESS_ERROR_CODE", status.getCode()); // Ensure to define this constant in your class
        assertEquals("CUSTOM_SERVICE_ID", status.getServiceID());
    }

    @Test
    public void testBuildClaimsContext() {
        ClaimsApplicationContext context = LetterWritingHelper.buildClaimsContext("session123", "user456", "app789");

        assertEquals("app789", context.getClientApplicationName());
        assertEquals("OS", context.getClientApplicationOS());
        assertEquals("user456", context.getUserID());
        assertEquals("Plaza", context.getUserRegion());
        assertEquals("session123", context.getClientSessionID());
    }

    @Test
    public void testConcatenateStringsWithSeparator() {
        String result = LetterWritingHelper.concatenateStringsWithSeparator(
                Arrays.asList("A", "B", "C"), ",");

        assertEquals("A,B,C", result);
    }

    @Test
    public void testConcatenateStringsWithSeparatorNullInput() {
        String result = LetterWritingHelper.concatenateStringsWithSeparator(null, ",");

        assertEquals("", result);
    }

    @Test
    public void testReturnEmptyStringIfNull() {
        assertEquals("", LetterWritingHelper.returnEmptyStringIfNull(null));
        assertEquals("Non-null", LetterWritingHelper.returnEmptyStringIfNull("Non-null"));
    }
}
