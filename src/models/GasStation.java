package models;

import java.util.HashMap;
import java.util.Map;

public class GasStation {
    private String id;
    private String name;
    private String zone;
    private double x;
    private double y;
    private Map<FuelType, FuelInventory> inventory;

    public GasStation(String id, String name, String zone, double x, double y) {
        this.id = id;
        this.name = name;
        this.zone = zone;
        this.x = x;
        this.y = y;
        this.inventory = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZone() {
        return zone;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Map<FuelType, FuelInventory> getInventory() {
        return inventory;
    }

    public void addFuelInventory(FuelType type, FuelInventory fuelInventory) {
        inventory.put(type, fuelInventory);
    }

    public void updateFuelStatus(FuelType type, double quantityLiters, boolean available) {
        FuelInventory fuel = inventory.get(type);
        if (fuel != null) {
            fuel.setQuantityLiters(quantityLiters);
            fuel.setAvailable(available);
        }
    }

    public boolean hasFuel(FuelType type) {
        FuelInventory fuel = inventory.get(type);
        return fuel != null && fuel.isAvailable() && fuel.getQuantityLiters() > 0;
    }
}
