package net.sourceforge.jfacets.groovy;

import java.io.File;

import net.sourceforge.jfacets.FacetDescriptor;

/**
 * An extension of the FacetDescriptor class for 
 * GroovyFacets, with an additional <code>scriptFile</code> property 
 * that points to the Groovy Script file.
 */
public class GroovyFacetDescriptor extends FacetDescriptor {

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
	
	public String toString() {
		return "{GroovyFacetDescriptor name=" + getName() + ", profileId=" + getProfileId() + ", targetObjectType=" + getTargetObjectType() + ", scriptFile=" + scriptFile.getAbsolutePath() + "]";
	}
	
}
