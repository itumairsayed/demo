import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EventNameTest {

    @Test
    public void testGetCode() {
        assertEquals("NDUR", EventName.UPLOAD_NEW_REQUEST.getCode());
        assertEquals("RTDR", EventName.RETRIEVE_DOCUMENTS.getCode());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Upload Request received for upload", EventName.UPLOAD_NEW_REQUEST.getDescription());
        assertEquals("Retrieve Documents", EventName.RETRIEVE_DOCUMENTS.getDescription());
    }

    @Test
    public void testGetSequence() {
        assertEquals(1, EventName.UPLOAD_NEW_REQUEST.getSequence());
        assertEquals(7, EventName.RETRIEVE_DOCUMENTS.getSequence());
    }

    @Test
    public void testGetEventNameByValue_Code() {
        Optional<EventName> result = EventName.getEventNameByValue("NDUR");
        assertTrue(result.isPresent());
        assertEquals(EventName.UPLOAD_NEW_REQUEST, result.get());

        result = EventName.getEventNameByValue("RTDR");
        assertTrue(result.isPresent());
        assertEquals(EventName.RETRIEVE_DOCUMENTS, result.get());
    }

    @Test
    public void testGetEventNameByValue_Description() {
        Optional<EventName> result = EventName.getEventNameByValue("Upload Request received for upload");
        assertTrue(result.isPresent());
        assertEquals(EventName.UPLOAD_NEW_REQUEST, result.get());

        result = EventName.getEventNameByValue("Retrieve Documents");
        assertTrue(result.isPresent());
        assertEquals(EventName.RETRIEVE_DOCUMENTS, result.get());
    }

    @Test
    public void testGetEventNameByValue_Invalid() {
        Optional<EventName> result = EventName.getEventNameByValue("INVALID_CODE");
        assertFalse(result.isPresent());

        result = EventName.getEventNameByValue("Some invalid description");
        assertFalse(result.isPresent());
    }

    @Test
    public void testEnumProperties() {
        EventName[] values = EventName.values();
        assertEquals(9, values.length);

        // Ensure each enum has the correct properties
        assertEquals("NDUR", EventName.UPLOAD_NEW_REQUEST.getCode());
        assertEquals("Upload Request received for upload", EventName.UPLOAD_NEW_REQUEST.getDescription());
        assertEquals(1, EventName.UPLOAD_NEW_REQUEST.getSequence());
        
        // Similar assertions for another enum value
        assertEquals("RTDR", EventName.RETRIEVE_DOCUMENTS.getCode());
        assertEquals("Retrieve Documents", EventName.RETRIEVE_DOCUMENTS.getDescription());
        assertEquals(7, EventName.RETRIEVE_DOCUMENTS.getSequence());
    }
}
