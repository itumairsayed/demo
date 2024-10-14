import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestLetterResponseDTOTest {

    private RequestLetterResponseDTO responseDTO;

    @BeforeEach
    public void setup() {
        responseDTO = new RequestLetterResponseDTO();
    }

    @Test
    public void testSetAndGetRequestId() {
        responseDTO.setRequestId("REQ123");
        assertEquals("REQ123", responseDTO.getRequestId());
    }

    @Test
    public void testSetAndGetLetterName() {
        responseDTO.setLetterName("SampleLetter");
        assertEquals("SampleLetter", responseDTO.getLetterName());
    }

    @Test
    public void testSetAndGetStatus() {
        ServiceStatus status = ServiceStatus.SUCCESS; // Assuming ServiceStatus is an enum
        responseDTO.setStatus(status);
        assertEquals(ServiceStatus.SUCCESS, responseDTO.getStatus());
    }

    @Test
    public void testNoArgsConstructor() {
        RequestLetterResponseDTO emptyDto = new RequestLetterResponseDTO();
        assertNull(emptyDto.getRequestId());
        assertNull(emptyDto.getLetterName());
        assertNull(emptyDto.getStatus());
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        responseDTO.setRequestId("REQ123");
        responseDTO.setLetterName("SampleLetter");
        responseDTO.setStatus(ServiceStatus.SUCCESS);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(responseDTO);

        // Assert the JSON includes non-null fields
        assertEquals("{\"requestId\":\"REQ123\",\"letterName\":\"SampleLetter\",\"status\":\"SUCCESS\"}", jsonResult);
    }

    @Test
    public void testJsonSerializationWithNullValues() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(responseDTO);

        // Since all fields are null, the JSON should be an empty object
        assertEquals("{}", jsonResult);
    }
}
