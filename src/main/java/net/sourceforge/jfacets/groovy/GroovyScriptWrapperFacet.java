package net.sourceforge.jfacets.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

/**
 * A Groovy-enabled executable facet that wraps a Groovy Script 
 * for implementing the <code>execute()</code> method. <br/>
 * You should never use this class when writing Groovy Facets, 
 * it's an internal component of GroovyFacets.
 * <br/>
 * This class serves only for scrips : scripted groovy facets are 
 * used directly.
 */
public class GroovyScriptWrapperFacet implements IFacet, IExecutable {

	private static final Logger logger = 
		Logger.getLogger(GroovyScriptWrapperFacet.class);
	
	/**
	 * The binding name for the facet context, so that it's accessible 
	 * from the Groovy script.
	 */
	public static final String BINDING_FACET_CONTEXT = "context";

	/**
	 * The binding name for the log4j logger, so that it's accessible 
	 * from the Groovy script (in case you want to log stuff easily).
	 */
	public static final String BINDING_LOGGER = "logger";

	private IFacetContext context;	
	private File scriptFile;
	
	private Binding binding;
	private GroovyShell groovyShell;
	
	/** return the Facet context */
	public IFacetContext getContext() {
		return context;
	}

	/** set the Facet context */
	public void setContext(IFacetContext ctx) {
		this.context = ctx;
	}
	
	/**
	 * Executes the GroovyFacet, thereby executing the facet script.
	 */
	public Object execute() {
		if (logger.isDebugEnabled()) logger.debug("Attempting to execute (I am " + this + ")");
		// bind necessary stuff
		binding = new Binding();
		groovyShell = new GroovyShell(binding);
		binding.setVariable(BINDING_FACET_CONTEXT, getContext());
		binding.setVariable(BINDING_LOGGER, logger);
		Object result;
		try {
			result = groovyShell.evaluate(scriptFile);
			if (logger.isDebugEnabled()) logger.debug("execution OK, result=" + result + " (I am " + this + ")");
			return result;
		} catch (Exception e) {
			String message = "Execution of facet " + this + " throws exception !";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	/** return the script file */
	public File getScriptFile() {
		return scriptFile;
	}

	/** assign the script file */
	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}
	
	public String toString() {
		if (getContext()!=null) {
			return "[GroovyFacet name=" + getContext().getFacetName() +
				", profileId=" + getContext().getProfile().getId() +
				", targetObject=" + getContext().getTargetObject() + 
				", scriptFile=" + scriptFile.getAbsolutePath() + "]";
		} else {
			return "[GroovyFacet (context not set) scriptFile=" + scriptFile.getAbsolutePath() + "]";
		}
	}

}
