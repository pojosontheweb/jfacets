package net.sourceforge.jfacets.groovy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;

/**
 * The Facet Descriptor Manager for Groovy Facets, which scans the CLASSPATH for 
 * "*.facet" or "*.groovy" Scripts, and creates associated descriptors. <br/>
 * The name, profileId and target type key has to be in the file path, which 
 * has to follow naming conventions :<br/>
 * <pre>
 * / <profile_id> / <target_type(fqcn)> / <facet_name>.facet
 * </pre>
 * <br/>
 * Here is an example :<br/>
 * <pre>/admin/com.xyz.MyClass/View</pre> 
 * maps to the 
 * <pre>(View, admin, com.xyz.MyClass)</pre>
 * facet key.
 * <br/>
 */
public class GroovyFacetDescriptorManager implements IFacetDescriptorManager, ApplicationContextAware {

	private static final String SEPARATOR = "/";
	private static final String FACET_SCRIPT_SUFFIX1 = ".facet";
	private static final String FACET_SCRIPT_SUFFIX2 = ".groovy";

	private static final Logger logger = Logger.getLogger(GroovyFacetDescriptorManager.class); 
		
	/** base packages list to search facet scripts in */
	private List<String> basePackages;
	/** the Spring app context, used to search resources */
	private ApplicationContext springContext;
	/** the list of loaded descriptors */
	private ArrayList<FacetDescriptor> descriptors = new ArrayList<FacetDescriptor>();
	
	/**
	 * Create the manager and set base packages.
	 * @param basePackages
	 */
	public GroovyFacetDescriptorManager(List<String> basePackages) {
		this.basePackages = basePackages;
	}
	
	/** injected Spring context */
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		springContext = ctx;		
	}
	
	/**
	 * Loads all available Groovy Facet Descriptors by scanning the CLASSPATH with 
	 * specified base packages.
	 */	
	public void initialize() {
		int nbDesc = 0;
		for (String pkgBase : basePackages) {
			// convert pkb base to path
			pkgBase = SEPARATOR + pkgBase.replace('.', '/');
			// try to find descriptors in this package...
			Resource[] facetFiles;
			try {
				String query1 = "classpath*:" + pkgBase + "/**/*" + FACET_SCRIPT_SUFFIX1;
				if (logger.isDebugEnabled()) logger.debug("Scanning CLASSPATH entries : " + query1);
				Resource[] resources1 = springContext.getResources(query1);	
				String query2 = "classpath*:" + pkgBase + "/**/*" + FACET_SCRIPT_SUFFIX2;
				if (logger.isDebugEnabled()) logger.debug("Scanning CLASSPATH entries : " + query2);
				Resource[] resources2 = springContext.getResources(query2);	
				ArrayList<Resource> resources = new ArrayList<Resource>();
				resources.addAll(Arrays.asList(resources1));
				resources.addAll(Arrays.asList(resources2));
				facetFiles = new Resource[resources.size()];
				facetFiles = resources.toArray(facetFiles);
				if (logger.isDebugEnabled()) logger.debug("Found " + facetFiles.length + " candidates, iterating and creating descriptors..." );
			} catch(IOException e) {
				String message = "Unable to lookup for resources using Spring Context !";
				logger.error(message, e);
				throw new RuntimeException(message, e);
			}
				
			// iterate on descriptors and create them 
			for (int i = 0; i < facetFiles.length; i++) {
				File f;
				try {
					f = facetFiles[i].getFile();
				} catch(IOException e) {
					String message = "IOException caught while trying to access facet file '" + facetFiles[i] + "', facet file skipped";
					logger.error(message, e);
					throw new RuntimeException(message, e);
				}
				String fullPath = f.getAbsolutePath();
				String name;
				String profileId;
				Class targetObjectClass;
									
				// name (remove the .facet suffix)
				// -------------------------------
				name = f.getName();
				if (name!=null) {
					int dotIndx = name.lastIndexOf('.');
					if (dotIndx!=-1) {
						name = name.substring(0, dotIndx);
					}
				
					// target Object Type
					// ------------------					
					File typeFolder = f.getParentFile();
					if (typeFolder!=null) {
						String fqcn = typeFolder.getName();
						try {
							targetObjectClass = Class.forName(fqcn);
						} catch(Exception ex) {
							targetObjectClass = null;
							logger.warn("Exception caught while trying to load target type class in facet file '" + fullPath + "'", ex);
						}							
						if (targetObjectClass!=null) {
						
							// profile Id
							// ----------
							File pflFolder = typeFolder.getParentFile();
							if (pflFolder!=null) {
								profileId = pflFolder.getName();
								
								// everything is OK, creade the descriptor and add it to the list
								GroovyFacetDescriptor d = new GroovyFacetDescriptor();
								d.setName(name);
								d.setProfileId(profileId);
								d.setTargetObjectType(targetObjectClass);
								d.setScriptFile(f);
								descriptors.add(d);
								nbDesc++;
								if (logger.isDebugEnabled()) logger.debug("Groovy facet descriptor found and added for name='" + name + "', profileId='" + profileId + "' and targetObjectType='" + targetObjectClass + "'");
								
							} else {									
								logger.warn("Facet from file " + fullPath + " isn't defined in a parent folder, which is required (profile ID) - facet file skipped");	
							}
						} else {
							logger.warn("Unable to load class target object class '" + fqcn + "' for facet from file '" + fullPath + "' - facet file skipped !");								
						}
					} else {
						logger.warn("Facet from file " + fullPath + " isn't defined in a parent folder, which is required (target type) - facet file skipped");	
					}
				} else {
					logger.warn("Unable to read facet name from file " + fullPath + ", facet file skipped");
				}
			}						
		}
		if (logger.isInfoEnabled()) logger.info("init OK, " + nbDesc + " descriptors loaded");
	}	
	
	/**
	 * Utility method : Compute and return the path to a Facet Script 
	 * for passed parameters.
	 */
	public String computeScriptPath(String name, String profileId, Class targetObjectType) {
		StringBuffer res = new StringBuffer();
		res.append(SEPARATOR);
		res.append(profileId);
		res.append(SEPARATOR);
		res.append(targetObjectType.getSimpleName());
		res.append(SEPARATOR);
		res.append(name);
		return res.toString();		
	}
	
	/**
	 * Return an input stream on the facet Script file, by using 
	 * <code>getClass().getResourceAtStream()</code>.
	 */
	public InputStream getStreamToScript(FacetDescriptor d) {
		String path = computeScriptPath(d.getName(), d.getProfileId(), d.getTargetObjectType());
		InputStream res = getClass().getResourceAsStream(path);
		return res;
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
