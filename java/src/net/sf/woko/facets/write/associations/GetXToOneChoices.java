package net.sf.woko.facets.write.associations;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;

/**
 * Facet that returns a list of objects used as the available 
 * options when editing X-to-one associations. The facet's target object
 * should be an instance owning the association.
 * 
 * This implementation performs a raw HQL select on the property's type. 
 * To change this, override getChoices() in your facets.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : getXToOneChoices</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="getXToOneChoices",profileId="ROLE_WOKO_GUEST")
public class GetXToOneChoices extends BaseFacet {
	
	/**
	 * Return a list of choices for the association end. This 
	 * implementation peforms a raw HQL select in order to fetch 
	 * all instances of the class "propertyClass".
	 */
	public List getChoices(Class propertyClass) {
		// simply perform a basic hql select !
		Session s = Util.getSession();
		String hql = "select o from " + propertyClass.getName() + " as o";
		Query q = s.createQuery(hql);
		return q.list();
	}

}
