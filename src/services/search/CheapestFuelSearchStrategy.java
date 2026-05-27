package services.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import models.GasStation;

/** Same filters as zone search, then sorts by lowest price per liter for the fuel type. */
public class CheapestFuelSearchStrategy extends AbstractSearchStrategy {

    @Override
    public List<GasStation> search(List<GasStation> stations, SearchCriteria criteria) {
        List<GasStation> filtered = applyCommonFilters(stations, criteria);
        List<GasStation> sorted = new ArrayList<>(filtered);
        sorted.sort(Comparator.comparingDouble(
                s -> s.getInventory().get(criteria.getFuelType()).getPrice()));
        return sorted;
    }
}
