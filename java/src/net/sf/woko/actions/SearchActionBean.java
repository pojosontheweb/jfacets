package net.sf.woko.actions;

import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.search.CompassSearchResults;
import net.sf.woko.search.HQLSearchResults;
import net.sf.woko.facets.read.DoSearch;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

/**
 * Action used for Compass search.
 */
@UrlBinding("/@search/:query/:_eventName")
public class SearchActionBean extends BaseActionBean {

	@Validate(required=true)
	private String query;

    @DefaultHandler
	public Resolution searchCompass() {
        // get hold of the doSearch facet
        DoSearch doSearch = getDoSearchFacet();
        // can the search be performed ?
        if (doSearch.allowCompassSearch()) {
            // yep, do it...
            CompassSearchResults results = doSearch.executeCompassSearch();
            // and use woko to display the results !
            return Util.browseForwardResolution(results, getRequest());
        } else {
            throw new RuntimeException("You are not allowed to perform full-text searches !");
        }
    }

	public Resolution searchHQL() {
        // get hold of the doSearch facet
        DoSearch doSearch = getDoSearchFacet();
        // can the search be performed ?
        if (doSearch.allowHQLSearch()) {
            // yep, do it...
            HQLSearchResults results = doSearch.executeHQLSearch();
            // and use woko to display the results !
            return Util.browseForwardResolution(results, getRequest());
        } else {
            throw new RuntimeException("You are not allowed to perform full-text searches !");
        }
    }

    private DoSearch getDoSearchFacet() {
        WebFacets wf = WebFacets.get(getRequest());
        return (DoSearch)wf.getFacet("doSearch", query, String.class, getRequest());
    }

    public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
