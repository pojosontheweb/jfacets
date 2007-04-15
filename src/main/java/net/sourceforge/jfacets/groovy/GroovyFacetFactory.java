package net.sourceforge.jfacets.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

import java.io.File;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetFactory;

import org.apache.log4j.Logger;

/**
 * The GroovyFacetFactory, responsible of creating Groovy Facets. 
 * <br/>
 * A fallback factory can be specified, in case you want to use Groovy and 
 * regular facets in the same app.
 */
public class GroovyFacetFactory implements IFacetFactory {

	private static final Logger logger = 
		Logger.getLogger(GroovyFacetFactory.class);
	
	private static GroovyClassLoader groovyClassLoader;
	
	private IFacetFactory fallbackFactory;
	
	private GroovyClassLoader getGroovyClassLoader() {
		if (groovyClassLoader==null) 
			groovyClassLoader = new GroovyClassLoader();
		return groovyClassLoader;
	}
	
	/**
	 * Creates a GroovyFacet for passed descriptor. 
	 * Depending on what's in the script (a facet class declaration 
	 * or just the execute script), creates and return the 
	 * appropriate facet.
	 */
	public Object createFacet(FacetDescriptor d) {
		if (logger.isDebugEnabled()) logger.debug("about to create facet for descriptor " + d);
		if (d instanceof GroovyFacetDescriptor) {
			
			// groovy facet
			// ------------
			
			GroovyFacetDescriptor gfd = (GroovyFacetDescriptor)d;
			File scriptFile = gfd.getScriptFile();
			// try to see if we have a facet definition in there (a 
			// class declaration), or just a script.
			if (logger.isDebugEnabled()) logger.debug("Trying to parse groovy class from file " + scriptFile.getAbsolutePath());
			Object res = parseFacetClass(scriptFile);
			if (res!=null) {
				// script file contains a valid class def, we return it
				if (logger.isDebugEnabled()) logger.debug("Facet found and created from IFacet class in file " + scriptFile.getAbsolutePath() + ", returning " + res);
			} else {
				// script does not contain a valid class, try a script wrapper
				GroovyScriptWrapperFacet wrapperFacet = new GroovyScriptWrapperFacet();
				wrapperFacet.setScriptFile(scriptFile);
				res = wrapperFacet;
				if (logger.isDebugEnabled()) logger.debug("script wrapper facet created OK for file '" + scriptFile.getAbsolutePath() + ", returning " + res);
			}
			return res;
						
		} else {
			
			// regular pure java facet
			// -----------------------
			
			if (fallbackFactory==null) {
				logger.error("Received descriptor is not a Groovy one (" + d + "), and we have " +
						"no fallback factory. Returning null !");
				return null;
			} else {
				if (logger.isDebugEnabled()) logger.debug("Received descriptor isn't a valid Groovy one : " + d + ", try the fallback factory...");
				Object res = fallbackFactory.createFacet(d);
				if (logger.isDebugEnabled()) logger.debug("Facet created using fallback factory, returning " + res);
				return res;
			}
		}
	}
	
	/**
	 * Tries to parse and create a facet from supplied file. Return the facet 
	 * is it's a valid class declaration, or null if it's not a valid type (a script only 
	 * for example).
	 */
	private Object parseFacetClass(File facetFile) {
		Object res = null;
		try {
			if (logger.isDebugEnabled()) logger.debug("Trying to parse groovy class from file " + facetFile.getAbsolutePath() + "...");
			Class groovyClass = getGroovyClassLoader().parseClass(facetFile);
			if (groovyClass==null) {
				if (logger.isDebugEnabled()) logger.debug("Class parsed is null, return null");
			} else {
				// does the class define a plain script ?
				if (Script.class.isAssignableFrom(groovyClass)) {
					// plain script, return null...
					if (logger.isDebugEnabled()) logger.debug("Groovy Class loaded OK : " + groovyClass + ",  it's a plain script, returning null");
					return null;
				} else {
					if (logger.isDebugEnabled()) logger.debug("Groovy Class loaded OK : " + groovyClass + ", checking if it's a facet...");
					res = groovyClass.newInstance();
					if (logger.isDebugEnabled()) logger.debug("Groovy facet created, returning " + res);					
				}
			}
		} catch(Exception e) {
			String message = "error while parsing or instanciating the facet in file " + facetFile.getAbsolutePath();
			logger.error(message, e);
		}
		return res;
	}

	public IFacetFactory getFallbackFactory() {
		return fallbackFactory;
	}

	public void setFallbackFactory(IFacetFactory fallbackFactory) {
		this.fallbackFactory = fallbackFactory;
	}

}
