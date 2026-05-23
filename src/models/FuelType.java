//Enum defining BENZENE, REGULAR, KERO
package models;
public enum FuelType{
    BENZENE(167.41),
    DIESEL(181.46),
    KEROSENE(320);

    private double defaultPrices;

    FuelType(double defaultPrices){

        this.defaultPrices = defaultPrices;
    }

    public double getDefaultPrices(){

        return defaultPrices;
    }

    public void setDefaultPrices(double newPrices) {
        if (newPrices <= 0){
            throw new IllegalArgumentException("Price must be positive!");
        }
        this.defaultPrices = newPrices;
    }
    public static FuelType fromString(String input){
        if (input == null){
            throw new IllegalArgumentException("Fuel type cannot be null");
        }
        String normalized = input.trim().toUpperCase();
        for (FuelType type : FuelType.values()){
            if(type.name().equals(normalized)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid fuel type:" + input + "Valid options: BENZENE, DIESEL, KEROSENE" );
    }
    public static boolean isValidFuelType(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }


        try {
            fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    public static String getAvailableFuelTypes(){
        StringBuilder sb = new StringBuilder("Available Fuel Types:\n");
        for (FuelType type : FuelType.values()){
            sb.append(String.format("• %s: %.2f ETB/L\n",
                    type.name(), type.getDefaultPrices()));
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        return String.format("%s (%.2f ETB/L)", name(), defaultPrices);
    }
}

<<<<<<< HEAD
=======
}
>>>>>>> 1b623b0a3b2d78e3b9f0f8ef6ab758c3a97684da
