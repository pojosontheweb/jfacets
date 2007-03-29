package net.sf.woko.facets;

import groovy.lang.GroovyClassLoader;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sf.woko.util.IWokoInternal;

import static com.mongus.beans.validation.BeanValidator.validate;

@Entity
public class WokoDbGroovyFacetDescriptor extends FacetDescriptor implements IWokoInternal {

	private static final Logger logger = Logger.getLogger(WokoDbGroovyFacetDescriptor.class);
	
	private final static transient GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Boolean active;
	
	@NotNull
	private String name;
	
	@NotNull
	private String profileId;
		
	@Length(max=99999)
	private String sourceCode;

	@NotNull
	private String targetObjectClassName;
	
	private transient Class groovyClass;
	
	private boolean loadGroovyClass() {
		// parse groovy class...
		if (sourceCode!=null) {
			try {
				logger.debug("Trying to parse groovy class (I am DB/Groovy descriptor " + this + ")");
				groovyClass = groovyClassLoader.parseClass(sourceCode);
                if (groovyClass!=null) {
                    logger.debug("Groovy class parsed : " + groovyClass);
				    return true;
                } else {
                    logger.debug("Unable to parse Groovy class");
				    return false;
                }
			} catch(CompilationFailedException e) {
				logger.warn("Compilation failed ! (I am DB/Groovy descriptor " + this + ")", e);
				return false;
			}
		} else {
			logger.warn("Nothing to compile, script is null ! (I am DB/Groovy descriptor " + this + ")");
			return false;
		}			
	}
	
	public Boolean getCompiling() {
        if (groovyClass==null && sourceCode!=null)
			return loadGroovyClass();
		else
			return true;
	}
	
	/**
	 * Create and return the facet.
	 */
	public Object getFacet() {
        Object facet = null;
        if (getCompiling()) {
            // instanciate the facet if possible
            try {
                facet = groovyClass.newInstance();
                logger.debug("Groovy Facet created OK, returning " + facet + " (I am DB/Groovy descriptor " + this + ")");
            } catch (Exception e) {
                logger.error("Unable to create facet using no-args constructor, I am DB/Groovy descriptor " + this, e);
            }
        } else {
            logger.warn("Facet does not compile ! (I am DB/Groovy descriptor " + this + ")");
        }
		return facet;
	}

	@Override
	public Class getTargetObjectType() {
		if (targetObjectClassName!=null) {
			try {
                return Class.forName(targetObjectClassName);
			} catch (ClassNotFoundException e) {
				return null;
			}			
		} else 
			return null;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		validate(name);
		this.name = name;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		validate(profileId);
		this.profileId = profileId;
	}
	
	public String getTargetObjectClassName() {
		return targetObjectClassName;
	}

	public void setTargetObjectClassName(String targetObjectClassName) {
		validate(targetObjectClassName);
		this.targetObjectClassName = targetObjectClassName;
	}	
	
	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActive() {
		if (active==null)
			return Boolean.FALSE;
		return active;
	}

	public void setActive(Boolean a) {
        active = getCompiling() && a;
    }
	
	@Override
	public Class getFacetClass() {
        Object facet = getFacet();
        if (facet != null)
			return facet.getClass();
		else
			return null;
	}

	@Override
	public String toString() {
		return "[WokoDbGroovyFacetDescriptor name="+getName()+
			" profileId=" + getProfileId() + 
			" targetObjectType=" + getTargetObjectType() + 
			" active=" + getActive() + "]";
	}
	
	
	
}
