package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.user.ViewMenuUser;

/**
 * Renders DEVLOPER's viewMenu
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewMenu</li>
 * <li><b>profileId</b> : ROLE_WOKO_DEVELOPER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 * 
 * @see net.sf.woko.facets.guest.ViewMenuGuest
 */
@FacetKey(name="viewMenu", profileId="ROLE_WOKO_DEVELOPER")
public class ViewMenuDevel extends ViewMenuUser implements IExecutable {

	/**
	 * Return the menu JSP fragment 
	 * (<code>/WEB-INF/woko/fragments/menuDevel.jsp</code>)
	 */
	@Override
	public Object execute() {
		return "/WEB-INF/woko/fragments/menuDevel.jsp";
	}
	
}
