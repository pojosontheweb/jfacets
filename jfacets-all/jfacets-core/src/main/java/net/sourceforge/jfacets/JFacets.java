package net.sourceforge.jfacets;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.jfacets.log.JFacetsLogger;


/**
 * Top-level class for clients : used for retrieving facets at run time (see the getFacet(...) methods).
 *
 * The lifecycle of instances of this class goes like :
 * <ul>
 *  <li>create object using no-args constructor</li>
 *  <li>set <code>facetRepository</code> to be used for facet lookup</li>
 *  <li>invoke the <code>afterPropertiesSet</code> init method</li>
 * </ul>
 *
 * The preferred way to obtain a JFacets instance is to use the
 * {@link net.sourceforge.jfacets.JFacetsBuilder}.
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class JFacets {
	
	private static final JFacetsLogger logger = JFacetsLogger.getLogger(JFacets.class);
			
	/**
	 * Indicates wether or not to use cached profiles or not (default=false).
	 */
	private boolean useProfilesCache = false;

	/**
	 * The cache of already obtained profiles.
	 */
	private ConcurrentHashMap<String, IProfile> profilesCache;
	
	/** 
	 * The fallback profile to be used in case 
	 * get/execFacet is invoked with a null profile 
	 * or no principal associated to the current request. 
	 * Handy for allowing unauthenticated users to use facets 
	 * that are assigned e.g. to a "GUEST" profile or smth like this...
	 * Defaults to null.
	 */ 
	private String fallbackProfileId = null;
	
	/**
	 * The facet repository to be used for facet lookup.
	 */
	private IFacetRepository facetRepository;
	
	/**
	 * Retrieve a profile for passed ID. 
	 * Checks for fallback profile and profiles cache if needed.
     * @return the profile for passed ID if found, <code>null</code> if no such profile
	 */
	protected IProfile getProfile(String profileId) {
		
		// handle fallback profile
		// -----------------------
		
		if (profileId==null) {
			if (fallbackProfileId!=null) {
				if (logger.isInfoEnabled()) logger.info("supplied profile ID is null, trying fallbackProfileId '" + fallbackProfileId + "'");
				profileId = fallbackProfileId;
			} else {
				logger.warn("Supplied profileId is null ! If you plan to pass null profile IDs then consider using the fallbackProfileId feature !");
				return null;
			}
		}
		
		// retrieve profile for ID
		// -----------------------
		
		IProfile res = null;
		
		// do we use cache ?
		if (useProfilesCache) {
			// try to get it from the cache if possible
			res = profilesCache.get(profileId);
		}
		if (res!=null) {
			if (logger.isDebugEnabled()) logger.debug("Profile obtained from cache, using it !");
		} else {
			// profile not in cache or cache not used, grab the profile
			// from the profile repository
			IProfileRepository profileRepository = facetRepository.getProfileRepository();
			res = profileRepository.getProfileById(profileId);
			if (res==null)
				logger.warn("Profile not found for ID " + profileId + " using pRepo !");
			else {
				if (logger.isDebugEnabled()) logger.debug("Profile obtained from the repository, returning " + res.toString());
				if (useProfilesCache) {
					profilesCache.put(profileId, res);
					if (logger.isDebugEnabled()) logger.debug("Using profiles cache, profile " + res + " added to cache");
				}
			}
		}
		return res;
	}
	
	/**
	 * Clears the profiles cache
	 */
	public void clearCache() {
		profilesCache.clear();
	}
		
	/**
	 * Retrieves a facet for passed parameters (uses <code>targetObjectClass</code> class to 
	 * retrieve the facet, so <code>targetObject</code> can be null).
	 * @param facetName The name of the facet
	 * @param profileId The id of the profile
	 * @param targetObject The target object
	 * @param targetObjectClass The target object's class to be used to retrieve the facet
	 * @return the facet for passed params if found, <code>null</code> if not found.
	 */
	public Object getFacet(
			String facetName, 
			String profileId, 
			Object targetObject,
			Class targetObjectClass) {
		
		if (logger.isInfoEnabled()) logger.info("Attempting to retrieve facet for key (" + facetName + ", " + profileId + ", " + targetObjectClass + ", with targetObject="+targetObject+")...");
		
		// retrieve profile
		IProfile profile = getProfile(profileId);
		if (profile == null) {
			logger.warn("Profile not found for id " + profileId + " ! returning null");
			return null;
		} else {
			Object facet = facetRepository.getFacet(facetName, profile, targetObject, targetObjectClass);
			if (logger.isInfoEnabled()) logger.info("returning facet " + facet);
			return facet;
		}

	}
	
	/**
	 * Retrieves a facet for passed parameters (uses <code>targetObject</code>'s class to 
	 * retrieve the facet).
	 * @param facetName The name of the facet
	 * @param profileId The id of the profile
	 * @param targetObject The target object
	 * @return the facet for passed params if found, <code>null</code> if not found.
	 */
	public Object getFacet(
			String facetName, 
			String profileId, 
			Object targetObject) {
		
		if (logger.isInfoEnabled()) logger.info("Attempting to retrieve facet for key (" + facetName + ", " + profileId + ", " + targetObject.getClass() + ")...");
		
		// retrieve profile
		IProfile profile = getProfile(profileId);
		if (profile == null) {
			logger.warn("Profile not found for id " + profileId + " ! returning null");
			return null;
		} else {
			Object facet = facetRepository.getFacet(facetName, profile, targetObject);
			if (logger.isInfoEnabled()) logger.info("returning facet " + facet);
			return facet;
		}
	}
	
	/**
	 * Retrieves a facet for passed parameters. This version passes a fake Object 
	 * as the target object of the facet. It can be used when you need only the 
	 * profile assignation, but don't really care about the target object.
	 * @param facetName The name of the facet
	 * @param profileId The id of the profile
	 * @return the facet for passed params if found, <code>null</code> if not found.
	 */
	public Object getFacet(
			String facetName, 
			String profileId) {
		
		if (logger.isInfoEnabled()) logger.info("Attempting to retrieve facet for key (" + facetName + ", " + profileId + " with no target object (dummy Object will be used instead)");
		
		return getFacet(facetName, profileId, new Object());
	}

	public IFacetRepository getFacetRepository() {
		return facetRepository;
	}

	public void setFacetRepository(IFacetRepository facetRepository) {
		this.facetRepository = facetRepository;
	}
	
	public IProfileRepository getProfileRepository() {
		return facetRepository.getProfileRepository();
	}
	
	public String getFallbackProfileId() {
		return fallbackProfileId;
	}

	public void setFallbackProfileId(String fallbackProfileId) {
		this.fallbackProfileId = fallbackProfileId;
	}

	/**
	 * To be invoked after all props have been set. Checks that required components have been
     * injected, and creates the profiles cache map if required.
	 */
	public void afterPropertiesSet() {
        if (facetRepository==null) {
            throw new IllegalStateException("FacetRepository has not been set !");
        }
		if (useProfilesCache) {
			profilesCache = new ConcurrentHashMap<String, IProfile>();
			if (logger.isInfoEnabled()) logger.info("Using profiles cache !");
		}
	}

	public boolean isUseProfilesCache() {
		return useProfilesCache;
	}

	public void setUseProfilesCache(boolean useProfilesCache) {
		this.useProfilesCache = useProfilesCache;
	}
	
	/**
	 * Dump all facets as an XML facet descriptor (utility method, 
	 * can be handy sometimes to get a list of all facet definitions).
	 */
	public String dumpFacetsAsXml() {
		StringBuffer res = new StringBuffer();
		res.append("<facets>\n");
		List<FacetDescriptor> descriptors = getFacetRepository().getFacetDescriptorManager().getDescriptors();
		for (FacetDescriptor fd : descriptors) {
			if (isDescriptorOk(fd)) {
				res.append("  <facet name=\"");
				res.append(fd.getName());
				res.append("\" profile=\"");
				res.append(fd.getProfileId());
				res.append("\" object_type=\"");
				res.append(fd.getTargetObjectType().getName());
				res.append("\" class=\"");
				res.append(fd.getFacetClass().getName());
				res.append("\"/>\n");
			}
		}
		res.append("</facets>\n");
		return res.toString();
	}
	
	/**
	 * Return true if all fields of the descriptor are filled in (name, 
	 * profileId, targetObjectType and facetClass), false otherwise.
	 */
	private static boolean isDescriptorOk(FacetDescriptor fd) {
		return fd.getName()!=null && fd.getProfileId()!=null && fd.getTargetObjectType()!=null && fd.getFacetClass()!=null;
	}
	
}
