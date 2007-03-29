package net.sf.woko.facets.search;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewObject;
import net.sf.woko.search.ISearchResults;

/**
 * Facet used to display the search results objects
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : view</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : ISearchResults</li>
 * </ul>
 */
@FacetKey(name="view",profileId="ROLE_WOKO_USER",targetObjectType= ISearchResults.class)
public class ViewSearchResults extends ViewObject {


    public Object execute() {
        super.execute();
        return "/WEB-INF/woko/fragments/read/search-results.jsp";
    }


}
