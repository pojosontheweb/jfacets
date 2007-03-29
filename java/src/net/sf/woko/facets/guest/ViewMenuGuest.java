package net.sf.woko.facets.guest;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;

/**
 * Return the JSP fragment to be used for building the 
 * menu bar (nav bar) for the GUEST users.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewMenu</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="viewMenu", profileId="ROLE_WOKO_GUEST")
public class ViewMenuGuest extends BaseFacet implements IExecutable {

	/**
	 * Return the menu JSP fragment (<code>/WEB-INF/woko/fragments/menuGuest.jsp</code>)
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/menuGuest.jsp";
	}

}
