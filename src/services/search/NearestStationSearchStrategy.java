package services.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import models.GasStation;

/** Same filters as zone search, then sorts by Euclidean distance from the driver. */
public class NearestStationSearchStrategy extends AbstractSearchStrategy {

    @Override
    public List<GasStation> search(List<GasStation> stations, SearchCriteria criteria) {
        List<GasStation> filtered = applyCommonFilters(stations, criteria);
        List<GasStation> sorted = new ArrayList<>(filtered);
        sorted.sort(Comparator.comparingDouble(s -> distanceFromDriver(s, criteria)));
        return sorted;
    }
}
