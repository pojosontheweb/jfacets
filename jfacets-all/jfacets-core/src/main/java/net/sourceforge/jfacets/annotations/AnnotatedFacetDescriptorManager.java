package net.sourceforge.jfacets.annotations;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.util.ResolverUtil;
import net.sourceforge.jfacets.log.JFacetsLogger;

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
public class AnnotatedFacetDescriptorManager implements IFacetDescriptorManager {

	private static final String SEPARATOR = "/";

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(AnnotatedFacetDescriptorManager.class);

	/** base packages list to search facet scripts in */
	private List<String> basePackages;

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

	/**
	 * Loads all available Facet Descriptors by scanning the CLASSPATH with
	 * specified base packages.
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
        if (basePackages==null || basePackages.size()==0) {
            logger.warn("No base packages have been defined, as a result no annotated facet can be found ! " +
                    "Please set the basePackages property of the AnnotatedFacetDescriptorManager.");
        }
        String[] pkgs = new String[basePackages.size()];
        pkgs = basePackages.toArray(pkgs);
        ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
        resolverUtil.findAnnotated(FacetKey.class, pkgs);
        resolverUtil.findAnnotated(FacetKeyList.class, pkgs);
        for (Class<? extends Object> clazz : resolverUtil.getClasses()) {
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
		res = descriptors.toArray(res);
		return res;
	}


}
