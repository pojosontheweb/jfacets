package net.sf.woko.facets.write;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.*;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewPropertyValue;
import net.sf.woko.util.Util;

import org.apache.log4j.Logger;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Facet used to render an object's property in edit mode.
 * This facet, when executed, sets the fields that indicate 
 * the rendering infos for this property (readOnly, length, etc.). 
 * You may override this facet to customize the editing of some properties 
 * for your objects and profiles. 
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : editPropertyValue</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="editPropertyValue",profileId="ROLE_WOKO_GUEST")
public class EditPropertyValue extends ViewPropertyValue {

	private static final Logger logger = Logger.getLogger(EditPropertyValue.class);
			
	public static final String KEY_REQ_PROP_INFO = "propInfo";
	
	private boolean idField = false;	
	private boolean requiredField = false;	
	private int maxLen = 30;			
	private boolean generatedValue = false;
	private boolean entity = false;
	boolean isEnum = false;
	private boolean readOnly;
	
	/**
	 *  Problem : subclasses usually return their own JSP fragment
	 * in order to render the property. In some cases, this implementation 
	 * should be preferred, and should be used instead of anything overriden 
	 * for example for readOnly stuff where you use viewXxx
	 */
	protected boolean originalResultPreferred = false;
	
	private Object handleReadOnly(PropertyDescriptor currentProp) {
		originalResultPreferred = true;
        Object targetObject = getRequest().getAttribute("targetObject");
        return  Util.getViewPropValueFacet(
                    targetObject,
                    targetObject.getClass(),
                    getContext().getTargetObject(),
                    currentProp.getPropertyType(),
                    getContext().getRequest()).execute();


//                WebFacets.get(getRequest()).execFacet(
//				"viewPropertyValue",
//				getContext().getTargetObject(),
//				currentProp.getPropertyType(),
//				getRequest());
	}
	
	/**
	 * Computes infos about the property (read-only, length, etc.) and 
	 * return the path to the JSP fragment to be used.
	 */
	@SuppressWarnings("unchecked")
	public Object execute() {
		
		// is the property read-only ?
		
		PropertyDescriptor currentProp = (PropertyDescriptor)getRequest().getAttribute("propertyDescriptor");
		if (currentProp.getWriteMethod()==null ) {
			
			// no write method,
			// let's simply render it !
			readOnly = true;
						
		} else {

            Class propClass = currentProp.getPropertyType();

            // is the property an entity ?
            entity = Util.isEntity(propClass);

            // check annotations for Id, GenValue, etc...

            String propName = currentProp.getName();
			Object targetObject = getRequest().getAttribute("targetObject");
			Class clazz = targetObject.getClass();
			Field[] fields = Util.getFields(clazz);
			Field f = null;
            for (int i = 0; (i<fields.length && f==null); i++) {
				if (fields[i].getName().equals(propName)) {
                    f = fields[i];
                }
            }
            Method readMethod = currentProp.getReadMethod();

            idField = Util.hasAnnotOnFieldOrGetter(Id.class, f, readMethod);
            generatedValue = Util.hasAnnotOnFieldOrGetter(GeneratedValue.class, f, readMethod);
            requiredField = Util.hasAnnotOnFieldOrGetter(NotNull.class, f, readMethod);
            Length lengthAnnot = (Length)Util.getAnnotOnFieldOrGetter(Length.class, f, readMethod);
            if (lengthAnnot!=null) {
                maxLen = lengthAnnot.max();
            }
            isEnum = propClass.isEnum();

            // is it an unmidifiable x-to-many ?
            OneToMany otm = (OneToMany)Util.getAnnotOnFieldOrGetter(OneToMany.class, f, readMethod);
            if (otm!=null && otm.mappedBy()!=null)
                readOnly = true;
            ManyToMany mtm = (ManyToMany)Util.getAnnotOnFieldOrGetter(ManyToMany.class, f, readMethod);
            if (mtm!=null && !mtm.mappedBy().equals("")) {
                readOnly = true;
			}

            // is the object fresh (then we don't allow to edit associations) ?
            ManyToOne mto = (ManyToOne)Util.getAnnotOnFieldOrGetter(ManyToOne.class, f, readMethod);
            if (otm!=null || mtm!=null || mto!=null) {
                boolean isFresh = getContext().getRequest().getParameter("isFresh") != null;
                if (isFresh)
                    readOnly = true;
            }
        }
		if (readOnly)
			return handleReadOnly(currentProp);
		
		if (entity)
			return "/WEB-INF/woko/fragments/write/simpletypes/prop-entity.jsp";

		if (isEnum)
			return "/WEB-INF/woko/fragments/write/simpletypes/prop-enum.jsp";

		// don't know what to do : defaults to toString() !
		return "/WEB-INF/woko/fragments/write/property-value-default.jsp";
	}

	public boolean isGeneratedValue() {
		return generatedValue;
	}

	public boolean isIdField() {
		return idField;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public boolean isRequiredField() {
		return requiredField;
	}

	public boolean isEntity() {
		return entity;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean isOriginalResultPreferred() {
		return originalResultPreferred;
	}
}