package net.sf.woko.facets.write;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewObject;

/**
 * Facet for rendering the target object in edit-mode
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : edit</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="edit", profileId="ROLE_WOKO_USER")
public class EditObject extends ViewObject {
			
	/**
	 * Return the path of the JSP fragment to be used for 
	 * rendering the target object :
	 * <code>/WEB-INF/woko/fragments/write/edit.jsp</code>
	 */
	@Override
	public Object execute() {
		super.execute();
		return "/WEB-INF/woko/fragments/write/edit.jsp";
	}

}
