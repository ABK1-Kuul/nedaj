package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import models.FuelType;
import models.GasStation;
import services.search.CheapestFuelSearchStrategy;
import services.search.NearestStationSearchStrategy;
import services.search.SearchCriteria;
import services.search.ZoneSearchStrategy;

class GasStationServiceTest {

    @TempDir
    Path tempDir;

    private GasStationService service;

    @BeforeEach
    void setUp() {
        service = new GasStationService(tempDir.resolve("stations.json"));
        service.seedMockData();
    }

    @Test
    void findStationById_isCaseInsensitive() {
        assertNotNull(service.findStationById("st001"));
        assertEquals("ST001", service.findStationById("st001").getId());
    }

    @Test
    void findStationById_returnsNullForUnknownId() {
        assertNull(service.findStationById("UNKNOWN"));
    }

    @Test
    void searchFuel_filtersByZoneLineAndStock() {
        List<GasStation> results = service.searchFuel("Bole", FuelType.BENZENE);

        assertEquals(1, results.size());
        assertEquals("ST001", results.get(0).getId());
    }

    @Test
    void search_withCheapestStrategy_ordersByPrice() {
        SearchCriteria criteria = new SearchCriteria("Megenagna", FuelType.BENZENE);
        List<GasStation> results = service.search(criteria, new CheapestFuelSearchStrategy());

        assertEquals(1, results.size());
        assertEquals("ST002", results.get(0).getId());
    }

    @Test
    void search_withNearestStrategy_ordersByDistance() {
        SearchCriteria criteria = new SearchCriteria("Bole", FuelType.BENZENE, 12.0, 8.5);
        List<GasStation> results = service.search(criteria, new NearestStationSearchStrategy());

        assertEquals(1, results.size());
        assertEquals("ST001", results.get(0).getId());
    }

    @Test
    void updateInventory_persistsChanges() throws Exception {
        Path dataFile = tempDir.resolve("stations.json");

        assertTrue(service.updateInventory("ST001", FuelType.BENZENE, 999, true));
        service.loadStations();

        GasStation reloaded = service.findStationById("ST001");
        assertEquals(999, reloaded.getInventory().get(FuelType.BENZENE).getQuantityLiters());
        assertTrue(Files.exists(dataFile));
    }

    @Test
    void updateInventory_returnsFalseForInvalidStationOrFuel() {
        assertFalse(service.updateInventory("NOPE", FuelType.BENZENE, 100, true));
        assertFalse(service.updateInventory("ST004", FuelType.BENZENE, 100, true));
    }

    @Test
    void loadStations_seedsFileWhenMissing() throws Exception {
        Path missingFile = tempDir.resolve("missing.json");
        GasStationService fresh = new GasStationService(missingFile);

        fresh.loadStations();

        assertTrue(Files.exists(missingFile));
        assertNotNull(fresh.findStationById("ST001"));
    }

    @Test
    void search_withZoneStrategy_matchesSearchFuel() {
        SearchCriteria criteria = new SearchCriteria("Bole", FuelType.KEROSENE);
        List<GasStation> viaStrategy = service.search(criteria, new ZoneSearchStrategy());
        List<GasStation> viaHelper = service.searchFuel("Bole", FuelType.KEROSENE);

        assertEquals(viaHelper.size(), viaStrategy.size());
        assertEquals(viaHelper.get(0).getId(), viaStrategy.get(0).getId());
    }
}
