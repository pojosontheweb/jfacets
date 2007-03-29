package net.sf.woko.facets.read;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;

/**
 * Facet used to render the title of the target object. 
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewTitle</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 * @author vankeisb
 *
 */
@FacetKey(name="viewTitle",profileId="ROLE_WOKO_GUEST")
public class ViewObjectTitle extends BaseFacet implements IExecutable {
	
	/**
	 * Return the JSP fragment to be used for rendering the title 
	 * of the object : 
	 * <code>/WEB-INF/woko/fragments/read/title.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/read/title.jsp";
	}
	
	/**
	 * Return true if the type (class) should be displayed or 
	 * not.
	 */
	public boolean isShowType() {
		return true;
	}

	/**
	 * Return the title to be displayed for the target object
	 */
	public String getTitle() {
		return Util.getTitle(getTargetObject());
	}
	
	/**
	 * Returh the type to be displayed for the target object
	 */
	public String getType() {
		return Util.getType(getTargetObject());
	}

}
