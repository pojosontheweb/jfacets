package net.sourceforge.jfacets.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.jfacets.log.JFacetsLogger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Intercepts HTTP requests and associates a jFacets bean to them. This filter is 
 * used to inject the web stuff (request, response and servlet context) into tje system 
 * for each request, so that we can have web-enabled contexts in facets.<br/>
 * It loads descriptors at init, and then provides one new instance of JFacets for each 
 * request.
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public class WebFacetsFilter implements Filter {
	
	private static final JFacetsLogger logger = JFacetsLogger.getLogger(WebFacetsFilter.class);

	private String appCtxName = "webFacetsAppCtx.xml";
	private ApplicationContext applicationContext;
	private FilterConfig config;
	
	/**
	 * Initializes the filter. 
	 * Lookups for a config param with name 'appCtxName' to load the JFacets 
	 * bean factory from (defaults to 'webFacetsAppCtx.xml').
	 */
	public void init(FilterConfig config) throws ServletException {
		if (logger.isInfoEnabled()) logger.info("Initializing WebFacetsFilter...");
		this.config = config;
		
		// Spring context loading
		// ----------------------
		
		// first, do we load from a specific classpath context specified 
		// in web.xml ?
		
		String ctxName = config.getInitParameter("appCtxName");
		if (ctxName!=null) {
			appCtxName = ctxName;
			if (logger.isDebugEnabled()) logger.debug("Creating bean factory from CLASSPATH resource '" + appCtxName + "'..." );
			applicationContext = new ClassPathXmlApplicationContext(
		        new String[] {appCtxName});
			if (applicationContext==null) {
				String msg = "Unable to find '" + appCtxName + "' context (as specified in the WebFacetsFilter configuration in web.xml) from the CLASSPATH !";
				initError(msg);
			} else
				if (logger.isInfoEnabled()) logger.info("Spring context '" + appCtxName + "' loaded from CLASSPATH");
		} else {
			// no context specified, do we use Spring context loader ?
			applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
			if (applicationContext!=null) {
				if (logger.isInfoEnabled()) logger.info("ContextLoader-loaded Spring context obtained OK");	
			} else {
				// no loaded context, use defaults !
				applicationContext = new ClassPathXmlApplicationContext(
				        new String[] {appCtxName});
				if (applicationContext==null) {
					String msg = "Unable to find default '" + appCtxName + "' context, context loader not used, and no context name supplied in web.xml !";
					initError(msg);
				} else
					if (logger.isInfoEnabled()) logger.info("Default Spring context '" + appCtxName + "' loaded from CLASSPATH (no context loader is used, and no context name provided in web.xml)");
			}
		}
			
		if (logger.isInfoEnabled()) logger.info("... filter init OK" );			
	}
	
	private void initError(String msg) throws ServletException {
		logger.fatal(msg);
		throw new ServletException(msg);		
	}

	/**
	 * Filters the request. 
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (logger.isDebugEnabled()) logger.debug("doFilter() : handling request...");
		
		// delegate to the filter bean
		WebFacetsFilterBean filterBean = 
			(WebFacetsFilterBean)applicationContext.getBean("webFacetsFilterBean");
		filterBean.init(config);
		filterBean.doFilter(request, response, chain);
	}

	/** does nothing */
	public void destroy() {
	}

	/**
	 * Return the WebFacets instance for passed request 
	 * @deprecated use WebFacets.get(request) or WebFacets.get() instead !
	 */
	public static WebFacets getJFacets(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
