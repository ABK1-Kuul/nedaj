//Holds in-memory lists, mock data, lookup logic
package services;

import java.util.ArrayList;
import java.util.Comparator;
import.java.util.List;
import java.util.List;
import java.util.stream.Collector;

import models.FuelInventory;
import models.FuelType;
import models.GasStation;

public class GasStationService {
    private final List<GasStation> stations = new ArrayList<>();

    public void seedMockData(){
        GasStation s1 = new GasStation("STOO1", "TotalEnergies Bole", "Bole");
        s1.addFuelInventory(FuelType.BENZENE, new FuelInventory(500, 170, true));
        s1.addFuelInventory(FuelType.DIESEL, new FuelInventory(0, 171, false));
        s1.addFuelInventory(FuelType.KEROSENE, new FuelInventory(500, 350, true));

        GasStation s2 = new GasStation("ST002", "NOC Megenagna", "Megnagna");
        s2.addFuelInventory(FuelType.BENZENE, new FuelInventory(2000, 168, true));
        s2.addFuelInventory(FuelType.DIESEL, new FuelInventory(3000, 190, true));
        s2.addFuelInventory(FuelType.KEROSENE, new FuelInventory(0, 321, false));

        GasStation s3 = new GasStation("STOO3","OLA Megenagna","Megenagna");
        s3.addFuelInventory(FuelType.BENZENE, new FuelInventory(800, 169, true));
        s3.addFuelInventory(FuelType.DIESEL, new FuelInventory(900, 187, true));
        s3.addFuelInventory(FuelType.KEROSENE, new FuelInventory(1000, 345, true));

        GasStation s4 = new GasStation("ST004","NOC Summit", Summit);
        s4.addFuelInventory(FuelType.BENZENE, new FuelInventory(400, 169, true));
        s4.addFuelInventory(FuelType.DIESEL, new FuelInventory(500, 190, true));
        s4.addFuelInventory(FuelType.KEROSENE, new FuelInventory(300, 325, true));

        stations.add(s1);
        stations.add(s2);
        stations.add(s3);
        stations.add(s4);

    }
    // ========== OVERLOADED SEARCH METHODS (POLYMORPHISM DEMO) ==========

    public List<GasStation> searchFuel(String zone){
        if (zone == null || zone.trim().isEmpty()){
            throe new IllegalArgumentException("Zone cannot be null or empty");
        }
        List<GasStation> results = new ArrayList<>();
        for (GasStation station : stations){
            if (station.getZone().equalsIgnoreCase(zone.trim())){
                results.add(station);
            }
        }
        return results;
    }
    // METHOD OVERLOADING by zone and fuel type

    public List<GasStation> searchFuel(String zone, FuelType fuelType){
        if (zone == null || zone.trim().isEmpty()){
            throw new IllegalArgumentException("Zone cannot be null or empty");
        }
        if (fuelType == null){
            throw new IllegalArgumentException("Fuel type cannot be null");

        }
        List<GasStation> results = new ArrayList<>();
        for (GasStation station : stations){
            if (station.getZone().equalsIgnoreCase(zone.trim()) && station.hasFuel(fuelType)){
                results.add(station);
            }
        }
        return results;
    }
   // METHOD OVERLOADING: Search by price range

   public List<GasStation> searchFuel(FuelType fuelType, double minPrice, double maxPrice) {
       if (fuelType == null) {
           throw new IllegalArgumentException("Invalid price range");
       }
       if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
           throw new IllegalArgumentException("Invalid price range");
       }
       List<GasStation> results = new ArrayList<>();
       for (GasStation station : stations) {
           FuelInventory fuel = station.getInventory().get(fuelType);
           if (fuel != null && fuel.isSellable()) {
               double price = fuel.getPricePerLiter();
               if (price >= minPrice && price <= maxPrice) {
                   results.add(station);
               }
           }
       }
       return results;
   }
    // METHOD OVERLOADING: Search by minimum quantity only
    public List<GasStation> searchFuel(FuelType fuelType, double minQuantity){
        if (fuelType == null){
            throw new IllegalArgumentException("Fuel type cannot be null");

        }
        if (minQuantity < 0){
            throw new IllegalArgumentException("Minmum quantity cannot be negative");
        }
        List<GasStation> results = new ArrayList<>();
        for (GasStation station : stations){
            FuelInventory fuel = station.getInventory().get(fuelType);
            if (fuel != null && fuel.getIsAvailable() && fuel.getQuantity() >= minQuantity){
                results.add(station);
            }
        }
        return results;
       }
    // METHOD OVERLOADING Search by zone and price

    public List<GasStation> searchFuelSortedbyPrice(String zone, FuelType fuelType) {
      List<GasStation> results = searchFuel(zone, fuelType);
    // Sort by price using Comparator
    results.sort(Comparator.comparingDouble(station -> {
        FuelInventory fuel = station.getInventory().get(fuelType);
        return fuel != null ? fuel.getPricePerLiter() : Double.MAX_VALUE;
    }));

    return results;
    }
   // METHOD OVERLOADIG: Get all stations with any available fuel

   public List<GasStation> searchFuel(){
        List<GasStation> results = new ArrayList<>();
        for (GasStation station : stations){
            if (station.hasFuel()){
                results.add(station);
            }
        }
        return results;
   }

   public GasStation findStationById(String stationId){
        if (stationId == null || stationId.trim().isEmpty()){
            throw new IllegalArgumentException("Station ID cannot be null or empty!");
        }
        for (GasStation station : stations){
            if (station.getId().equalsIgnoreCase(stationId.trim())){
                return station;
            }
        }
        return null;
   }
   public List<GasStation> findStationByName(String namePattern){
        if (namePattern == null || namePattern.trim().isEmpty()){
            throw new IllegalArgumentException("Name patter cannot be null or empty");
        }
        List<GasStation> results = new ArrayList<>();
        String pattern = namePattern.trim().toLowerCase();

        for (GasStation station : stations){
            if (station.getName().toLowerCase().contains(pattern)){
                results.add(station);
            }
        }
        return results;
   }
   public List<GasStation> getStationsByZone(String zone){
        return searchFuel(zone);
   }
   public List<GasStation> getAllStations(){
        return new ArrayList<>(stations);
   }
   public boolean updateInventory(String stationId, FuelType fuelType, double quantity, boolean available){
        GasStation station = findStationById(stationId);
        if (station == null){
            return false;
        }
        FuelInventory fuel = station.getInventory().get(fuelType);
        if (fuel == null){
            return false;
        }
        station.updateFuelStatus(fuelType, quantity, available);
        return true;
   }
  // METHOD OVERLOADING : Update only quantity

  public boolean updateFuelStatus(String stationId, FuelType fuelType, double quantity){
        GasStation station = findStationById(stationId);
        if (station == null) {
            return false;
        }

        FuelInventory fuel = station.getInventory().get(fuelType);
        if (fuel == null){
            return false;
        }
        boolean currentAvailabilty = fuel.IsAvailable();
        station.updateFuelStstus(fuelType, quantity, currentAvailabilty);
        return true;
  }
  // Update only price
  public boolean updateFuelPrice(String stationId, FuelType fuelType, double newPrice){
        GasStation station = findStationById(stationId);
        if (station == null){
            return false;
        }
        FuelInventory fuel = station.getInventory().get(fuelType);
        if (fuel == null){
            return false;
        }
        fuel.setPricePerLiter(newPrice);
        return true;
  }
  // Update price for all stations
    public int updateFuelPrice(FuelType fuelType, double newPrice){
        if (newPrice <= 0){
            throw new IllegalArgumentException("Price must be positive");
        }
        int updatedCount = 0;
        for (GasStation station : stations){
            FuelInventory fuel = station.getInventory().get(fuelType);
            if (fuel!= null){
                fuel.setPricePerLiter(newPrice);
                updatedCount++;
            }
        }
        return updatedCount;
    }
    public double getTotalAvailableQuantity(FuelType fuelType){
        double total = 0;
        for (GasStation station : stations){
            FuelInventory fuel = station.getInventory().get(fuelType);
            if (fuel != null && fuel.isSellable()){
                total += fuel.getQuantity();
            }
        }
        return total;
    }
   // Find Station with lowest Price for a Fuel type
    public GasStation findCheapestStation(FuelType fuelType) {
        List<GasStation> stationsWithFuel = searchFuel(null, fuelType);
        if (stationsWithFuel.isEmpty()) {
            return null;
        }

        return stationsWithFuel.stream()
                .min(Comparator.comparingDouble(station ->
                        station.getInventory().get(fuelType).getPricePerLiter()))
                .orElse(null);
    }
  // Get all unique Zones
  public List<String> getAllZones() {
      return stations.stream()
              .map(GasStation::getZone)
              .distinct()
              .collect(Collectors.toList());
  }
 // Display summary of all stations (For debugging / admin)
 public void printAllStations() {
     System.out.println("\n=== ALL STATIONS SUMMARY ===");
     for (GasStation station : stations) {
         System.out.println(station);
         System.out.println("  Inventory:");
         for (FuelType type : FuelType.values()) {
             FuelInventory fuel = station.getInventory().get(type);
             if (fuel != null) {
                 System.out.println("    " + fuel.getStatus());
             }
         }
         System.out.println();
     }
 }


}