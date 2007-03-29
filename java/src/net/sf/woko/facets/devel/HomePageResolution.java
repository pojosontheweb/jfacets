package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sourceforge.stripes.action.ForwardResolution;

/**
 * Facet that overrides GUEST's homePageResolution in order to show 
 * a different home page.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : homePageResolution</li>
 * <li><b>profileId</b> : ROLE_WOKO_DEVELOPER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="homePageResolution",profileId="ROLE_WOKO_DEVELOPER")
public class HomePageResolution extends BaseFacet implements IExecutable {

	/**
	 * returns a Stripes <code>ForwardResolution</code> 
	 * object that is used in order to show the user home page :
	 * <code>/WEB-INF/woko/homePageDeveloper.jsp</code>
	 */
	public Object execute() {
		return new ForwardResolution("/WEB-INF/woko/homePageDeveloper.jsp");
	}

}
