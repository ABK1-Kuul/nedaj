package models;

public class FuelInventory {
    private double price;
    private double quantityLiters;
    private boolean isAvailable;

    public FuelInventory(double price, double quantityLiters, boolean isAvailable) {
        this.price = price;
        this.quantityLiters = quantityLiters;
        this.isAvailable = isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantityLiters() {
        return quantityLiters;
    }

    public void setQuantityLiters(double quantityLiters) {
        this.quantityLiters = quantityLiters;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
