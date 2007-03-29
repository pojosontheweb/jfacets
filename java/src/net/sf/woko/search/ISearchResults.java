package net.sf.woko.search;

import java.util.Map;
import java.util.List;

/**
 * Interface for search result objects
 */
public interface ISearchResults {

    public String getQuery();

    public int getCount();

    public List<Object> getResults();

    public Float getScore(Object o);
}
