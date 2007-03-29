package net.sf.woko.facets.read;

import java.beans.PropertyDescriptor;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;

/**
 * Base facet for rendering the value of each property, passed as 
 * the target object of this facet. 
 * Instances of this class are executed if no specific facet has been defined for that 
 * property (e.g. "viewPropertyValue",SOME_ROLE,com.myco.model.SomeObject). 
 * If target object is an entity, then a link is displayed for it. Otherwise, 
 * it defaults to "toString()" on the target object.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewPropertyValue</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 * This facet can be overriden in case you want to render the properties of a given 
 * type in a different way. To do so, extend this class and override execute() to render 
 * the property like you want. Reassign the facet to the profile and property type 
 * of your choice.
 */
@FacetKey(name="viewPropertyValue",profileId="ROLE_WOKO_GUEST")
public class ViewPropertyValue  extends BaseFacet implements IExecutable {

	private static final Logger logger = Logger.getLogger(ViewPropertyValue.class);

	/** the value of displayed prop */
	protected Object propVal;
	
	/**
	 * Sets prop val from the target object
	 */
	@Override
	public void setContext(IFacetContext context) {
		super.setContext(context);
		propVal = context.getTargetObject();
	}

	/**
	 * Checks wether or not target object is an entity. If it is, then 
	 * a link is generated (using fragment 
	 * <code>/WEB-INF/woko/fragments/read/view-property-link.jsp</code>), otherwise 
	 * it defaults to toString() on the target object (using fragment 
	 * <code>/WEB-INF/woko/fragments/read/property-value.jsp</code>)
	 */
	@SuppressWarnings("unchecked")
	public Object execute() {			
		logger.debug("executed for " + getPropName() + "=" + getPropVal());
		// check if the object has the @Entity and @Id annotations...
		if (propVal!=null) {
            if (Util.isEntity(propVal)) {
				// it's an entity, lookup for @Id on the fields...
				logger.debug("Found Entity, building link...");
                return "/WEB-INF/woko/fragments/read/view-property-link.jsp";
			}
		}
		// toString() if we don't know what to do...
		return "/WEB-INF/woko/fragments/read/property-value.jsp";
	}

	/**
	 * Return the value of the property to be displayed
	 */
	public Object getPropVal() {
		return propVal;
	}
	
	public String getPropName() {
		return getPropertyDescriptor().getName();
	}
	
	public PropertyDescriptor getPropertyDescriptor() {
		PropertyDescriptor pd = (PropertyDescriptor)getContext().getRequest().getAttribute("propertyDescriptor");
		if (pd==null)
			logger.error("Property descriptor not found in request ! I am " + this);
		return pd;
	}

}