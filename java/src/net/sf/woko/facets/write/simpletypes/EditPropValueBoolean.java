package net.sf.woko.facets.write.simpletypes;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.write.EditPropertyValue;

/**
 * Facet used for editing Boolean properties
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : editPropertyValue</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : java.lang.Boolean</li>
 * </ul>
 * @see net.sf.woko.facets.write.EditPropertyValue
 */
@FacetKey(name="editPropertyValue",
		profileId="ROLE_WOKO_GUEST",
		targetObjectType=Boolean.class)
public class EditPropValueBoolean extends EditPropertyValue {
	
	/**
	 * Return the JSP fragment to be used for Boolean properties (unless 
	 * original result preferred) :
	 * <code>/WEB-INF/woko/fragments/write/simpletypes/prop-boolean.jsp</code>
	 */
	@Override
	public Object execute() {
		Object res = super.execute();
		if (isOriginalResultPreferred())
			return res;
		else 
			return "/WEB-INF/woko/fragments/write/simpletypes/prop-boolean.jsp";
	}
	
}
