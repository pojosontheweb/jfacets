package net.sf.woko.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.woko.search.CompassUtil;

/**
 * A very crappy filter that loads compass when the first request comes in !
 * The problem is that we need a request to open the hb session so that 
 * compass' hb3 device works :-/
 * <br/>
 * <b>TODO</b> find a better solution to this problem !
 */
public class CompassInitFilter implements Filter {
	
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(CompassInitFilter.class);	

	private boolean initialized = false;
	
	public void destroy() {
		CompassUtil.shutdown();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!initialized) {
			// init compass...
			logger.info("Compass not yet ready, initializing it...");
			CompassUtil.initialize();
			initialized = true;
			logger.info("... done, compass up and ready !");			
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig cfg) throws ServletException {
		logger.info("Filter created and initialized");
	}

}
