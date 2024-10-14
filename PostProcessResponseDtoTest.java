import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostProcessResponseDtoTest {

    private PostProcessResponseDto responseDto;

    @BeforeEach
    public void setup() {
        responseDto = new PostProcessResponseDto();
    }

    @Test
    public void testSetAndGetMessageId() {
        responseDto.setMessageId("MSG123");
        assertEquals("MSG123", responseDto.getMessageId());
    }

    @Test
    public void testSetAndGetStatus() {
        ServiceStatus status = ServiceStatus.SUCCESS; // Assuming ServiceStatus is an enum
        responseDto.setStatus(status);
        assertEquals(ServiceStatus.SUCCESS, responseDto.getStatus());
    }

    @Test
    public void testNoArgsConstructor() {
        PostProcessResponseDto emptyDto = new PostProcessResponseDto();
        assertNull(emptyDto.getMessageId());
        assertNull(emptyDto.getStatus());
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        responseDto.setMessageId("MSG123");
        responseDto.setStatus(ServiceStatus.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(responseDto);

        // Assert the JSON includes both fields
        assertEquals("{\"messageId\":\"MSG123\",\"status\":\"SUCCESS\"}", jsonResult);
    }

    @Test
    public void testJsonSerializationWithNullValues() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(responseDto);

        // Since both fields are null, the JSON should be an empty object
        assertEquals("{}", jsonResult);
    }
}
