package net.sf.woko.facets.write;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewObjectHeader;

/**
 * Facet used to render the header of an edited object
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : editHeader</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="editHeader", profileId="ROLE_WOKO_GUEST")
public class EditObjectHeader extends ViewObjectHeader implements IExecutable {

	/**
	 * Return the path to the JSP fragment to be used to render the 
	 * target object's header :
	 * <code>/WEB-INF/woko/fragments/write/edit-header.jsp</code>
	 */
	public Object execute() {
		super.execute();
		return "/WEB-INF/woko/fragments/write/edit-header.jsp";
	}
}