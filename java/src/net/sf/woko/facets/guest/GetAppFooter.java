package net.sf.woko.facets.guest;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;

/**
 * Executable facet that returns the path to a fragment 
 * to used for the application footer.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * 	<li><b>name</b> : getAppFooter</li>
 * 	<li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 *  <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="getAppFooter", profileId="ROLE_WOKO_GUEST")
public class GetAppFooter extends BaseFacet implements IExecutable {

	/**
	 * Return the path to the footer JSP fragment :
	 * <code>/WEB-INF/woko/fragments/app-footer.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/app-footer.jsp";
	}
	
}
