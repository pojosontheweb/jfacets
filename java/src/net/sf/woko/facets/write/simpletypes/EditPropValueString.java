package net.sf.woko.facets.write.simpletypes;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.write.EditPropertyValue;

/**
 * Facet used for editing String properties
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : editPropertyValue</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : java.lang.String</li>
 * </ul>
 * @see net.sf.woko.facets.write.EditPropertyValue
 */
@FacetKey(name="editPropertyValue",
		profileId="ROLE_WOKO_GUEST",
		targetObjectType=String.class)
public class EditPropValueString extends EditPropertyValue {
	
	/**
	 * Return the JSP fragment to be used (unless 
	 * original result preferred) :
	 * <code>/WEB-INF/woko/fragments/write/simpletypes/prop-string.jsp</code>
	 */
	@Override
	public Object execute() {
		Object res = super.execute();
		if (isOriginalResultPreferred())
			return res;
		else 
			return "/WEB-INF/woko/fragments/write/simpletypes/prop-string.jsp";
	}
	
}
