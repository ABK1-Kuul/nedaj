package services.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import models.FuelType;

class SearchCriteriaTest {

    @Test
    void constructor_trimsZone() {
        SearchCriteria criteria = new SearchCriteria("  Bole  ", FuelType.BENZENE);
        assertEquals("Bole", criteria.getZone());
    }

    @Test
    void constructor_rejectsBlankZone() {
        assertThrows(IllegalArgumentException.class,
                () -> new SearchCriteria("   ", FuelType.BENZENE));
    }

    @Test
    void constructor_rejectsNullZone() {
        assertThrows(NullPointerException.class,
                () -> new SearchCriteria(null, FuelType.BENZENE));
    }

    @Test
    void twoArgConstructor_hasNoDriverLocation() {
        SearchCriteria criteria = new SearchCriteria("Bole", FuelType.BENZENE);
        assertFalse(criteria.hasDriverLocation());
    }

    @Test
    void fourArgConstructor_setsDriverLocation() {
        SearchCriteria criteria = new SearchCriteria("Bole", FuelType.BENZENE, 1.0, 2.0);
        assertTrue(criteria.hasDriverLocation());
        assertEquals(1.0, criteria.getDriverX());
        assertEquals(2.0, criteria.getDriverY());
    }
}
