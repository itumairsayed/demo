import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FilterTypeTest {

    private FilterType filterType;
    private FilterType.FilterMonth filterMonth;

    @BeforeEach
    public void setup() {
        filterType = new FilterType();
        filterMonth = new FilterType.FilterMonth();
    }

    // Testing FilterType class
    @Test
    public void testSetAndGetLetterName() {
        filterType.setLetterName("SampleLetter");
        assertEquals("SampleLetter", filterType.getLetterName());
    }

    @Test
    public void testSetAndGetFilterMonths() {
        List<FilterType.FilterMonth> filterMonths = new ArrayList<>();
        filterMonths.add(new FilterType.FilterMonth());
        filterType.setFilterMonths(filterMonths);
        assertEquals(filterMonths, filterType.getFilterMonths());
    }

    @Test
    public void testNoArgsConstructorForFilterType() {
        FilterType emptyFilterType = new FilterType();
        assertNull(emptyFilterType.getLetterName());
        assertNull(emptyFilterType.getFilterMonths());
    }

    // Testing static inner class FilterMonth
    @Test
    public void testSetAndGetMonth() {
        filterMonth.setMonth("January");
        assertEquals("January", filterMonth.getMonth());
    }

    @Test
    public void testSetAndGetDate() {
        filterMonth.setDate("2024-01-01");
        assertEquals("2024-01-01", filterMonth.getDate());
    }

    @Test
    public void testSetAndGetMachineName() {
        filterMonth.setMachineName("MachineA");
        assertEquals("MachineA", filterMonth.getMachineName());
    }

    @Test
    public void testSetAndGetRiskStates() {
        filterMonth.setRiskStates("High");
        assertEquals("High", filterMonth.getRiskStates());
    }

    @Test
    public void testSetAndGetLossStates() {
        filterMonth.setLossStates("Low");
        assertEquals("Low", filterMonth.getLossStates());
    }

    @Test
    public void testNoArgsConstructorForFilterMonth() {
        FilterType.FilterMonth emptyFilterMonth = new FilterType.FilterMonth();
        assertNull(emptyFilterMonth.getMonth());
        assertNull(emptyFilterMonth.getDate());
        assertNull(emptyFilterMonth.getMachineName());
        assertNull(emptyFilterMonth.getRiskStates());
        assertNull(emptyFilterMonth.getLossStates());
    }
}
