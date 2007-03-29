package net.sf.woko.facets.write.associations;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;

/**
 * Facet that returns a list of objects used as the available 
 * options when editing X-to-many associations. The facet's target object
 * should be the instance owning the association.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : getXToManyChoices</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="getXToManyChoices",profileId="ROLE_WOKO_GUEST")
public class GetXToManyChoices extends BaseFacet {
	
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GetXToManyChoices.class);
	
	private List alreadyThere;
	private List available;
	
	/**
	 * Returns available choices. The list does not contain 
	 * elements returned in getAlreadyThere.
	 * @param compoundType The compound type for the end of the to-many association.
	 */
	public List getAvailable(PropertyDescriptor propertyDescriptor, Class compoundType) {
		if (available==null) {
			// select all objects of compound type
			Session s = Util.getSession();
			String hql = "select o from " + compoundType.getName() + " as o";
			Query q = s.createQuery(hql);
			List l = q.list();
			available = new ArrayList(l);
			available.removeAll(getAlreadyThere(propertyDescriptor));
		}
		return available;
	}
	
	/**
	 * Returns a list of objects already associated.
	 * @param propertyDescriptor The descriptor of the association (collection) property.
	 */
	public final List getAlreadyThere(PropertyDescriptor propertyDescriptor) {
		if (alreadyThere==null) {
			alreadyThere = new ArrayList();			
			try {
				Collection c = (Collection)propertyDescriptor.
					getReadMethod().invoke(getTargetObject(), new Object[0]);
				alreadyThere.addAll(c);
			} catch (Exception e) {
				logger.error("Unable to obtain elements in collection for property " + propertyDescriptor.getName() + " of object " + getTargetObject());
			}
		}
		return alreadyThere;
	}
	
	public List getChoices(Class propertyClass) {
		// simply perform a basic hql select !
		Session s = Util.getSession();
		String hql = "select o from " + propertyClass.getName() + " as o";
		Query q = s.createQuery(hql);
		return q.list();
	}

}
