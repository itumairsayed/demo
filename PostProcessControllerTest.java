import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostProcessControllerTest {

    @Mock
    private LetterWritingService letterWritingService;

    @InjectMocks
    private PostProcessController postProcessController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnPostProcessMessage_Success() throws Exception {
        String message = "test message";
        PostProcessResponseDto expectedResponse = new PostProcessResponseDto();
        ServiceStatus successStatus = new ServiceStatus(false, "Success", "POST_PROCESS_SERVICE_ID");
        expectedResponse.setStatus(successStatus);

        // Mock the service response
        PostProcessResponseDto atlasResponse = new PostProcessResponseDto();
        when(letterWritingService.postProcess(message)).thenReturn(atlasResponse);

        // Call the controller method
        ResponseEntity<PostProcessResponseDto> responseEntity = postProcessController.onPostProcessMessage(message);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Success", responseEntity.getBody().getStatus().getMessage());
        
        // Verify that the service was called once
        verify(letterWritingService, times(1)).postProcess(message);
    }

    @Test
    public void testOnPostProcessMessage_Failure() throws Exception {
        String message = "test message";
        String errorMessage = "Processing error";
        PostProcessResponseDto expectedResponse = new PostProcessResponseDto();
        ServiceStatus errorStatus = new ServiceStatus(true, errorMessage, "POST_PROCESS_SERVICE_ID");
        expectedResponse.setStatus(errorStatus);

        // Mock the service to throw an exception
        when(letterWritingService.postProcess(message)).thenThrow(new RuntimeException(errorMessage));

        // Call the controller method
        ResponseEntity<PostProcessResponseDto> responseEntity = postProcessController.onPostProcessMessage(message);

        // Assert the response
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(errorMessage, responseEntity.getBody().getStatus().getMessage());

        // Verify that the service was called once
        verify(letterWritingService, times(1)).postProcess(message);
    }
}
