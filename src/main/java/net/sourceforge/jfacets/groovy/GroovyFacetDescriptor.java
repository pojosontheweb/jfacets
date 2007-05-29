package net.sourceforge.jfacets.groovy;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

import net.sourceforge.jfacets.FacetDescriptor;

/**
 * An extension of the FacetDescriptor class for 
 * GroovyFacets, with an additional <code>scriptFile</code> property 
 * that points to the Groovy Script file.
 */
public class GroovyFacetDescriptor extends FacetDescriptor {
	
	private static final Logger logger = Logger.getLogger(GroovyFacetDescriptor.class);
	
	private static GroovyClassLoader groovyClassLoader = new GroovyClassLoader(); 
	private Class facetClass;

	/** the groovy script file for this descriptor */
	private File scriptFile;

	/** return the groovy script file for this descriptor */
	public File getScriptFile() {
		return scriptFile;
	}

	/** set the groovy script file for this descriptor */
	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}
	
	
	@Override
	public Class getFacetClass() {
		if (facetClass==null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Class not yet parsed, parsing from file " + scriptFile.getAbsolutePath() + ", I am " + this);
			}
			try {
				facetClass = groovyClassLoader.parseClass(scriptFile);
			} catch (CompilationFailedException e) {
				logger.warn("Compilation failed, file=" + scriptFile.getAbsolutePath() + ", I am " + this, e);
			} catch (IOException e) {
				logger.warn("Caught an IOException while compiling from file " + scriptFile.getAbsolutePath());
			}
		}
		return facetClass;
	}

	public String toString() {
		return "{GroovyFacetDescriptor name=" + getName() + ", profileId=" + getProfileId() + ", targetObjectType=" + getTargetObjectType() + ", scriptFile=" + scriptFile.getAbsolutePath() + "]";
	}
	
}
