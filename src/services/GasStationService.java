package services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import models.FuelInventory;
import models.FuelType;
import models.GasStation;

public class GasStationService {
    private static final Path DEFAULT_DATA_PATH = Path.of("data", "stations.json");

    private final Path dataPath;
    private final List<GasStation> stations = new ArrayList<>();

    public GasStationService() {
        this(DEFAULT_DATA_PATH);
    }

    public GasStationService(Path dataPath) {
        this.dataPath = dataPath;
    }

    public void loadStations() {
        stations.clear();
        try {
            if (!java.nio.file.Files.exists(dataPath)) {
                seedMockData();
                saveStations();
                return;
            }
            stations.addAll(StationJsonStore.load(dataPath));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load " + dataPath + ": " + e.getMessage());
            System.err.println("Using built-in sample data instead.");
            seedMockData();
            saveStations();
        }
    }

    public void saveStations() {
        try {
            StationJsonStore.save(dataPath, stations);
        } catch (IOException e) {
            System.err.println("Could not save stations to " + dataPath + ": " + e.getMessage());
        }
    }

    public void seedMockData() {
        stations.clear();

        GasStation s1 = new GasStation("ST001", "TotalEnergies Bole", "Bole", 12.0, 8.5, true);
        s1.addFuelInventory(FuelType.BENZENE, new FuelInventory(90.50, 500, true));
        s1.addFuelInventory(FuelType.REGULAR, new FuelInventory(85.00, 0, false));
        s1.addFuelInventory(FuelType.KEROSENE, new FuelInventory(75.00, 200, true));

        GasStation s2 = new GasStation("ST002", "NOC Megenagna", "Megenagna", 28.0, 15.0, true);
        s2.addFuelInventory(FuelType.BENZENE, new FuelInventory(90.50, 1200, true));
        s2.addFuelInventory(FuelType.REGULAR, new FuelInventory(85.00, 800, true));

        GasStation s3 = new GasStation("ST003", "TotalEnergies Megenagna", "Megenagna", 30.0, 14.0, false);
        s3.addFuelInventory(FuelType.BENZENE, new FuelInventory(91.00, 0, false));
        s3.addFuelInventory(FuelType.KEROSENE, new FuelInventory(74.50, 350, true));

        GasStation s4 = new GasStation("ST004", "NOC Bole", "Bole", 14.5, 9.0, true);
        s4.addFuelInventory(FuelType.REGULAR, new FuelInventory(84.50, 600, true));
        s4.addFuelInventory(FuelType.KEROSENE, new FuelInventory(76.00, 150, true));

        stations.add(s1);
        stations.add(s2);
        stations.add(s3);
        stations.add(s4);
    }

    public List<GasStation> searchFuel(String zone, FuelType fuelType) {
        List<GasStation> results = new ArrayList<>();

        for (GasStation station : stations) {
            if (station.getZone().equalsIgnoreCase(zone)
                    && station.hasLine()
                    && station.hasFuel(fuelType)) {
                results.add(station);
            }
        }
        return results;
    }

    public GasStation findStationById(String stationId) {
        for (GasStation station : stations) {
            if (station.getId().equalsIgnoreCase(stationId)) {
                return station;
            }
        }
        return null;
    }

    public boolean updateInventory(String stationId, FuelType fuelType, double quantity, boolean available) {
        GasStation station = findStationById(stationId);
        if (station == null) {
            return false;
        }

        FuelInventory fuel = station.getInventory().get(fuelType);
        if (fuel == null) {
            return false;
        }

        station.updateFuelStatus(fuelType, quantity, available);
        saveStations();
        return true;
    }
}
