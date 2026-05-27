package services.search;

import java.util.List;

import models.GasStation;

/** Lists stations in the zone that have a line and the requested fuel (no extra sorting). */
public class ZoneSearchStrategy extends AbstractSearchStrategy {

    @Override
    public List<GasStation> search(List<GasStation> stations, SearchCriteria criteria) {
        return applyCommonFilters(stations, criteria);
    }
}
