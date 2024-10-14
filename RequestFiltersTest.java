import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestFiltersTest {

    private RequestFilters requestFilters;

    @BeforeEach
    public void setup() {
        requestFilters = new RequestFilters();
    }

    @Test
    public void testSetAndGetCl200() {
        Map<String, Object> cl200Map = new HashMap<>();
        cl200Map.put("key1", "value1");
        requestFilters.setCl200(cl200Map);
        assertEquals(cl200Map, requestFilters.getCl200());
    }

    @Test
    public void testSetAndGetCl50() {
        Map<String, Object> cl50Map = new HashMap<>();
        cl50Map.put("key2", "value2");
        requestFilters.setCl50(cl50Map);
        assertEquals(cl50Map, requestFilters.getCl50());
    }

    @Test
    public void testSetAndGetCl207() {
        Map<String, Object> cl207Map = new HashMap<>();
        cl207Map.put("key3", "value3");
        requestFilters.setCl207(cl207Map);
        assertEquals(cl207Map, requestFilters.getCl207());
    }

    @Test
    public void testSetAndGetCl209() {
        Map<String, Object> cl209Map = new HashMap<>();
        cl209Map.put("key4", "value4");
        requestFilters.setCl209(cl209Map);
        assertEquals(cl209Map, requestFilters.getCl209());
    }

    @Test
    public void testSetAndGetCl210() {
        Map<String, Object> cl210Map = new HashMap<>();
        cl210Map.put("key5", "value5");
        requestFilters.setCl210(cl210Map);
        assertEquals(cl210Map, requestFilters.getCl210());
    }

    @Test
    public void testSetAndGetNjAppeal() {
        Map<String, Object> njAppealMap = new HashMap<>();
        njAppealMap.put("key6", "value6");
        requestFilters.setNjAppeal(njAppealMap);
        assertEquals(njAppealMap, requestFilters.getNjAppeal());
    }

    @Test
    public void testSetAndGetDearPatient() {
        Map<String, Object> dearPatientMap = new HashMap<>();
        dearPatientMap.put("key7", "value7");
        requestFilters.setDearPatient(dearPatientMap);
        assertEquals(dearPatientMap, requestFilters.getDearPatient());
    }

    @Test
    public void testSetAndGetClLOU() {
        Map<String, Object> clLOUMap = new HashMap<>();
        clLOUMap.put("key8", "value8");
        requestFilters.setClLOU(clLOUMap);
        assertEquals(clLOUMap, requestFilters.getClLOU());
    }

    @Test
    public void testNoArgsConstructor() {
        RequestFilters emptyFilters = new RequestFilters();
        assertNull(emptyFilters.getCl200());
        assertNull(emptyFilters.getCl50());
        assertNull(emptyFilters.getCl207());
        assertNull(emptyFilters.getCl209());
        assertNull(emptyFilters.getCl210());
        assertNull(emptyFilters.getNjAppeal());
        assertNull(emptyFilters.getDearPatient());
        assertNull(emptyFilters.getClLOU());
    }
}
