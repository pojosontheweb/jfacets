package net.sf.woko.facets.read.simpletypes;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewPropertyValue;

/**
 * Facet used to render java.lang.Number properties
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewPropertyValue</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : java.lang.Number</li>
 * </ul>
 * @see net.sf.woko.facets.read.ViewPropertyValue
 */
@FacetKey(name="viewPropertyValue",
		profileId="ROLE_WOKO_GUEST",
		targetObjectType=Number.class)
public class ViewPropValueNumber extends ViewPropertyValue  {

	/**
	 * Return the fragment to be used to render the property : 
	 * <code>/WEB-INF/woko/fragments/read/simpletypes/prop-number.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/read/simpletypes/prop-number.jsp";
	}
	
	

}
