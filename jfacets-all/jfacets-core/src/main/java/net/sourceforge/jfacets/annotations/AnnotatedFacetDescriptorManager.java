package net.sourceforge.jfacets.annotations;

import net.sourceforge.jfacets.FacetDescriptorManagerBase;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.util.ResolverUtil;
import net.sourceforge.jfacets.log.JFacetsLogger;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
public class AnnotatedFacetDescriptorManager extends FacetDescriptorManagerBase {

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(AnnotatedFacetDescriptorManager.class);

	/** base packages list to search facet scripts in */
	private List<String> basePackages;

    private ClassLoader classLoader;

	/**
	 * Create the manager and set base packages that will be scanned for
     * annotated facets.
	 * @param basePackages the packages as a list of Strings
	 */
	public AnnotatedFacetDescriptorManager(List<String> basePackages) {
		this.basePackages = basePackages;
	}

    /**
     * Set the class loader to be used for classpath scanning
     * @param classLoader the classloader to be used
     * @return this for chained calls
     */
    public AnnotatedFacetDescriptorManager setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    /**
	 * Loads all available Facet Descriptors by scanning the CLASSPATH with
	 * specified base packages.
     * @return this for chained calls
	 */
	@SuppressWarnings("unchecked")
	public AnnotatedFacetDescriptorManager initialize() {
        if (basePackages==null || basePackages.size()==0) {
            logger.warn("No base packages have been defined, as a result no annotated facet can be found ! " +
                    "Please set the basePackages property of the AnnotatedFacetDescriptorManager.");
        }
        List<FacetDescriptor> annotatedDescriptors = new ArrayList<FacetDescriptor>();
        for (String pkg : basePackages) {
            if (logger.isDebugEnabled()) {
                logger.debug("Scanning package " + pkg);
            }
            ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
            if (classLoader!=null) {
                logger.debug("Using supplied class loader " + classLoader);
                resolverUtil.setClassLoader(classLoader);
            }
            resolverUtil.findAnnotated(FacetKey.class, pkg);
            resolverUtil.findAnnotated(FacetKeyList.class, pkg);
            for (Class<?> clazz : resolverUtil.getClasses()) {
                FacetKeyList fkl = clazz.getAnnotation(FacetKeyList.class);
                 if (fkl!=null) {

                     // multiple facet keys
                     // -------------------
                     FacetKey[] facetKeys = fkl.keys();
                     for (FacetKey facetKey : facetKeys) {
                         createDescriptorForAnnotation(facetKey, clazz, annotatedDescriptors);
                     }

                 } else {
                     // no multiple key, try single one...
                     FacetKey annot = clazz.getAnnotation(FacetKey.class);
                     if (annot!=null) {

                         // single facet key
                         // ----------------
                         createDescriptorForAnnotation(annot, clazz, annotatedDescriptors);

                     } else {
                         // not annotated
                         if (logger.isDebugEnabled()) logger.debug("Skipped class " + clazz + ", does not have the @FacetKey annot");
                     }
                 }
            }
        }
        setDescriptors(annotatedDescriptors);
		if (logger.isInfoEnabled()) logger.info("init OK, " + annotatedDescriptors.size() + " descriptors loaded");
        return this;
	}


    /**
	 * Create a descriptor for passed FacetKey and facet class and adds it to the descriptors list
	 * @param annot The FacetKey annotation
	 * @param facetClass The facet implementation class
	 */
	private void createDescriptorForAnnotation(FacetKey annot, Class<?> facetClass, List<FacetDescriptor> descriptors) {
		// annotated, create and add descriptor
		String name = annot.name();
		String profileId = annot.profileId();
		Class<?> targetObjectType = annot.targetObjectType();
		if (name!=null && profileId!=null && targetObjectType!=null) {
            FacetDescriptor descriptor = new FacetDescriptor();
            descriptor.setName(name);
            descriptor.setProfileId(profileId);
            descriptor.setTargetObjectType(targetObjectType);
            descriptor.setFacetClass(facetClass);
            descriptors.add(descriptor);
            if (logger.isDebugEnabled()) {
                logger.debug("Created and added descriptor " + descriptor + " for annotated facet class " + facetClass);
            }

		} else {
			// missing annot attributes
			logger.warn("Class " + facetClass + " has the @FacetKey annot but some attributes are missing ! name, profileId and targetObjectType are required");
		}
	}

}
