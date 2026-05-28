package services.search;

import java.util.Objects;

import models.FuelType;

/** Input for a fuel station search (zone, fuel type, optional driver coordinates). */
public class SearchCriteria {
    private final String zone;
    private final FuelType fuelType;
    private final double driverX;
    private final double driverY;
    private final boolean driverLocationSet;

    public SearchCriteria(String zone, FuelType fuelType) {
        this(zone, fuelType, 0, 0, false);
    }

    public SearchCriteria(String zone, FuelType fuelType, double driverX, double driverY) {
        this(zone, fuelType, driverX, driverY, true);
    }

    private SearchCriteria(String zone, FuelType fuelType, double driverX, double driverY, boolean driverLocationSet) {
        String normalizedZone = Objects.requireNonNull(zone, "zone must not be null").trim();
        if (normalizedZone.isEmpty()) {
            throw new IllegalArgumentException("zone must not be blank");
        }

        this.zone = normalizedZone;
        this.fuelType = Objects.requireNonNull(fuelType, "fuelType must not be null");
        this.driverX = driverX;
        this.driverY = driverY;
        this.driverLocationSet = driverLocationSet;
    }

    public String getZone() {
        return zone;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public double getDriverX() {
        return driverX;
    }

    public double getDriverY() {
        return driverY;
    }

    public boolean hasDriverLocation() {
        return driverLocationSet;
    }
}
