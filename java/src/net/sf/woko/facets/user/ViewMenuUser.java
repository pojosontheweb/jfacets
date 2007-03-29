package net.sf.woko.facets.user;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.guest.ViewMenuGuest;

/**
 * Overrides READ's viewMenu in order to show role WRITE's menu
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewMenu</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 * 
 * @see net.sf.woko.facets.guest.ViewMenuGuest
 */
@FacetKey(name="viewMenu", profileId="ROLE_WOKO_USER")
public class ViewMenuUser extends ViewMenuGuest implements IExecutable {

	/**
	 * Return the menu JSP fragment 
	 * (<code>/WEB-INF/woko/fragments/menuUser.jsp</code>)
	 */
	@Override
	public Object execute() {
		return "/WEB-INF/woko/fragments/menuUser.jsp";
	}
	
}
