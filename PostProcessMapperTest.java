import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
class PostProcessMapperTest {

    @InjectMocks
    private PostProcessMapper postProcessMapper;

    @Mock
    private ClaimsApplicationContext applicationContext;

    @Mock
    private Document document;

    @Mock
    private Node letterWritingMessageRootNode;

    @Mock
    private NodeList childNodes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuildAtlasRequest() throws Exception {
        // Setup mock application context
        when(applicationContext.getClientSessionID()).thenReturn("12345");

        // Setup mock document and child nodes
        when(document.getFirstChild()).thenReturn(letterWritingMessageRootNode);
        when(letterWritingMessageRootNode.getChildNodes()).thenReturn(childNodes);
        when(childNodes.getLength()).thenReturn(0); // No children in this case

        // Test the method
        PostProcessingRequest request = postProcessMapper.buildAtlasRequest(applicationContext, document);

        // Assertions
        assertNotNull(request);
        assertNotNull(request.getPostProcessingDTO());
        assertEquals("12345", request.getPostProcessingDTO().getRequestID());
        assertEquals(0, request.getPostProcessingDTO().getNumCopiesSent());

        verify(applicationContext, times(1)).getClientSessionID();
        verify(document, times(1)).getFirstChild();
    }

    @Test
    void testBuildPostProcessResponse_Success() {
        // Mock PostProcessingResponse
        PostProcessingResponse response = mock(PostProcessingResponse.class);
        com.geico.claims.lws.cds.letterwriting.atlas.client.dto.ServiceStatus serviceStatus =
            new com.geico.claims.lws.cds.letterwriting.atlas.client.dto.ServiceStatus();
        serviceStatus.setCode("200");
        serviceStatus.setMessage("Success");
        com.geico.claims.lws.cds.letterwriting.atlas.client.dto.ServiceStatus[] serviceStatuses = 
            new com.geico.claims.lws.cds.letterwriting.atlas.client.dto.ServiceStatus[] {serviceStatus};
        
        when(response.getServiceStatus()).thenReturn(serviceStatuses);

        // Test the method
        PostProcessResponseDto result = postProcessMapper.buildPostProcessResponse(response);

        // Assertions
        assertNotNull(result);
        assertNotNull(result.getStatus());
        assertEquals("200", result.getStatus().getCode());
        assertEquals("Success", result.getStatus().getMessage());
    }

    @Test
    void testGetDateFromString_ValidDate() {
        String dateStr = "2023-10-15";
        Date date = postProcessMapper.getDateFromString(dateStr, "yyyy-MM-dd");
        assertNotNull(date);
    }

    @Test
    void testGetDateFromString_InvalidDate() {
        String dateStr = "invalid-date";
        Date date = postProcessMapper.getDateFromString(dateStr, "yyyy-MM-dd");
        assertNull(date);
    }

    @Test
    void testProcessChildNodes_NoChildren() {
        PostProcessingDTO postProcessingDTO = new PostProcessingDTO();
        when(letterWritingMessageRootNode.getChildNodes()).thenReturn(childNodes);
        when(childNodes.getLength()).thenReturn(0);

        postProcessMapper.processChildNodes(letterWritingMessageRootNode, postProcessingDTO);

        // No children, so nothing should be processed
        verify(letterWritingMessageRootNode, times(1)).getChildNodes();
    }
}
