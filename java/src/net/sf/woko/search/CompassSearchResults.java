package net.sf.woko.search;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.compass.core.CompassHit;
import org.compass.core.CompassHits;

/**
 * Wrapper class for compass search results.
 */
public class CompassSearchResults implements ISearchResults {
	
	private String query;
	private CompassHits compassHits;
	private final List<CompassHit> hits = new ArrayList<CompassHit>();
	private final HashMap<Object, Float> matchingObjects = new HashMap<Object, Float>();
	
	public CompassSearchResults(String query, CompassHits compassHits) {
		this.query = query;
		this.compassHits = compassHits;		
		initialize();
	}
	
	public void initialize() {
		for (int i = 0; i < compassHits.getLength(); i++) {
			CompassHit hit = compassHits.hit(i); 
			hits.add(hit);	
			matchingObjects.put(hit.getData(), hit.getScore());
        }
	}

	public CompassHits getCompassHits() {
		return compassHits;
	}

	public void setCompassHits(CompassHits compassHits) {
		this.compassHits = compassHits;
	}

	public List<CompassHit> getHits() {
		return hits;
	}
	
	public List<Object> getResults() {
        ArrayList<Object> results = new ArrayList<Object>();
        results.addAll(matchingObjects.keySet());
        return results;
	}

    public Float getScore(Object o) {
        return  matchingObjects.get(o);
    }

    public int getCount() {
		return compassHits.getLength();
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
