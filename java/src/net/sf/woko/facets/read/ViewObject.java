package net.sf.woko.facets.read;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.web.WebFacets;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.facets.write.SaveObject;
import net.sf.woko.facets.write.DeleteObject;
import net.sf.woko.util.Util;

/**
 * Top-level facet for Woko browsing : used to render the target object read-only.<br/>
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : view</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="view",profileId="ROLE_WOKO_USER",targetObjectType=Object.class)
public class ViewObject extends BaseFacet implements IExecutable {

	/** request attribute key to bind the browsed object */
	public static final String KEY_REQ_TARGET_OBJECT = "targetObject";
	/** request attribute key to bind the browsed object's ID */
	public static final String KEY_REQ_TARGET_OBJECT_ID = "targetObjectId";
	/** request attribute key to bind the browsed object's Class */
	public static final String KEY_REQ_TARGET_OBJECT_CLASS = "targetObjectClass";
		
	/** the browsed object's ID */
	private String id;
	/** the browsed object's class name */
	private String className;
	
	/**
	 * Computes id and className, sets the request attributes and 
	 * returns the path to the JSP fragment to be used for displaying the object :
	 * <code>/WEB-INF/woko/fragments/read/view.jsp</code>. 
	 */
	public Object execute() {
		// set Id and ClassName
		Object target = getTargetObject();
		id = Util.getId(target);
		Class clazz = target.getClass();
		className = clazz.getName();
		getRequest().setAttribute(KEY_REQ_TARGET_OBJECT, target);
		getRequest().setAttribute(KEY_REQ_TARGET_OBJECT_ID, id);
		getRequest().setAttribute(KEY_REQ_TARGET_OBJECT_CLASS, clazz);
		return "/WEB-INF/woko/fragments/read/view.jsp";
	}

	/**
	 * Return true if targetObject has the Entity annotation
	 */
	public boolean isEditable() {
        WebFacets wf = WebFacets.get(getRequest());
        SaveObject saveFacet = (SaveObject)wf.getFacet("saveObject", getTargetObject(), getRequest());
        if (saveFacet!=null) {
            return saveFacet.isSaveEnabled();
        } else {
            // SaveObject facet isn't defined, return false !
            return false;
        }
    }

    public boolean isDeletable() {
        WebFacets wf = WebFacets.get(getRequest());
        DeleteObject deleteFacet =
                (DeleteObject)wf.getFacet("deleteObject", getTargetObject(), getRequest());
        if (deleteFacet!=null) {
            return deleteFacet.isDeleteEnabled();
        } else {
            // SaveObject facet isn't defined, return false !
            return false;
        }
    }

    public String getClassName() {
		return className;
	}

    public String getShortClassName() {
        if (className!=null) {
            return Util.stripPackageName(className);
        } else {
            return null;
        }
    }

    public String getId() {
		return id;
	}

    public Class getTargetObjectClass() {
        if (getTargetObject()!=null) {
            return getTargetObject().getClass();
        } else {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }
}
