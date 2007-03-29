package net.sf.woko.facets;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.web.IWebFacetContext;

/**
 * Base class for facets used in woko. Binds itself to 
 * the request when the facet context is set, with the name 
 * "facet" and also with the facet's name (as defined in the class' FacetKey
 * annotation).
 */
public class BaseFacet implements IFacet {

	/** the key for binding the facet to the request */ 
	public static final String KEY_REQ_FACET = "facet";
	
	private IWebFacetContext context;
	
	public IWebFacetContext getContext() {
		return context;
	}

	/**
	 * Facet bound to request automatically using requested facet name 
	 * and "facet" keys.
	 */
	public void setContext(IFacetContext context) {
		this.context = (IWebFacetContext)context;
		bindToRequest(getContext().getFacetName());
		bindToRequest(KEY_REQ_FACET);
	}
	
	/** utility methods */
	
	public void bindToRequest(String name) {
		getRequest().setAttribute(name, this);
	}
	
	public HttpServletRequest getRequest() {
		return getContext().getRequest();
	}
		
	public Object getTargetObject() {
		return getContext().getTargetObject();
	}
	
}
