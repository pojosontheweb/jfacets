package net.sf.woko.facets.read;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.search.CompassSearchResults;
import net.sf.woko.search.CompassUtil;
import net.sf.woko.search.HQLSearchResults;
import net.sf.woko.util.Util;
import org.hibernate.Session;
import org.hibernate.Query;

import java.util.List;

/**
 * Facet used for performing a compass search. The target object of this facet 
 * is a String with the user's query. The execute() method actually performs 
 * the search, and returns a 
 * <code>CompassSearchResults</code> object holding the results.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : doSearch</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : String</li>
 * </ul>
 */
@FacetKey(name="doSearch",profileId="ROLE_WOKO_USER",targetObjectType=String.class)
public class DoSearch extends BaseFacet {

	/**
	 * Performs a raw compass search, using the target object for 
	 * the query.
	 * @return A CompassSearchResults object holding the results
	 * @see net.sf.woko.search.CompassUtil
	 * @see net.sf.woko.search.CompassSearchResults
     */
	public CompassSearchResults executeCompassSearch() {
		String query = (String)getContext().getTargetObject();
        return CompassUtil.search(query);
	}

    public HQLSearchResults executeHQLSearch() {
        // list all instances for the given type
		Session s = Util.getSession();
        String hql = (String)getTargetObject();
        Query q = s.createQuery(hql);
		List res = q.list();
		HQLSearchResults rs = new HQLSearchResults();
		rs.setHqlResults(res);
		rs.setHql(hql);
        return rs;
    }

    /**
     * Determines wether or not the user can perform a search by
     * object type.
     * This implementation always return true.
     */
    public Boolean allowCompassSearch() {
        return Boolean.TRUE;
    }

    /**
     * Determines wether or not the user can perform a search by
     * object type.
     * This implementation always return true.
     */
    public Boolean allowHQLSearch() {
        return Boolean.TRUE;
    }

}
