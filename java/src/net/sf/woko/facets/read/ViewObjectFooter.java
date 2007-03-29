package net.sf.woko.facets.read;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;

/**
 * Facet used to render the footer of an object
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewFooter</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="viewFooter",profileId="ROLE_WOKO_GUEST",targetObjectType=Object.class)
public class ViewObjectFooter extends BaseFacet implements IExecutable {

	/**
	 * Return the path to the JSP fragment to be used for rendering the 
	 * target object's footer : 
	 * <code>/WEB-INF/woko/fragments/read/footer.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/read/footer.jsp";
	}
}