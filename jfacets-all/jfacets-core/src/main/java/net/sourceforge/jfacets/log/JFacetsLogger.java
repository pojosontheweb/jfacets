package net.sourceforge.jfacets.log;

import org.apache.log4j.Logger;

/**
 * Utility class that centralizes dependencies on the underlying logging system.
 * 
 * @author Remi VANKEISBELCK - remi 'at' rvkb.com
 */
public class JFacetsLogger {
	
	private Logger logger;
	
	protected JFacetsLogger(Logger logger) {
		this.logger = logger;
	}
	
	public static JFacetsLogger getLogger(Class clazz) {
		return new JFacetsLogger(Logger.getLogger(clazz));
	}

	public void debug(Object arg0, Throwable arg1) {
		logger.debug(arg0, arg1);
	}

	public void debug(Object arg0) {
		logger.debug(arg0);
	}

	public void error(Object arg0, Throwable arg1) {
		logger.error(arg0, arg1);
	}

	public void error(Object arg0) {
		logger.error(arg0);
	}

	public void fatal(Object arg0, Throwable arg1) {
		logger.fatal(arg0, arg1);
	}

	public void fatal(Object arg0) {
		logger.fatal(arg0);
	}

	public void info(Object arg0, Throwable arg1) {
		logger.info(arg0, arg1);
	}

	public void info(Object arg0) {
		logger.info(arg0);
	}

	public void warn(Object arg0, Throwable arg1) {
		logger.warn(arg0, arg1);
	}

	public void warn(Object arg0) {
		logger.warn(arg0);
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	
}
