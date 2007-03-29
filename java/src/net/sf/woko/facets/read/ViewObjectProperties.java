package net.sf.woko.facets.read;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;

import org.apache.log4j.Logger;

/**
 * Facet used to render properties of the target object dynamically, using 
 * java reflection. 
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewProperties</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 * This facet can be used to limit the properties you want to show for 
 * a given object and profile by overriding this facet and returning the 
 * list of properties in your overriden getPropertyNames().
 *
 */
@FacetKey(name="viewProperties",profileId="ROLE_WOKO_GUEST",targetObjectType=Object.class)
public class ViewObjectProperties  extends BaseFacet implements IExecutable {
		
	private static final Logger logger = Logger
			.getLogger(ViewObjectProperties.class);

	/** property descriptors and property values */
	private HashMap<PropertyDescriptor, Object> propertyDescriptors;
	/** short class name of the target object */
	private String shortClassName;
	/** property descriptors by their name */
	private HashMap<String, PropertyDescriptor> descriptorsByName;
	
	/**
	 * Computes the list of descriptors to be displayed and 
	 * return the path to the JSP fragment to be used for 
	 * rendering the properties : 
	 * <code>/WEB-INF/woko/fragments/read/properties.jsp</code>
	 */
	public Object execute() {
		
		// bind short class name 
		Class clazz = getContext().getTargetObject().getClass();
		String className = clazz.getName();
		int i = className.lastIndexOf('.');
		if (i!=-1)
			className = className.substring(i+1);
		shortClassName = className;
		
		// cache property descriptors
		cacheDescriptors();
		
		// return view name
		return "/WEB-INF/woko/fragments/read/properties.jsp";
	}
		
	/**
	 * Cache descriptors to avoid recalculating 
	 * everything every time !
	 */
	private void cacheDescriptors() {
		if (descriptorsByName==null) {
			descriptorsByName = new HashMap<String, PropertyDescriptor>();
			Object target = getContext().getTargetObject();
			logger.debug("Caching descriptors for target object = " + target);
	        BeanInfo beanInfo;
	        try {
				beanInfo = Introspector.getBeanInfo( target.getClass());
				logger.debug("BeanInfo obtained, iterating on properties...");
				PropertyDescriptor pds[] = beanInfo.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					String propName = pds[i].getName();
					descriptorsByName.put(propName,	pds[i]);
					logger.debug("Property " + propName + " added");
				}
				logger.debug("OK, " + pds.length + " descriptors cached");
	        } catch (Exception e) {
				logger.error("Unable to introspect target object " + target + " during property descriptors listing !", e);
			}
		}
	}
	
	/**
	 * Return a map of all the descriptors for properties to be displayed.
	 * @return
	 */
	public HashMap<PropertyDescriptor, Object> getPropertyDescriptors() {
		if (propertyDescriptors==null) {
			propertyDescriptors = new HashMap<PropertyDescriptor, Object>();
			Object target = getContext().getTargetObject();
			logger.debug("Obtaining property descriptors for target object " + target);
	        try {
				List<String> propNames = getPropertyNames();				
				logger.debug(propNames.size() + " property name(s) to be displayed");
				for (String pName : propNames) {
					PropertyDescriptor pd = descriptorsByName.get(pName);
					if (pd==null) {
						logger.error("There's no descriptor for property " + pName + " ! property skipped");
						// TODO : find better error handling
						throw new RuntimeException("Supplied property name '" + pName + "' from getPropertyNames() is invalid ! I am " + this);
					} else {
						logger.debug("  Handling " + pd.getName());
						// get the propety's value...
						Method readMethod = pd.getReadMethod();
						// special handling for booleans !!
						if (pd.getPropertyType().equals(Boolean.class) && readMethod==null) {
							String booleanGetterName = "is" + capitalize(pd.getName());
							readMethod = target.getClass().getMethod(booleanGetterName, new Class[0]);
						}
			        	Object propVal = readMethod.invoke(target, new Object[0]);
						logger.debug("  Prop value =  " + propVal + ", adding to map");
			        	propertyDescriptors.put(pd, propVal);
					}
				}
	        } catch (Exception e) {
				logger.error("Unable to introspect target object " + target + " !", e);
				return new HashMap<PropertyDescriptor, Object>();
			}
		}
		return propertyDescriptors;
	}
	
	private static String capitalize(String s) {
		char chars[] = s.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	/**
	 * Return the short class name (no dots) to be used 
	 * for building the labels
	 */
	public String getShortClassName() {
		return shortClassName;
	}
	
	/**
	 * Return a list of property names, to be displayed in a given order. 
	 * This default implementation returns all properties sorted by alphabetical 
	 * order, but you can override it in your facets in order to change the 
	 * appearing properties and their order.
	 */
	public List<String> getPropertyNames() {
		ArrayList<String> pNames = new ArrayList<String>(descriptorsByName.keySet()); 
		Collections.sort(pNames);
		return pNames;
	}

	/**
	 * Return a map of all descriptors by name
	 */
	public HashMap<String, PropertyDescriptor> getDescriptorsByName() {
		return descriptorsByName;
	}
	
}