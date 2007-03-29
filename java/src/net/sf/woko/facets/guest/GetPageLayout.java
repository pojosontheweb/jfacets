package net.sf.woko.facets.guest;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;

/**
 * Facet that returns the path to the layout to be used when 
 * executed. 
 * This implementation returns "/WEB-INF/woko/layout.jsp". You can override 
 * this facet for your profiles and objects if you want to radically change the 
 * layout.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : getPageLayout</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="getPageLayout",profileId="ROLE_WOKO_GUEST",targetObjectType=Object.class)
public class GetPageLayout extends BaseFacet implements IExecutable {

	/**
	 * Return the path to the Stripes layout to be used.
	 */
	public Object execute() {
		return "/WEB-INF/woko/layout.jsp";
	}

}
