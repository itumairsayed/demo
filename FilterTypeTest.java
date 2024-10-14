import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FilterTypeTest {

    private FilterType filterType;

    @BeforeEach
    public void setup() {
        filterType = new FilterType();
    }

    @Test
    public void testSetAndGetLetterName() {
        filterType.setLetterName("SampleLetter");
        assertEquals("SampleLetter", filterType.getLetterName());
    }

    @Test
    public void testSetAndGetFilterMonths() {
        List<FilterMonth> filterMonths = new ArrayList<>();
        filterMonths.add(new FilterMonth());  // Assuming FilterMonth is a class
        filterType.setFilterMonths(filterMonths);
        assertEquals(filterMonths, filterType.getFilterMonths());
    }

    @Test
    public void testNoArgsConstructor() {
        FilterType emptyFilterType = new FilterType();
        assertNull(emptyFilterType.getLetterName());
        assertNull(emptyFilterType.getFilterMonths());
    }
}
