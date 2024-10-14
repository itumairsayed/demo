import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VariableDTOTest {

    private VariableDTO variableDTO;

    @BeforeEach
    public void setup() {
        variableDTO = new VariableDTO();
    }

    @Test
    public void testSetAndGetName() {
        variableDTO.setName("variableName");
        assertEquals("variableName", variableDTO.getName());
    }

    @Test
    public void testSetAndGetValue() {
        variableDTO.setValue("variableValue");
        assertEquals("variableValue", variableDTO.getValue());
    }

    @Test
    public void testSetAndGetType() {
        Type type = Type.STRING; // Assuming Type is an enum
        variableDTO.setType(type);
        assertEquals(Type.STRING, variableDTO.getType());
    }

    @Test
    public void testNoArgsConstructor() {
        VariableDTO emptyDto = new VariableDTO();
        assertNull(emptyDto.getName());
        assertNull(emptyDto.getValue());
        assertNull(emptyDto.getType());
    }
}
