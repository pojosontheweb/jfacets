package net.sf.woko.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.PersistentClass;

import com.mongus.servlet.filter.HibernateFilter;
import com.mongus.stripes.HibernateProvider;
import com.mongus.stripes.PrimaryKeyUtil;

import net.sourceforge.jfacets.acegi.HierarchicalUserDetails;
import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.actions.ViewActionBean;
import net.sf.woko.feeds.annots.Feedable;
import net.sf.woko.facets.read.ViewPropertyValue;
import net.sf.woko.facets.write.EditPropertyValue;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Woko utils !
 */
public class Util {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Util.class);

    /**
     * Map with hibernated class names (short, and fqcn).
     * Used to find the fully qualified class name from
     * the short name.
     */
    private static HashMap<String, String> shortClassNames;

    /**
	 * Return the entity and its class in an Object[2] for passed params using stripernate
	 * type converter
	 * @param id The id of the object to be fetched
	 * @param className The class of the object to be fetched
	 * @param context The action bean context
	 */
	@SuppressWarnings("unchecked")
	public static Object[] getObjectData(String id, String className, ActionBeanContext context) {
        // convert to fqcn if needed
        if (className.indexOf('.')==-1)
            className = shortClassNames.get(className);

        Class targetType;
		try {
			targetType = Class.forName(className);
		} catch (ClassNotFoundException e) {
			String msg = "Unable to load class for name " + className;
			logger.error(msg, e);			
			return null;	
		}
		try {
			TypeConverter tc =  
				StripesFilter.getConfiguration().getTypeConverterFactory().getTypeConverter(targetType, context.getLocale());
            ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
            Object target = tc.convert(id, targetType, errors);
            // TODO : better error handling !
            if (errors.size()>0) {
                for(ValidationError e : errors)
                    context.getValidationErrors().addGlobalError(e);
            }
            return new Object[]{target, targetType};
		} catch(Exception e) {
			String msg = "Unable to load class for name " + className;
			logger.error(msg, e);			
			return null;	
		}
	}
	
	/**
	 * Return true if passed object is a persistent entity
	 */
	public static boolean isEntity(Object obj) {
        return isEntity(obj.getClass());
	}

    /**
     * Return true if passed class is a mapped entity class
     */
    @SuppressWarnings("unchecked")
    public static boolean isEntity(Class clazz) {
        return getHibernatedClasses().contains(deproxifyCglibClass(clazz));
//        return clazz.getAnnotation(Entity.class)!=null;
    }

    /**
	 * Return the ID (PK) of passed entity as a String 
	 * (for now does a toString() on the id's value)
	 */
	@SuppressWarnings("unchecked")
	public static String getId(Object entity) {
		String res = null;
		// check if the object has the @Entity and @Id annotations...
		if (isEntity(entity)) {
            Object key = PrimaryKeyUtil.getPrimaryKeyValue(entity);
            // convert to a String
            if (key!=null)
                res = key.toString();
            else
                res = null;
		}		
		return res;
	}



    /**
	 * Return the short class name for passed object
	 */
	public static String getType(Object target) {
        return target.getClass().getSimpleName();   
	}

	/**
	 * Try to compute a title for passed object 
	 * (try a few methods returned by getTitleMethodNames())
	 */
	public static String getTitle(Object object) {
		String s;
		String[] methodNames = getTitleMethodNames();
		Method readMethod = null;
		Class clazz = object.getClass();
		while(clazz!=null && readMethod==null) {
			Method[] methods = clazz.getDeclaredMethods();
			for (int i = 0; (i < methods.length && readMethod==null); i++) {
				for (int j = 0; (j < methodNames.length && readMethod==null); j++) {
					if (methods[i].getName().equals(methodNames[j]) && 
							methods[i].getParameterTypes().length==0 && 
							methods[i].getReturnType().equals(String.class)) {
						readMethod = methods[i];
					}					
				}
			}
			clazz = clazz.getSuperclass();
		}
		if (readMethod!=null) {
			if (readMethod.getModifiers()==Modifier.PRIVATE || readMethod.getModifiers()==Modifier.PRIVATE)
				readMethod.setAccessible(true);
			try {
				s = (String)readMethod.invoke(object, new Object[0]);
			} catch (Exception e) {
				e.printStackTrace();
				s = "Error !";
			}
		} else {
			s = object.toString();
		}
		return s;
	}
	
	public static String[] getTitleMethodNames() {
		return new String[]{ 
				"getTitle", "getName" };
	}
	
	/**
	 * Return the list of hibernated classes
	 */
	public static List<Class> getHibernatedClasses() {
		ArrayList<Class> res = new ArrayList<Class>();		
		HibernateProvider p = HibernateFilter.getCurrentInstance();
		Iterator it = p.getConfiguration().getClassMappings();
		while (it.hasNext()) {
			PersistentClass pc = (PersistentClass)it.next();
			Class mappedClass = pc.getMappedClass();
			res.add(mappedClass);
		}
        if (shortClassNames == null) {
            shortClassNames = new HashMap<String, String>();
            for(Class c : res)
                shortClassNames.put(stripPackageName(c), c.getName());
        }
        return res;
	}	
	
	/**
	 * Return the current hibernate session
	 */
	public static Session getSession() {
		HibernateProvider provider = HibernateFilter.getCurrentInstance();
		if (provider==null)
			return null;
		else 
			return provider.getSession();
	}
	
	/**
	 * Return the current session factory
	 */
	public static SessionFactory getSessionFactory() {
		return HibernateFilter.getCurrentInstance().getSessionFactory();
	}

	/**
	 * Commit the current hibernate session
	 */
	public static void commit() {
		HibernateFilter.getCurrentInstance().commit();		
	}

	/**
	 * Return the fields for passed class
	 */
	public static Field[] getFields(Class clazz) {
		ArrayList<Field> fields = new ArrayList<Field>();
		while (clazz!=null) {
			Field[] fds = clazz.getDeclaredFields();
			fields.addAll(Arrays.asList(fds));
			clazz = clazz.getSuperclass();
		}
		Field[] result = new Field[fields.size()];
		result = fields.toArray(result);
		return result;		
	}
	
	/**
	 * Return the field for passed class and field name
	 */
	public static Field getField(Class clazz, String name) {
		Field[] fields = getFields(clazz);
		Field f = null;
		for (int i = 0; (i<fields.length && f==null); i++) {
			if (fields[i].getName().equals(name))
				f = fields[i];
		}
		return f;
	}
	
	/**
	 * Return a property descriptor for passed parameters
	 * @param o The object to find the property in
	 * @param propName The name of the property to be found
	 * @return the PropertyDescriptor if found, null otherwise
	 */
	public static PropertyDescriptor getPropertyDescriptor(Object o, String propName) {
		try {
			BeanInfo bi = Introspector.getBeanInfo( o.getClass() );
			PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
			PropertyDescriptor pd = null;
			for (int i = 0; (i<descriptors.length && pd==null); i++)
				if (descriptors[i].getName().equals(propName))
					pd = descriptors[i];
			return pd;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Return the current user name if any. Unwraps the objects 
	 * from Acegi and JFacets and invokes UserDetails.getUsername().
	 */
	public static String getCurrentUsername() {
		String res = null;
		SecurityContext sCtx = SecurityContextHolder.getContext();
		if (sCtx!=null) {
			Authentication auth = sCtx.getAuthentication();
			if (auth!=null) {
				HierarchicalUserDetails hud = (HierarchicalUserDetails) auth.getPrincipal();
				 if (hud!=null) {
					 UserDetails u = hud.getDelegate();
					 if (u!=null)
						 res = u.getUsername();
				}
			}
		}
		return res;
	} 



    /**
	 * Return true if passed object has the Feedable annotation, false otherwise
	 */
	@SuppressWarnings("unchecked")
	public static boolean isFeedable(Object object) {
		Class clazz = object.getClass();
		return clazz.isAnnotationPresent(Feedable.class);
	}
	
	public static String stripPackageName(Class clazz) {
		String s = clazz.getName();
		return stripPackageName(s);
	}

    public static String stripPackageName(String className) {
		int i = className.lastIndexOf('.');
		if (i!=-1)
			return className.substring(i+1);
		else
			return className;
	}

    public static ForwardResolution browseForwardResolution(Object results, HttpServletRequest request) {
        request.setAttribute(ViewActionBean.KEY_TARGET,  results);
        request.setAttribute(ViewActionBean.KEY_TARGET_CLASS,  results.getClass());
        return new ForwardResolution(ViewActionBean.class);
    }


    public static boolean hasAnnotOnFieldOrGetter(Class<? extends Annotation> annotationClass, Field f, Method readMethod) {
        return ((f!=null) && f.isAnnotationPresent(annotationClass)) ||
                ((readMethod!=null) && readMethod.isAnnotationPresent(annotationClass));        
    }

    public static Annotation getAnnotOnFieldOrGetter(Class<? extends Annotation> annotationClass, Field f, Method readMethod) {
        Annotation res = null;
        if (f!=null) {
            res = f.getAnnotation(annotationClass);
        }
        if (res==null && readMethod!=null) {
            res = readMethod.getAnnotation(annotationClass);
        }
        return res;
    }

    /**
     * Return the proxified class for passed class.
     * If the passed class is not CGLIB enhanced, then it returns that same class.
     */
    public static Class deproxifyCglibClass(Class proxifiedClass) {
        String proxifiedClassName = proxifiedClass.getName();
        int i = proxifiedClassName.indexOf("$$");
        if (i==-1) {
            return proxifiedClass;
        } else {
            String className = proxifiedClassName.substring(0, i);
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.error("Unable to deproxify CGLIB proxy : " + className + " not found !", e);
                return null;
            }
        }

    }

    public static ViewPropertyValue getViewPropValueFacet(Object targetObject, Class targetObjectClass, Object propVal, Class propClass, HttpServletRequest request) {
        // do we have a property-specific facet ?
        WebFacets wf = WebFacets.get(request);
        PropertyDescriptor pd = (PropertyDescriptor) request.getAttribute("propertyDescriptor");
        String propName = pd.getName();
        String facetName = "viewPropertyValue_" + propName;
        ViewPropertyValue facet =
            (ViewPropertyValue) wf.getFacet(facetName, targetObject, targetObjectClass, request);
        if (facet == null) {
            // no property-specific facet, find type-specific one...
            facet = (ViewPropertyValue)wf.getFacet("viewPropertyValue", propVal, propClass, request);
        }
        return facet;
    }

     public static ViewPropertyValue getEditPropValueFacet(Object targetObject, Class targetObjectClass, Object propVal, Class propClass, HttpServletRequest request) {
        // do we have a property-specific facet ?
        WebFacets wf = WebFacets.get(request);
        PropertyDescriptor pd = (PropertyDescriptor) request.getAttribute("propertyDescriptor");
        String propName = pd.getName();
        String facetName = "editPropertyValue_" + propName;
        ViewPropertyValue facet =
            (EditPropertyValue) wf.getFacet(facetName, targetObject, targetObjectClass, request);
        if (facet == null) {
            // no property-specific facet, find type-specific one...
            facet = (ViewPropertyValue)wf.getFacet("editPropertyValue", propVal, propClass, request);
        }
        return facet;
    }
}
