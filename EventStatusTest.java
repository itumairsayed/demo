import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventStatusTest {

    @Test
    public void testEventStatusValues() {
        // Verify that the string values match the expected ones
        assertEquals("Success", EventStatus.SUCCESS.getValue());
        assertEquals("Partial Success", EventStatus.PARTIAL_SUCCESS.getValue());
        assertEquals("Failed", EventStatus.FAILED.getValue());
        assertEquals("Pending", EventStatus.PENDING.getValue());
    }

    @Test
    public void testEnumCount() {
        // Verify that there are exactly 4 enum values
        EventStatus[] statuses = EventStatus.values();
        assertEquals(4, statuses.length);
    }

    @Test
    public void testEnumNames() {
        // Verify the enum names are correctly defined
        assertEquals("SUCCESS", EventStatus.SUCCESS.name());
        assertEquals("PARTIAL_SUCCESS", EventStatus.PARTIAL_SUCCESS.name());
        assertEquals("FAILED", EventStatus.FAILED.name());
        assertEquals("PENDING", EventStatus.PENDING.name());
    }
}
