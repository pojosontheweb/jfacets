package net.sourceforge.jfacets.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.impl.DefaultFacetContext;

/**
 * Default web facet context implementation
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public class DefaultWebFacetContext extends DefaultFacetContext implements IWebFacetContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;
	
	public DefaultWebFacetContext(String facetName, 
			IProfile profile, 
			Object targetObject,
			FacetDescriptor facetDescriptor,
			HttpServletRequest request,
			HttpServletResponse response,
			ServletContext servletContext) {
		super(facetName, profile, targetObject, facetDescriptor);
		this.request = request;
		this.response = response;
		this.servletContext = servletContext;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String toString() {
		return "[DefaultWebFacetContext facetName=" + getFacetName() +
			", profileId=" + getProfile().getId() + ", targetObject=" + getTargetObject() +
			", request=" + getRequest() + ", response=" + getResponse() + ", ctx=" + getServletContext() + "]";
	}
	
}
