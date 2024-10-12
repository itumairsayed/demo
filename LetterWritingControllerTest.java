import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

class LetterWritingControllerTest {

    @Mock
    private LetterWritingService letterWritingService;

    @InjectMocks
    private LetterWritingController letterWritingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestLetter_Success() {
        // Given
        RequestLetterRequestDTO request = new RequestLetterRequestDTO();
        request.setLetterWritingDto(new LetterWritingDto());
        request.setApplicationContext("testContext");

        RequestLetterResponseDTO expectedResponse = new RequestLetterResponseDTO();
        when(letterWritingService.requestLetter(any(RequestLetterRequestDTO.class))).thenReturn(expectedResponse);

        // When
        RequestLetterResponseDTO response = letterWritingController.requestLetter(request);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    void testRequestLetter_BadRequest() {
        // Given
        RequestLetterRequestDTO invalidRequest = new RequestLetterRequestDTO();

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> letterWritingController.requestLetter(invalidRequest));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Bad Request. Invalid input. request:" + invalidRequest + " \"", exception.getMessage());
    }

    @Test
    void testRequestLetter_InternalServerError() {
        // Given
        RequestLetterRequestDTO request = new RequestLetterRequestDTO();
        request.setLetterWritingDto(new LetterWritingDto());
        request.setApplicationContext("testContext");

        doThrow(new RuntimeException("Service failure")).when(letterWritingService).requestLetter(any(RequestLetterRequestDTO.class));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> letterWritingController.requestLetter(request));

        assertEquals(INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("500 INTERNAL_SERVER_ERROR \"Internal Server Error in requestLetter. request: " + request + "\"", exception.getMessage());
    }
}
