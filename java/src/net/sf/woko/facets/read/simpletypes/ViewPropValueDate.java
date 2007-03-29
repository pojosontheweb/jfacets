package net.sf.woko.facets.read.simpletypes;

import java.util.Date;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewPropertyValue;

/**
 * Facet used to render java.util.Date properties
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewPropertyValue</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : java.util.Date</li>
 * </ul>
 * @see net.sf.woko.facets.read.ViewPropertyValue
 */
@FacetKey(name="viewPropertyValue",profileId="ROLE_WOKO_GUEST",targetObjectType=Date.class)
public class ViewPropValueDate extends ViewPropertyValue  {

	/**
	 * Return the fragment to be used to render the property : 
	 * <code>/WEB-INF/woko/fragments/read/simpletypes/prop-date.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/read/simpletypes/prop-date.jsp";
	}
	
	

}
