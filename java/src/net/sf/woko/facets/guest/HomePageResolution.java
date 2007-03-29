package net.sf.woko.facets.guest;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sourceforge.stripes.action.ForwardResolution;

/**
 * Home Page Resolution facet. 
 * This one returns a Stripes <code>Resolution</code> 
 * object that is used in order to show the user home page.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : homePageResolution</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="homePageResolution",profileId="ROLE_WOKO_GUEST")
public class HomePageResolution extends BaseFacet implements IExecutable {

	/**
	 * returns a Stripes <code>ForwardResolution</code> 
	 * object that is used in order to show the user home page :
	 * <code>/WEB-INF/woko/homePageGuest.jsp</code>
	 */
	public Object execute() {
		return new ForwardResolution("/WEB-INF/woko/homePageGuest.jsp");
	}

}
