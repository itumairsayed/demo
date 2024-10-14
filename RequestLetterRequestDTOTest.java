import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestLetterRequestDTOTest {

    private RequestLetterRequestDTO requestDTO;

    @BeforeEach
    public void setup() {
        requestDTO = new RequestLetterRequestDTO();
    }

    @Test
    public void testSetAndGetRequestId() {
        requestDTO.setRequestId("REQ123");
        assertEquals("REQ123", requestDTO.getRequestId());
    }

    @Test
    public void testSetAndGetLetterWritingDto() {
        LetterWritingDto letterWritingDto = new LetterWritingDto();
        requestDTO.setLetterWritingDto(letterWritingDto);
        assertEquals(letterWritingDto, requestDTO.getLetterWritingDto());
    }

    @Test
    public void testSetAndGetApplicationContext() {
        ClaimsApplicationContext applicationContext = new ClaimsApplicationContext();
        requestDTO.setApplicationContext(applicationContext);
        assertEquals(applicationContext, requestDTO.getApplicationContext());
    }

    @Test
    public void testSetAndGetStatus() {
        ServiceStatus status = ServiceStatus.PENDING; // Assuming ServiceStatus is an enum
        requestDTO.setStatus(status);
        assertEquals(ServiceStatus.PENDING, requestDTO.getStatus());
    }

    @Test
    public void testSetAndGetVariables() {
        List<Object> variables = new ArrayList<>();
        variables.add("var1");
        variables.add("var2");
        requestDTO.setVariables(variables);
        assertEquals(variables, requestDTO.getVariables());
    }

    @Test
    public void testNoArgsConstructor() {
        RequestLetterRequestDTO emptyDto = new RequestLetterRequestDTO();
        assertNull(emptyDto.getRequestId());
        assertNull(emptyDto.getLetterWritingDto());
        assertNull(emptyDto.getApplicationContext());
        assertNull(emptyDto.getStatus());
        assertNull(emptyDto.getVariables());
    }

    @Test
    public void testJsonSerialization() throws JsonProcessingException {
        requestDTO.setRequestId("REQ123");
        LetterWritingDto letterWritingDto = new LetterWritingDto();
        requestDTO.setLetterWritingDto(letterWritingDto);
        ServiceStatus status = ServiceStatus.SUCCESS;
        requestDTO.setStatus(status);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(requestDTO);

        // Assert the JSON includes the non-null fields
        assertEquals("{\"requestId\":\"REQ123\",\"letterWritingDto\":{},\"status\":\"SUCCESS\"}", jsonResult);
    }

    @Test
    public void testJsonSerializationWithNullValues() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(requestDTO);

        // Since all fields are null, the JSON should be an empty object
        assertEquals("{}", jsonResult);
    }
}
