package net.sourceforge.jfacets.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Base class for WebFacets JSP tags
 * 
 * @author VANKEISBELCK Remi - remi 'at' rvkb.com
 */
public class FacetTagSupport extends TagSupport {
	
	private static final long serialVersionUID = 1L;	

	private String name;
	private String profileId;
	private Class targetObjectClass;
	private Object targetObject;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public Class getTargetObjectClass() {
		return targetObjectClass;
	}

	public void setTargetObjectClass(Class targetObjectClass) {
		this.targetObjectClass = targetObjectClass;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
		if (targetObject!=null)
			setTargetObjectClass(targetObject.getClass());
	}

	/**
	 * Checks if name has been supplied, and does preliminary 
	 * initialization work (set to Object.class if no target type and 
	 * target object supplied) 
	 */
	protected void checkAttributes() throws JspException {
		if (name==null)
			throw new JspException("name attribute can't be null");
		if (targetObject==null && targetObjectClass==null) {
			// use root "java.lang.Object" type if nothing supplied 
			targetObjectClass = Object.class;
		}

		// do we use an instance or a class name for the target object ?		
		if (targetObjectClass==null)
			targetObjectClass = targetObject.getClass();		
	}
	
	/**
	 * Set all tag attributes to null.
	 */
	protected void resetAttributes() {
		name = null;
		profileId = null;
		targetObject = null;
		targetObjectClass = null;
	}
	
}
