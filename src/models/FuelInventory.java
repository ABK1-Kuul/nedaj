//Enum defining BENZENE, REGULAR, KERO
package models;
public class FuelInventory {
    private FuelType type;
    private double quantity;
    private double pricePerLiter;
    private boolean available;

    public FuelInventory(FuelType type, double quantity, double pricePerLiter, boolean available){
        this.type = type;
        setQuantity(quantity);
        setPricePerLiter(pricePerLiter);
        setAvailable(available);
    }
   public FuelInventory(FuelInventory other){
        this (other.type, other.quantity, other.pricePerLiter, other.available);
   }

    public FuelType getType() {

        return type;
    }

    public void setType(FuelType type) {
        if(type == null){
            throw new IllegalArgumentException("Fuel type cannot be null");
        }
        this.type = type;
    }

    public double getQuantity() {

        return quantity;
    }

    public void setQuantity(double qty) {
        if (qty < 0){
            throw new IllegalArgumentException("Quantity cannot be negative. Attempted:" + qty);
        }
        this.quantity = qty;

        if (qty == 0){
           this.available = false;
        }
    }
    public double getPricePerLiter(){

        return pricePerLiter;
    }

    public void setPricePerLiter(double price) {
        if (price < 0){
            throw new IllegalArgumentException("Price must be positive. Attempted:" + price);
        }
        this.pricePerLiter = price;
    }

    public boolean getIsAvailable() {

        return available;
    }
    public void setAvailable(boolean available){
        if(available && this.quantity == 0){
            throw new IllegalArgumentException("Cannot set available to true when quantity is 0. Add stock first.");
        }
        this.available = available;
    }
    public void addStock(double amount){
        if (amount < 0){
            throw new IllegalArgumentException("Cannot add negative stock. Use removeStock() to decrease.");
        }
        if (amount == 0){
            return;
        }

        double oldQuantity = this.quantity;
        this.quantity += amount;

        System.out.printf("Added %.2f L of %s. New quantity: %.2f L%n", amount, type.name(),quantity);

    }
   public void removeStock(double amount){
        if (amount < 0){
            throw new IllegalArgumentException("Cannot remove negative stock. Use addStock() to increase.");
        }
        if (amount == 0){
            return;
        }
        if (amount > this.quantity){
            throw new IllegalArgumentException(
                    String.format("Insufficient stock! Available: %2f L, Requested: %2f L", quantity, amount)
            );
        }
        this.quantity -= amount;
        System.out.printf("Sold %.2f L of %s. Remaining: %.2f L%n", amount, type.name(), quantity );

        if (this.quantity == 0){
            this.available = false;
            System.out.println("Stock Depleted!" + type.name() + "is now unavailable.");
        }
   }
   public boolean isSellable(){

        return this.available && this.quantity > 0;
   }
   public double getTotalValue(){

        return  this.quantity * this.pricePerLiter;
   }

   public void resetTODefaultPrice(){
        setPricePerLiter(this.type.getDefaultPrices());
        System.out.println("Reset price of" + type.name() + "to default: " + pricePerLiter + "ETB/L");
   }
   public String getStatus(){
        String statusIcon = isSellable() ? "Sellable" : (available ? "MAINTENANCE" : "UNAVAILABLE");
        return String.format("%s | %.2f L @ %.2f ETB/L | %s",
       type.name(), quantity, pricePerLiter, statusIcon);
   }
}


