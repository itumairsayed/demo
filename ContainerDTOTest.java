import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ContainerDTOTest {

    private ContainerDTO containerDTO;
    private List<VariableDTO> variableListMock;

    @BeforeEach
    public void setup() {
        variableListMock = mock(List.class);  // Mocking the list of VariableDTOs
        containerDTO = new ContainerDTO("TestContainer", variableListMock);
    }

    @Test
    public void testGetName() {
        assertEquals("TestContainer", containerDTO.getName());
    }

    @Test
    public void testSetName() {
        containerDTO.setName("NewName");
        assertEquals("NewName", containerDTO.getName());
    }

    @Test
    public void testGetVariables() {
        assertEquals(variableListMock, containerDTO.getVariables());
    }

    @Test
    public void testSetVariables() {
        List<VariableDTO> newVariableList = new ArrayList<>();
        containerDTO.setVariables(newVariableList);
        assertEquals(newVariableList, containerDTO.getVariables());
    }
}
