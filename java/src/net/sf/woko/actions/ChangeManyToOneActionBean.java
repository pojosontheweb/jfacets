package net.sf.woko.actions;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.facets.write.associations.GetXToOneChoices;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

/**
 * Used for modification of x-to-one associations.
 */
@UrlBinding("/tools/change-x-to-one.action")
public class ChangeManyToOneActionBean extends BaseActionBean {
	
	private static final Logger logger = Logger
			.getLogger(ChangeManyToOneActionBean.class);
	
	public static final String JSP_MANY_TO_ONE = "/WEB-INF/woko/change-x-to-one.jsp";
	
	@Validate(required=true)
	private String id;
	@Validate(required=true)
	private String className;
	@Validate(required=true)
	private String propertyName;
	@Validate(required=true)
	private String propertyClass;

    private String selectedObjectId;
	
	private List results;
	private Object targetObject;
		
	@DefaultHandler
	public Resolution displayChoices() {
		// retrieve the available choices using a facet
		targetObject = Util.getObjectData(id, className, getContext())[0];
		WebFacets wf = WebFacets.get(getRequest());
		GetXToOneChoices choicesFacet = 
			(GetXToOneChoices)wf.getFacet("getXToOneChoices", targetObject, getRequest());
		try  {
			Class propClazz = Class.forName(propertyClass);
			results = choicesFacet.getChoices(propClazz);
		} catch(Exception e) {
			logger.error("Unable to load property class ??!!", e);
		}
		return new ForwardResolution(JSP_MANY_TO_ONE);
	}
	
	@HandlesEvent("ok")
	public Resolution ok() {
		displayChoices();
		
		// allright, reload the objects
		Object targetObject = Util.getObjectData(id, className, getContext())[0];
		Object newAssociationEnd = null;
		if (selectedObjectId!=null)
			newAssociationEnd = Util.getObjectData(selectedObjectId, propertyClass, getContext())[0];
		
		try {
		
			// set the new property 
			BeanInfo bi = Introspector.getBeanInfo( targetObject.getClass() );
			PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
			PropertyDescriptor pd = null;
			for (int i = 0; (i<descriptors.length && pd==null); i++)
				if (descriptors[i].getName().equals(propertyName))
					pd = descriptors[i];

            if (pd==null)
                throw new RuntimeException("Unable to find property descriptor for property " + propertyName);
            
            Method m = pd.getWriteMethod();
			m.invoke(targetObject, new Object[]{ newAssociationEnd });
			
			// update the object
			Util.getSession().update(targetObject);
			Util.commit();
			
			getContext().getMessages().add(new LocalizableMessage("woko.edit.association.info.ok", new Object[0]));
			RedirectResolution rr = new RedirectResolution(EditActionBean.class);
			rr.addParameter("id", new Object[]{ id });
			rr.addParameter("className", new Object[]{ className });
			return rr;
			
		} catch(InvocationTargetException e) {			
			String msg = "Unable to assign the new property : " + e.getCause().getMessage();
			logger.error(msg, e);
			return validationError(msg);
		} catch(IllegalAccessException e) {			
			String msg = "Property can't be accessed";
			logger.error(msg, e);
			return validationError(msg);
		} catch(IntrospectionException e) {			
			String msg = "Unable to instrospect object";
			logger.error(msg, e);
			return validationError(msg);
		}
	}
	
	private Resolution validationError(String msg) {
		getContext().getValidationErrors().addGlobalError(new SimpleError(msg));
		return new ForwardResolution(JSP_MANY_TO_ONE);
	}
	
	@HandlesEvent("cancel")
	public Resolution cancel() {
		RedirectResolution rr = new RedirectResolution(EditActionBean.class);
		rr.addParameter("id", new Object[]{ id });
		rr.addParameter("className", new Object[]{ className });
		return rr.flash(this);
	}

	public List getResults() {
		return results;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPropertyClass() {
		return propertyClass;
	}

	public void setPropertyClass(String propertyClass) {
		this.propertyClass = propertyClass;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getSelectedObjectId() {
		return selectedObjectId;
	}

	public void setSelectedObjectId(String selectedObjectId) {
		this.selectedObjectId = selectedObjectId;
	}

	public Object getTargetObject() {
		return targetObject;
	}

    public List<Class> getCandidateClassesForCreation() {
        try {
            String className = getPropertyClass();
            Class clazz = Class.forName(className);
            List<Class> hibernatedClasses = Util.getHibernatedClasses();
            ArrayList<Class> res = new ArrayList<Class>();
            for(Class c : hibernatedClasses) {
                if (!Modifier.isAbstract(c.getModifiers()) && clazz.isAssignableFrom(c)) {
                    res.add(c);
                }
            }
            return res;
        } catch(ClassNotFoundException cnfex) {
            // no way !
            cnfex.printStackTrace();
            return null;
        }
    }

}
