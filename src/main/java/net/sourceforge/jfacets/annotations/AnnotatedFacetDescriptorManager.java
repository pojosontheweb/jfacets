package net.sourceforge.jfacets.annotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.log.JFacetsLogger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * The AnnotatedFacetDescriptorManager : loads and manages descriptors for facets 
 * using the @FacetKey annotation. <br/>
 * This manager scans the classpath in order to find classes that implement 
 * the IFacet interface and are marked with a @FacetKey annotation. If found, 
 * a descriptor is crated and added for this facet, based on the information 
 * supplied in the annotation's attributes. 
 * <br/>
 * <b>IMPORTANT : </b>You <b>HAVE</b> to provide at least one base package when 
 * creating this objects.
 * 
 * @author Remi VANKEISBELCK - remi@rvkb.com
 *
 */
public class AnnotatedFacetDescriptorManager implements IFacetDescriptorManager, ApplicationContextAware {

	private static final String SEPARATOR = "/";

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(AnnotatedFacetDescriptorManager.class); 
		
	/** base packages list to search facet scripts in */
	private List<String> basePackages;
	/** the Spring app context, used to search resources */
	private ApplicationContext springContext;
	/** the list of loaded descriptors */
	private ArrayList<FacetDescriptor> descriptors = new ArrayList<FacetDescriptor>();

	private int nbDesc = 0;
	
	/**
	 * Create the manager and set base packages.
	 * @param basePackages
	 */
	public AnnotatedFacetDescriptorManager(List<String> basePackages) {
		this.basePackages = basePackages;
	}
	
	public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
		springContext = appCtx;
	}

	/**
	 * Convert a file name to a class name
	 */
	private static String filenameToClassname(final String filename) {
		return filename.substring(0, filename.lastIndexOf(".class")).replace(
				'/', '.').replace('\\', '.');
	}
	
	/**
	 * Loads all available Facet Descriptors by scanning the CLASSPATH with 
	 * specified base packages.
	 */	
	@SuppressWarnings("unchecked")
	public void initialize() {		
		for (String pkgBase : basePackages) {
			// convert pkb base to path
			pkgBase = SEPARATOR + pkgBase.replace('.', '/');
			// try to find descriptors in this package...
			String query = "classpath*:" + pkgBase + "/**/*.class";
			if (logger.isDebugEnabled()) logger.debug("Scanning CLASSPATH entries : " + query);
			Resource[] facetFiles;
			try {
				facetFiles = springContext.getResources(query);				
			} catch(IOException e) {
				String message = "Unable to lookup for resources using Spring Context !";
				logger.error(message, e);
				throw new RuntimeException(message, e);
			}
				
			// iterate on resources and load appropriate classes for reflection...  
			for (int i = 0; i < facetFiles.length; i++) {
				Resource r = facetFiles[i];
				String className = null;
				try {
					// compute fqcn based on resource path + pkg base
					String fullPath = r.getURL().getFile();
					int startIndex = fullPath.indexOf(pkgBase);
					String fileName = fullPath.substring(startIndex + 1);
					className = filenameToClassname(fileName);
					if (logger.isDebugEnabled()) logger.debug("Converted resource '" + r + "' to class name '" + className + "'");
					
					// load that class
					Class clazz = Class.forName(className);
								
					FacetKeyList fkl = (FacetKeyList)clazz.getAnnotation(FacetKeyList.class);
					if (fkl!=null) {
						
						// multiple facet keys
						// -------------------							
						FacetKey[] facetKeys = fkl.keys();
						for (int j = 0; j < facetKeys.length; j++)
							createDescriptorForAnnotation(facetKeys[j], clazz);
													
					} else {						
						// no multiple key, try single one...
						FacetKey annot = (FacetKey)clazz.getAnnotation(FacetKey.class);						
						if (annot!=null) {		
							
							// single facet key
							// ----------------								
							createDescriptorForAnnotation(annot, clazz);
							
						} else {
							// not annotated
							if (logger.isDebugEnabled()) logger.debug("Skipped class " + clazz + ", does not have the @FacetKey annot");
						}
					}
					
				} catch(ClassNotFoundException cnfex) {
					logger.error("Error while trying to load annotated facet from class : " + className, cnfex);
				} catch(IOException e) {
					String message = "IOException caught while trying to access facet file '" + facetFiles[i] + "', facet file skipped";
					logger.error(message, e);
					throw new RuntimeException(message, e);
				}
			}						
		}
		if (logger.isInfoEnabled()) logger.info("init OK, " + nbDesc + " descriptors loaded");
	}	
	
	/**
	 * Create a descriptor for passed FacetKey and facet class and adds it to the descriptors list
	 * @param annot The FacetKey annotation
	 * @param facetClass The facet implementation class
	 * @return The freshly created descriptor if ok, null if error (missing infos in the facet key)
	 */
	private FacetDescriptor createDescriptorForAnnotation(FacetKey annot, Class facetClass) {
		// annotated, create and add descriptor
		String name = annot.name();
		String profileId = annot.profileId();
		Class targetObjectType = annot.targetObjectType();							
		if (name!=null && profileId!=null && targetObjectType!=null) {		
			FacetDescriptor descriptor = new FacetDescriptor();
			descriptor.setName(name);
			descriptor.setProfileId(profileId);
			descriptor.setTargetObjectType(targetObjectType);
			descriptor.setFacetClass(facetClass);	
			descriptors.add(descriptor);		
			nbDesc++;
			if (logger.isDebugEnabled()) logger.debug("Created and added descriptor " + descriptor + " for annotated facet class " + facetClass);			
			return descriptor;
		} else {
			// missing annot attributes
			logger.warn("Class " + facetClass + " has the @FacetKey annot but some attributes are missing ! name, profileId and targetObjectType are required");
			return null;
		}		
	}
	
	/**
	 * Return the descriptor sctrictly associated to passed params if any, 
	 * null of not found.
	 */
	public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
		FacetDescriptor res = null;
		for (FacetDescriptor d : descriptors) {
			if (d.getName().equals(name) &&
					d.getProfileId().equals(profileId) &&
					d.getTargetObjectType().equals(targetObjectType)) {
				res = d;
				break;
			}
		}
		return res;
	}

	/**
	 * Return all descriptors in an array.
	 */
	public FacetDescriptor[] getDescriptors() {
		FacetDescriptor[] res = new FacetDescriptor[descriptors.size()];
		res = (FacetDescriptor[])descriptors.toArray(res);
		return res;
	}


}
