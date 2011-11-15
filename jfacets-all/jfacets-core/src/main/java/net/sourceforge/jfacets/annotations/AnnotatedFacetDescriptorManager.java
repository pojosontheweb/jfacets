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

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(AnnotatedFacetDescriptorManager.class);

	/** base packages list to search facet scripts in */
	private List<String> basePackages;

	/** the list of loaded descriptors */
	private ArrayList<FacetDescriptor> descriptors = new ArrayList<FacetDescriptor>();

	private int nbDesc = 0;

    /**
     * duplicated key policy : throw exception by default
     */
    private DuplicatedKeyPolicyType duplicatedKeyPolicy = DuplicatedKeyPolicyType.ThrowException;

	/**
	 * Create the manager and set base packages that will be scanned for
     * annotated facets.
	 * @param basePackages the packages as a list of Strings
	 */
	public AnnotatedFacetDescriptorManager(List<String> basePackages) {
		this.basePackages = basePackages;
	}

    /**
     * Set the duplicated key policy. This allows to enable duplicated keys, or to throw an exception when
     * dups are found.
     * @param policyType the policy to use
     * @return this for chained calls
     */
    public AnnotatedFacetDescriptorManager setDuplicatedKeyPolicy(DuplicatedKeyPolicyType policyType) {
        this.duplicatedKeyPolicy = policyType;
        return this;
    }

    public DuplicatedKeyPolicyType getDuplicatedKeyPolicy() {
        return duplicatedKeyPolicy;
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
        for (String pkg : basePackages) {
            if (logger.isDebugEnabled()) {
                logger.debug("Scanning package " + pkg);
            }
            ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
            resolverUtil.findAnnotated(FacetKey.class, pkg);
            resolverUtil.findAnnotated(FacetKeyList.class, pkg);
            for (Class<?> clazz : resolverUtil.getClasses()) {
                FacetKeyList fkl = clazz.getAnnotation(FacetKeyList.class);
                 if (fkl!=null) {

                     // multiple facet keys
                     // -------------------
                     FacetKey[] facetKeys = fkl.keys();
                     for (FacetKey facetKey : facetKeys) {
                         createDescriptorForAnnotation(facetKey, clazz);
                     }

                 } else {
                     // no multiple key, try single one...
                     FacetKey annot = clazz.getAnnotation(FacetKey.class);
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
        }
		if (logger.isInfoEnabled()) logger.info("init OK, " + nbDesc + " descriptors loaded");
        return this;
	}


    /**
	 * Create a descriptor for passed FacetKey and facet class and adds it to the descriptors list
	 * @param annot The FacetKey annotation
	 * @param facetClass The facet implementation class
	 */
	private void createDescriptorForAnnotation(FacetKey annot, Class facetClass) {
		// annotated, create and add descriptor
		String name = annot.name();
		String profileId = annot.profileId();
		Class<?> targetObjectType = annot.targetObjectType();
		if (name!=null && profileId!=null && targetObjectType!=null) {

            // check if a descriptor already exists with the same values and handle
            // as told by the duplicated key policy
            FacetDescriptor dup = getDescriptor(name, profileId, targetObjectType);
            if (dup!=null) {
                switch(duplicatedKeyPolicy) {
                    case ThrowException : {
                        throw new DuplicatedKeyException(name, profileId, targetObjectType);
                    }
                    case FirstScannedWins : {
                        // ignore the facet, just log a message
                        logger.info("Ignoring duplicated facet key (" + name + "," + profileId + "," + targetObjectType +
                            "), policy is set to first scanned wins");
                    }
                    break;
                }
            } else {
                FacetDescriptor descriptor = new FacetDescriptor();
                descriptor.setName(name);
                descriptor.setProfileId(profileId);
                descriptor.setTargetObjectType(targetObjectType);
                descriptor.setFacetClass(facetClass);
                descriptors.add(descriptor);
                nbDesc++;
                if (logger.isDebugEnabled()) logger.debug("Created and added descriptor " + descriptor + " for annotated facet class " + facetClass);
            }
		} else {
			// missing annot attributes
			logger.warn("Class " + facetClass + " has the @FacetKey annot but some attributes are missing ! name, profileId and targetObjectType are required");
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
