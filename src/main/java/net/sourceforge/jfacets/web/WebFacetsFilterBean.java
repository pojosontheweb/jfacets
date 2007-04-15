package net.sourceforge.jfacets.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Spring bean is invoked by the WebFacetsFilter. 
 * It first obtains the WebObjectsHandler instance for the request, 
 * and sets all necessary attributes. 
 * Then, it binds the WebFacets bean to the current request. 
 * 
 * @author Remi VANKEISBELCK - remi "at" rvkb.com
 */
public class WebFacetsFilterBean implements Filter {
	
	public static final String KEY_REQ_WEBFACETS = "webFacets";
	
	private WebObjectsHolder webObjectsHolder;
	private FilterConfig config;
	private WebFacets webFacets;
	
	public WebFacetsFilterBean(WebObjectsHolder woh, WebFacets wf) {
		this.webFacets = wf;
		this.webObjectsHolder = woh;
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// is all stuff ready ?
		if (webObjectsHolder==null) {
			throw new ServletException("Unable to prepare the WebFacets bean for the request ! The webObjectsHolder bean has not been set : check your Spring config file to see how webObjectsHolder is defined !");
		}
		if (webFacets==null) {
			throw new ServletException("Unable to prepare the WebFacets bean for the request ! The webFacets bean has not been set : check your Spring config file to see how webFacets is defined !");
		}
		
		// feed webObjectsHolder...
		webObjectsHolder.setRequest((HttpServletRequest)request);
		webObjectsHolder.setResponse((HttpServletResponse)response);
		webObjectsHolder.setServletContext(config.getServletContext());
		// bind jFacets bean to the request
		request.setAttribute(KEY_REQ_WEBFACETS, webFacets);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

}
