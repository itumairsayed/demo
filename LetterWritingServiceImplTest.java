import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LetterWritingServiceImplTest {

    @InjectMocks
    private LetterWritingServiceImpl letterWritingService;

    @Mock
    private LetterWritingMapper letterWritingMapper;

    @Mock
    private PostProcessMapper postProcessMapper;

    @Mock
    private AtlasClient atlasClient;

    @Mock
    private XsltTransformer xsltTransformer;

    @Test
    void testRequestLetter_WhenFilterRequestIsTrue_ShouldProcessRequest() {
        // Given
        RequestLetterRequestDTO requestDto = new RequestLetterRequestDTO();
        LetterWritingDto letterWritingDto = new LetterWritingDto();
        letterWritingDto.setLetterName("Test Letter");
        requestDto.setLetterWritingDto(letterWritingDto);
        requestDto.setApplicationContext(new ApplicationContext());

        CEOutputRequestDto ceOutputRequestDto = new CEOutputRequestDto();
        String xmlString = "<xml></xml>";
        String transformedXml = "<transformedXml></transformedXml>";
        
        when(letterWritingMapper.filterRequest(requestDto)).thenReturn(true);
        when(letterWritingMapper.mapRequestLetterDTOToCEOutputRequestDto(requestDto)).thenReturn(ceOutputRequestDto);
        when(xsltTransformer.transform(xmlString)).thenReturn(transformedXml);
        when(XmlUtility.toXML(ceOutputRequestDto, true, any(JDomDriver.class))).thenReturn(xmlString);

        RequestLetterResponseDTO responseDTO = letterWritingService.requestLetter(requestDto);

        // Then
        assertNotNull(responseDTO);
        assertEquals(requestDto.getRequestId(), responseDTO.getRequestId());
        assertEquals(requestDto.getLetterWritingDto().getLetterName(), responseDTO.getLetterName());
        assertEquals("Success", responseDTO.getStatus().getCode());
    }

    @Test
    void testRequestLetter_WhenFilterRequestIsFalse_ShouldSkipProcessing() {
        // Given
        RequestLetterRequestDTO requestDto = new RequestLetterRequestDTO();
        LetterWritingDto letterWritingDto = new LetterWritingDto();
        letterWritingDto.setLetterName("Test Letter");
        requestDto.setLetterWritingDto(letterWritingDto);

        when(letterWritingMapper.filterRequest(requestDto)).thenReturn(false);

        // When
        RequestLetterResponseDTO responseDTO = letterWritingService.requestLetter(requestDto);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Success", responseDTO.getStatus().getCode());
        assertEquals("Letter request caught by filter; treating as a success", responseDTO.getStatus().getMessage());
    }

    @Test
    void testRequestLetter_ExceptionOccurs_ShouldSetErrorStatus() {
        // Given
        RequestLetterRequestDTO requestDto = new RequestLetterRequestDTO();
        LetterWritingDto letterWritingDto = new LetterWritingDto();
        letterWritingDto.setLetterName("Test Letter");
        requestDto.setLetterWritingDto(letterWritingDto);

        when(letterWritingMapper.filterRequest(requestDto)).thenThrow(new RuntimeException("Test Exception"));

        // When
        RequestLetterResponseDTO responseDTO = letterWritingService.requestLetter(requestDto);

        // Then
        assertNotNull(responseDTO);
        assertEquals("Error", responseDTO.getStatus().getCode());
        assertEquals("Test Exception", responseDTO.getStatus().getMessage());
    }

    @Test
    void testPostProcess_SupportedClaimHandling_ShouldProcessRequest() throws Exception {
        // Given
        String incomingMessage = "<xml><ClaimHandlingSystem>P</ClaimHandlingSystem><DocumentId>123</DocumentId></xml>";
        org.w3c.dom.Document document = mock(org.w3c.dom.Document.class);
        Node rootNode = mock(Node.class);
        PostProcessingRequest atlasRequest = new PostProcessingRequest();
        PostProcessingResponse atlasResponse = new PostProcessingResponse();
        PostProcessResponseDto postProcessResponseDto = new PostProcessResponseDto();

        when(XMLUtil.parseXmlString(incomingMessage)).thenReturn(document);
        when(document.getFirstChild()).thenReturn(rootNode);
        when(XMLUtil.findNodeValue(rootNode, "ClaimHandlingSystem")).thenReturn("P");
        when(XMLUtil.findNodeValue(rootNode, "DocumentId")).thenReturn("123");
        when(postProcessMapper.buildAtlasRequest(any(), any())).thenReturn(atlasRequest);
        when(atlasClient.startPostProcessing(atlasRequest)).thenReturn(atlasResponse);
        when(postProcessMapper.buildPostProcessResponse(atlasResponse)).thenReturn(postProcessResponseDto);

        // When
        PostProcessResponseDto responseDto = letterWritingService.postProcess(incomingMessage);

        // Then
        assertNotNull(responseDto);
    }

    @Test
    void testPostProcess_UnsupportedClaimHandling_ShouldThrowException() {
        // Given
        String incomingMessage = "<xml><ClaimHandlingSystem>Unsupported</ClaimHandlingSystem></xml>";
        org.w3c.dom.Document document = mock(org.w3c.dom.Document.class);
        Node rootNode = mock(Node.class);

        when(XMLUtil.parseXmlString(incomingMessage)).thenReturn(document);
        when(document.getFirstChild()).thenReturn(rootNode);
        when(XMLUtil.findNodeValue(rootNode, "ClaimHandlingSystem")).thenReturn("Unsupported");

        // Then
        assertThrows(LetterWritingServiceException.class, () -> {
            // When
            letterWritingService.postProcess(incomingMessage);
        });
    }

    @Test
    void testPublishMsgToQWithDAPR_Success() throws Exception {
        // Given
        RequestLetterResponseDTO responseDTO = new RequestLetterResponseDTO();
        DaprClient mockDaprClient = mock(DaprClient.class);

        try (MockedStatic<DaprClientBuilder> mockedStatic = mockStatic(DaprClientBuilder.class)) {
            DaprClientBuilder builder = mock(DaprClientBuilder.class);
            when(builder.build()).thenReturn(mockDaprClient);
            mockedStatic.when(DaprClientBuilder::new).thenReturn(builder);

            // When
            letterWritingService.publishMsgToQWithDAPR("payload", responseDTO);
        }

        // Then
        assertEquals("Success", responseDTO.getStatus().getCode());
    }

    @Test
    void testPublishMsgToQWithDAPR_Exception_ShouldSetErrorStatus() throws Exception {
        // Given
        RequestLetterResponseDTO responseDTO = new RequestLetterResponseDTO();
        DaprClient mockDaprClient = mock(DaprClient.class);

        try (MockedStatic<DaprClientBuilder> mockedStatic = mockStatic(DaprClientBuilder.class)) {
            DaprClientBuilder builder = mock(DaprClientBuilder.class);
            when(builder.build()).thenReturn(mockDaprClient);
            mockedStatic.when(DaprClientBuilder::new).thenReturn(builder);

            doThrow(new RuntimeException("Test Exception")).when(mockDaprClient).publishEvent(anyString(), anyString(), anyString());

            // When
            letterWritingService.publishMsgToQWithDAPR("payload", responseDTO);
        }

        // Then
        assertEquals("Error", responseDTO.getStatus().getCode());
        assertEquals("Test Exception", responseDTO.getStatus().getMessage());
    }
}
