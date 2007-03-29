package net.sf.woko.actions;

import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;

import org.apache.log4j.Logger;

/**
 * This action is the even handler for browsing objects.  
 * It invokes facets for the requested target object (obtained 
 * either from a request attribute if any, or from id/class request 
 * parameters) in order to render it. 
 */
@UrlBinding("/@view/:className/:id")
public class ViewActionBean extends BaseActionBean {
	
	/** the key for binding the target object to the request */
	public static final String KEY_TARGET = "targetObject";
	/** the key for binding the target object's class to the request */
	public static final String KEY_TARGET_CLASS = "targetObjectClass";
	
	/** name of the JSP to dispatch to */
	public static final String JSP_BROWSE = "/WEB-INF/woko/browse.jsp";
	
	private static final Logger logger = Logger
			.getLogger(ViewActionBean.class);

	/** id of the target object */
	private String id;

	/** fully qualified class name of the target object */
	private String className;

	/**
	 * Default handler, used to browse the target object.
	 * @return
	 */
	@DefaultHandler
    @DontValidate
    @SuppressWarnings("unchecked")
	public Resolution browse() {
		
		// do we have something do display in the request ?
		Object target = getRequest().getAttribute(KEY_TARGET);
		Class targetType = (Class)getRequest().getAttribute(KEY_TARGET_CLASS);
		if (target==null) {		
			
			// validate id and className
			boolean ok = true;
			if (id==null) {
				getContext().getValidationErrors().add("id", new LocalizableError("woko.missing.id"));
				ok = false;
			}
			if (className==null) {
				getContext().getValidationErrors().add("className", new LocalizableError("woko.missing.className"));
				ok = false;
			}
			
			// exit if error
			if (!ok)
				return getContext().getSourcePageResolution();
			
			// get hold of the object using Stripernate
			Object[] objectData = Util.getObjectData(id, className, getContext());
			target = objectData[0];
			targetType = (Class)objectData[1];
			
			if (target==null || targetType==null) {
				String msg = "Unable to convert ID " + id + " for type " + className;
				logger.error(msg);
				return error(msg, new Object[0]);
			}		
		}
		if (targetType==null)
			targetType = target.getClass();
		
		// bind the target object & class to the request
		getRequest().setAttribute(KEY_TARGET, target);
		getRequest().setAttribute(KEY_TARGET_CLASS, targetType);
				
		// retrieve the ViewFeed facet (it auto-binds to the request)
		WebFacets wf = WebFacets.get(getRequest());
		wf.getFacet("viewFeed", target, getRequest());
		
		// forward to JSP
		return new ForwardResolution(JSP_BROWSE);			
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
	
}

