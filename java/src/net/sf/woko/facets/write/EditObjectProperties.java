package net.sf.woko.facets.write;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewObjectProperties;
import net.sf.woko.util.Util;

/**
 * Facet used to render the properties of the target object in edit-mode.
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : editProperties</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="editProperties",profileId="ROLE_WOKO_GUEST")
public class EditObjectProperties  extends ViewObjectProperties {
		
	/** the id of the object */
	private String id;
	/** the className of the object */
	private String className;
	/** a list of booleans indicating which props are read-only */
	private List<Boolean> writeAllowed;
	
	/**
	 * set id and className from target object, and return the 
	 * path to the fragment to be used for rendering the 
	 * properties :
	 * <code>/WEB-INF/woko/fragments/write/properties.jsp</code>
	 */
	public Object execute() {
		super.execute();
		// bind id and class name to request for use in JSP
		Object targetObject = getContext().getTargetObject();
		id = Util.getId(targetObject);
		className = targetObject.getClass().getName();
        getRequest().setAttribute("targetObject", targetObject);
        // return view name
		return "/WEB-INF/woko/fragments/write/properties.jsp";
	}
	
	/**
	 * return a list of Boolean that has the same size 
	 * than the one returned by getPropertyNames with 
	 * the write mode (writable or not).
	 * Uses getReadOnlyPropertyNames() to set the values.
	 */
	public final List<Boolean> getWriteAllowed() {
		if (writeAllowed==null) {
			writeAllowed = new ArrayList<Boolean>();
			List<String> readOnlyPropNames = getReadOnlyPropertyNames();
			if (readOnlyPropNames==null)
				readOnlyPropNames = new ArrayList<String>();
			for (String pName : getPropertyNames()) {
				if (readOnlyPropNames.contains(pName))
					writeAllowed.add(Boolean.FALSE);
				else
					writeAllowed.add(Boolean.TRUE);
			}
		}
		return writeAllowed;
	}
	
	/**
	 * Return a list of read-only property names.
	 * In this implementation, returns null : all props are writable. 
	 * You may override this method in your facets in order to disable 
	 * the editing of some props for some objects and some profiles.
	 */
	public List<String> getReadOnlyPropertyNames() {
		return null;
	}

	public String getClassName() {
		return className;
	}

	public String getId() {
		return id;
	}
	
}