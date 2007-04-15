package net.sourceforge.jfacets.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.jfacets.IFacetContext;

/**
 * The web facet context : a web-enabled extension of the facet context which provides 
 * access to :
 * <ul>
 * 	<li>the HTTP request</li>
 * 	<li>the HTTP response</li>
 * 	<li>the servlet context</li>
 * </ul>
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 * 
 */
public interface IWebFacetContext extends IFacetContext {

	public HttpServletRequest getRequest();
	
	public HttpServletResponse getResponse();
	
	public ServletContext getServletContext();
	
}
