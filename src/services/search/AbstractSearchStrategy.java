package services.search;

import java.util.ArrayList;
import java.util.List;

import models.GasStation;

/**
 * Base class with shared filtering (zone, queue line, fuel availability).
 * Concrete strategies extend this and add sorting or other behavior.
 */
public abstract class AbstractSearchStrategy implements SearchStrategy {

    protected List<GasStation> applyCommonFilters(List<GasStation> stations, SearchCriteria criteria) {
        List<GasStation> results = new ArrayList<>();
        for (GasStation station : stations) {
            if (station.getZone().equalsIgnoreCase(criteria.getZone())
                    && station.hasLine()
                    && station.hasFuel(criteria.getFuelType())) {
                results.add(station);
            }
        }
        return results;
    }

    protected double distanceFromDriver(GasStation station, SearchCriteria criteria) {
        double dx = station.getX() - criteria.getDriverX();
        double dy = station.getY() - criteria.getDriverY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
