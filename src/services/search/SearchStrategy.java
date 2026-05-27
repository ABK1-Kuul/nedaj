package services.search;

import java.util.List;

import models.GasStation;

/** Contract for how stations are filtered and ordered in a driver search. */
public interface SearchStrategy {
    List<GasStation> search(List<GasStation> stations, SearchCriteria criteria);
}
