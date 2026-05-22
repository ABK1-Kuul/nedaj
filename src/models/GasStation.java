//Aggregates info & Map<FuelType, FuelInventory>
package models;

import java.util.HashMap;
import java.util.Map;

public class GasStation {
    private String id;
    private String name;
    private String zone;
    private Map<FuelType, FuelInventory> inventory;

    public GasStation(String id, String name, String zone){
        this.id = id;
        this.name = name;
        this.zone = zone;
        this.inventory = new HashMap<>;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public String getZone() {
        return zone;
    }

    public Map<FuelType, FuelInventory> getInventory() {
        return inventory;
    }

    public void addFuelInventory(FuelType, double quantity, double price){
        FuelInventory inv = new FuelInventory(type, quantity, price, quantity > 0);
        inventory.put(type, inv);
    }
    public boolean hasFuel(FuelType type){
        FuelInventory inv = inventory.get(type);
        return inv != null && inv.isSellable();
    }
    public void sellFuel(FUelType type, double liters){
        FuelInventory inv = inventory.get(type);
        if (inv == null || !inv.isSellable()){
            throw ew IllegalStatException("Fuel not available");
        }
        inv.removeStock(liters);
    }

}