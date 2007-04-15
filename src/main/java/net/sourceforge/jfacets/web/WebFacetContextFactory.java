package net.sourceforge.jfacets.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IProfile;

/**
 * The web context factory : creates web-enabled facet contexts
 * 
 * @see net.sourceforge.jfacets.web.IWebFacetContext
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public class WebFacetContextFactory implements IFacetContextFactory {

	private static final Logger logger = Logger.getLogger(WebFacetContextFactory.class);
	
	private WebObjectsHolder rrh;
	
	public WebFacetContextFactory(WebObjectsHolder requestResponseHolder) {
		this.rrh = requestResponseHolder;
	}
	
	/**
	 * Create a {@link DefaultWebFacetContext} context using current request, response and servletcontext, and null facetDescriptor
	 * @deprecated use the one that takes the facetDescriptor as the 4th param
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject) {
		return create(facetName, profile, targetObject, null);
	}
	
	/**
	 * Create a {@link DefaultWebFacetContext} context using current request, response and servletcontext
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
		DefaultWebFacetContext ctx = 
			new DefaultWebFacetContext(facetName, profile, targetObject, facetDescriptor, rrh.getRequest(), rrh.getResponse() , rrh.getServletContext());
		if (logger.isDebugEnabled()) logger.debug("Context created OK,  returning " + ctx);
		return ctx;
	}

	public HttpServletRequest getRequest() {
		return rrh.getRequest();
	}

	public HttpServletResponse getResponse() {
		return rrh.getResponse();
	}

	public ServletContext getServletContext() {
		return null;
	}

}
