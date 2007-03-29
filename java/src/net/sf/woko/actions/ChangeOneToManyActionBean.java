package net.sf.woko.actions;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.facets.write.associations.GetXToManyChoices;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;

/**
 * Used for modification of x-to-many associations.
 */
@UrlBinding("/tools/change-x-to-many.action")
public class ChangeOneToManyActionBean extends BaseActionBean {
	
	public static final String JSP_ONE_TO_MANY = "/WEB-INF/woko/change-x-to-many.jsp";
	
	private static final Logger logger = Logger
			.getLogger(ChangeOneToManyActionBean.class);
	
	@Validate(required=true)
	private String id;
	@Validate(required=true)
	private String className;
	@Validate(required=true)
	private String propertyName;
	
	private ArrayList<String> toAdd = new ArrayList<String>();

	private ArrayList<String> toRemove = new ArrayList<String>();

	private List available;
	private Collection alreadyThere;
	
	private Object targetObject;
	
	private Class getCompoundType() {
		Field[] fields = Util.getFields( targetObject.getClass() );
		Field f = null;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(propertyName)) {
				f = fields[i];
				break;
			}
		}
        if (f==null)
            throw new RuntimeException("Unabele to find the field for compound type for property " + propertyName);
        
        ParameterizedType t = (ParameterizedType)f.getGenericType();
        return (Class)t.getActualTypeArguments()[0];
	}
	
	@SuppressWarnings("unchecked")
	@DefaultHandler
	public Resolution displayChoices() {
		// find available and already there using a facet
		targetObject = Util.getObjectData(id, className, getContext())[0];
		PropertyDescriptor pd = getPropertyDescriptor(targetObject, propertyName);		
		WebFacets wf = WebFacets.get(getRequest());
		GetXToManyChoices choicesFacet = 
			(GetXToManyChoices)wf.getFacet("getXToManyChoices", getTargetObject(), getRequest());
		available = choicesFacet.getAvailable(pd, getCompoundType());
		alreadyThere = choicesFacet.getAlreadyThere(pd);
		
		return new ForwardResolution(JSP_ONE_TO_MANY);
	}
	
	private PropertyDescriptor getPropertyDescriptor(Object targetObject, String propertyName) {
		try {
			BeanInfo bi = Introspector.getBeanInfo( targetObject.getClass() );
			PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
			PropertyDescriptor pd = null;
			for (int i = 0; (i<descriptors.length && pd==null); i++)
				if (descriptors[i].getName().equals(propertyName))
					pd = descriptors[i];
			return pd;
		} catch (IntrospectionException e) {
			logger.error("Unable to introspect object " + targetObject);
			return null;
		}
	}
	
	private Collection getProperty(Object targetObject, String propertyName) throws Exception {
		PropertyDescriptor pd = getPropertyDescriptor(targetObject, propertyName);
		Method m = pd.getReadMethod();
        return (Collection)m.invoke(targetObject, new Object[0]);
	}


	private void setProperty(Object targetObject, String propertyName, Collection value) throws Exception {
		PropertyDescriptor pd = getPropertyDescriptor(targetObject, propertyName);
		Method m = pd.getWriteMethod();
		m.invoke(targetObject, new Object[]{value});
	}

	@SuppressWarnings("unchecked")
	@HandlesEvent("ok")
	public Resolution add() {
		try {			
			// reload object
			targetObject = Util.getObjectData(id, className, getContext())[0];		
			Collection propValue = getProperty(targetObject, propertyName);			
	
			// add new association ends to collection prop
			Class compoundType = getCompoundType();
			if (toAdd!=null) {
				for (String id : toAdd) {
					Object newAssociationEnd = Util.getObjectData(id, compoundType.getName(), getContext())[0];
					propValue.add(newAssociationEnd);	
//					HibernateFilter.getSession().update(newAssociationEnd);					
				}
			} 
			// remove the ones to be removed...
			if (toRemove!=null) {
				for (String id : toRemove) {
					Object oldAssociationEnd = Util.getObjectData(id, compoundType.getName(), getContext())[0];
					propValue.remove(oldAssociationEnd);	
//					HibernateFilter.getSession().update(newAssociationEnd);					
				}
			} 
			
			setProperty(targetObject, propertyName, propValue);
			
			// update the object
			Util.getSession().update(targetObject);
			Util.commit();

            getContext().getMessages().add(new LocalizableMessage("woko.edit.association.info.ok", new Object[0]));            

            // redirect to the message page...
			RedirectResolution rr = new RedirectResolution(EditActionBean.class);
			rr.addParameter("id", new Object[]{ id });
			rr.addParameter("className", new Object[]{ className });
			return rr.flash(this);
			
		} catch(Exception e) {
			String msg = "Unable to assign the new property !";
			logger.error(msg, e);
			getContext().getValidationErrors().addGlobalError(new SimpleError(msg));
			RedirectResolution rr = new RedirectResolution(JSP_ONE_TO_MANY);
			return rr.flash(this);
		}
	}
	
	@HandlesEvent("cancel")
	public Resolution cancel() {
		getContext().getMessages().add(new SimpleMessage("Operation canceled"));
		RedirectResolution rr = new RedirectResolution(EditActionBean.class);
		rr.addParameter("id", new Object[]{ id });
		rr.addParameter("className", new Object[]{ className });
		return rr.flash(this);
	}

	public List getAvailable() {
		return available;
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

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public ArrayList<String> getToAdd() {
		return toAdd;
	}

	public void setToAdd(ArrayList<String> toAdd) {
		this.toAdd = toAdd;
	}

	public ArrayList<String> getToRemove() {
		return toRemove;
	}

	public void setToRemove(ArrayList<String> toRemove) {
		this.toRemove = toRemove;
	}

	public Collection getAlreadyThere() {
		return alreadyThere;
	}

	public void setAlreadyThere(Collection alreadyThere) {
		this.alreadyThere = alreadyThere;
	}

    public String getPropertyClass() {
        return getCompoundType().getName();    
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
