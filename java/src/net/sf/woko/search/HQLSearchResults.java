package net.sf.woko.search;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * Wrapper for HQL search hqlResults
 */
public class HQLSearchResults implements ISearchResults {
	
	private Collection<Object> hqlResults;
	private String hql;

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public Collection<Object> getHqlResults() {
		return hqlResults;
	}
	
	public int getCount() {
		return getHqlResults().size();
	}

    public List<Object> getResults() {
        return new ArrayList<Object>(hqlResults);
    }

    public Float getScore(Object o) {
        return null;
    }

    public void setHqlResults(Collection<Object> hqlResults) {
		this.hqlResults = hqlResults;
	}

    public String getQuery() {
        return hql;
    }

}
