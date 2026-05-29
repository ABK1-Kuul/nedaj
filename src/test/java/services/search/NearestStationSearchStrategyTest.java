package services.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.FuelInventory;
import models.FuelType;
import models.GasStation;
import services.GasStationService;

class NearestStationSearchStrategyTest {

    private GasStationService service;
    private NearestStationSearchStrategy strategy;

    @BeforeEach
    void setUp() {
        service = new GasStationService();
        service.seedMockData();
        strategy = new NearestStationSearchStrategy();
    }

    @Test
    void search_requiresDriverCoordinates() {
        SearchCriteria criteria = new SearchCriteria("Bole", FuelType.BENZENE);

        assertThrows(IllegalArgumentException.class,
                () -> strategy.search(service.searchFuel("Bole", FuelType.BENZENE), criteria));
    }

    @Test
    void search_sortsByDistanceAscending() {
        GasStation near = new GasStation("NEAR", "Near", "TestZone", 1.0, 1.0, true);
        near.addFuelInventory(FuelType.BENZENE, new FuelInventory(100, 50, true));

        GasStation far = new GasStation("FAR", "Far", "TestZone", 10.0, 10.0, true);
        far.addFuelInventory(FuelType.BENZENE, new FuelInventory(100, 50, true));

        List<GasStation> stations = List.of(far, near);
        SearchCriteria criteria = new SearchCriteria("TestZone", FuelType.BENZENE, 0.0, 0.0);

        List<GasStation> results = strategy.search(stations, criteria);

        assertEquals(2, results.size());
        assertEquals("NEAR", results.get(0).getId());
        assertEquals("FAR", results.get(1).getId());
    }
}
