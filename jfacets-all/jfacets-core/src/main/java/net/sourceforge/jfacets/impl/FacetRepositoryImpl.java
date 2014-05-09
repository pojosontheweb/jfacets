package net.sourceforge.jfacets.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IFacetFactory;
import net.sourceforge.jfacets.IFacetRepository;
import net.sourceforge.jfacets.IInitializableFacet;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * Implementation of the facet repository
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public class FacetRepositoryImpl implements IFacetRepository {
	
	/** a ref to the profile repository */
	private IProfileRepository profileRepo = null;
	/** the facet factory to be used for facet instantiation */
	private IFacetFactory facetFactory;
	/** the facet context factory to create contexts */
	private IFacetContextFactory facetContextFactory;
	/** the descriptors manager */
	private IFacetDescriptorManager facetDescriptorManager;
	
	/** the logger */
	private static final JFacetsLogger logger = JFacetsLogger.getLogger(FacetRepositoryImpl.class);
	
	/** everything loaded at construction */
	public FacetRepositoryImpl(IProfileRepository pRepo, 
			IFacetFactory facetFactory, 
			IFacetContextFactory facetContextFactory,
			IFacetDescriptorManager facetDescriptorManager) {
		
		this.profileRepo = pRepo;
		this.facetFactory = facetFactory;
		this.facetContextFactory = facetContextFactory;
		this.facetDescriptorManager = facetDescriptorManager;
	}
				
	/** returns first level supertypes for passed <code>objectType</code> */
	public static Class[] getDirectSuperTypes(Class objectType) {
		if (objectType.isInterface())
			return objectType.getInterfaces();
		else {
			Class superClass = objectType.getSuperclass();
			if (superClass==null)
				return new Class[0];
			else {
				Class[] intfs = objectType.getInterfaces();
				Class[] allTypes = new Class[intfs.length+1];
//				allTypes[0] = superClass;
//				for (int i=0;i<intfs.length;i++)
//					allTypes[i+1] = intfs[i];
//				return allTypes;
				for (int i=0;i<intfs.length;i++)
					allTypes[i] = intfs[i];
				allTypes[intfs.length] = superClass;
				return allTypes;
			}
		}
	}
			
	@SuppressWarnings("unchecked")
	public FacetDescriptor[] getDescriptors(IProfile profile, Class targetObjectType) {
		if (logger.isDebugEnabled()) logger.debug("getting facet descriptors for profile id " + profile.getId() + " and type = " + targetObjectType);
		List<FacetDescriptor> descriptors = facetDescriptorManager.getDescriptors();
		ArrayList res = new ArrayList();
		// get descriptors for profile
		for (FacetDescriptor descriptor : descriptors) {
			if (descriptor.getProfileId().equals(profile.getId())) {
				if (logger.isDebugEnabled()) logger.debug("descriptor " + descriptor + " matches profile");
				// profile OK, look if type is assignable
				if (descriptor.getTargetObjectType().isAssignableFrom(targetObjectType)) {
					res.add(descriptor);
					if (logger.isDebugEnabled()) logger.debug("  -> was compatible, added");
				} else {
					if (logger.isDebugEnabled()) logger.debug("  -> was not compatible, skipped");
				}
			}
		}
		// check for profile supertypes
		IProfile[] superProfiles = profileRepo.getSuperProfiles(profile);
		for (int j = 0; j < superProfiles.length; j++) {
			FacetDescriptor[] fds = getDescriptors(superProfiles[j], targetObjectType);
			List lst = Arrays.asList(fds);
			res.addAll(lst);
		}
		FacetDescriptor[] resA = new FacetDescriptor[res.size()];
		resA = (FacetDescriptor[])res.toArray(resA);
		if (logger.isDebugEnabled()) logger.debug("OK returning " + resA.length + " descriptors");
		return resA;
	}

	public IProfileRepository getProfileRepository() {
		return profileRepo;
	}

	public IFacetFactory getFacetFactory() {
		return facetFactory;
	}

	public IFacetContextFactory getFacetContextFactory() {
		return facetContextFactory;
	}

	public IFacetDescriptorManager getFacetDescriptorManager() {
		return facetDescriptorManager;
	}
	
	public Object getFacet(String facetName, IProfile profile, Object targetObject) {
		if (logger.isDebugEnabled()) logger.debug("getFacet() : invoked with parameters : facetName='" + facetName + "', profileId='" + profile.getId() + "', and targetObject='" + targetObject + "'...");
		return getFacet(facetName, profile, targetObject, targetObject.getClass());
	}

	public Object getFacet(String name, IProfile profile, Object targetObject, Class targetObjectType) {
		if (logger.isDebugEnabled()) logger.debug("trying to retrieve facet name='" + name + "', profileId='" + profile.getId() + "',  targetObject='" + targetObject + "', targetObjectType='" + targetObjectType + "'...");		
		ArrayList<FacetDescriptor> discardedDescriptors = new ArrayList<FacetDescriptor>();
		Object facet = climbProfiles(name, profile, targetObject, targetObjectType, discardedDescriptors);
		if (facet!=null && facet instanceof IInitializableFacet) {
			if (logger.isDebugEnabled()) logger.debug("facet implements IInitializableFacet : initializing it");
			((IInitializableFacet)facet).initializeFacet();
		}
		if (logger.isDebugEnabled()) logger.debug("returning " + facet);
		return facet;
	}
	
	private Object climbProfiles(String facetName, 
			IProfile profile, 
			Object targetObject, 
			Class targetObjectClass, 
			List<FacetDescriptor> discardedDescriptors) {
		
		ArrayList<Class> alreadyCheckedClasses = new ArrayList<Class>();
		Object facet = climbTypes(facetName, profile, targetObject, targetObjectClass, alreadyCheckedClasses, discardedDescriptors);
		if (facet==null) {
			// facet not found, try super profile(s)...
			IProfile[] parents = getProfileRepository().getSuperProfiles(profile);
			for(IProfile parent : parents) {
				facet = climbProfiles(facetName, parent, targetObject, targetObjectClass, discardedDescriptors);
				if (facet!=null) {
					break;
				}
			}
		}
		return facet;
	}
	
	private Object climbTypes(String facetName, 
			IProfile profile, 
			Object targetObject, 
			Class targetObjectClass, 
			List<Class> alreadyCheckedClasses,
			List<FacetDescriptor> discardedDescriptors) {
		
		if (logger.isDebugEnabled()) logger.debug("trying to get descriptor (" + facetName + "," + profile.getId() + "," + targetObject + "," + targetObjectClass + ")");	
		
		// try to find descriptor strict

        List<FacetDescriptor> fds = facetDescriptorManager.getDescriptors(facetName, profile.getId(), targetObjectClass);
        // find first non discarded FD if any
        FacetDescriptor fd = null;
        for (FacetDescriptor curFd : fds) {
            if (!discardedDescriptors.contains(curFd)) {
                fd = curFd;
                break;
            }
        }

		alreadyCheckedClasses.add(targetObjectClass);
		if (fd==null) {
			
			// descriptor not found, search in supertypes...
			if (logger.isDebugEnabled()) logger.debug("descriptor not found, climbing supertype(s)...");
			Class[] directSuperTypes = getDirectSuperTypes(targetObjectClass);
			for(Class superType : directSuperTypes) {
				if (!alreadyCheckedClasses.contains(superType)) {
					Object facet = climbTypes(facetName, profile, targetObject, superType, alreadyCheckedClasses, discardedDescriptors);
					if (facet!=null) {
						return facet;
					}
				}
			}
            return null;
			
		} else {
			
			// strict descriptor found, create the facet...
			if (logger.isDebugEnabled()) logger.debug("descriptor found : " + fd + ", creating facet...");
			Object facet = facetFactory.createFacet(fd);
			if (logger.isDebugEnabled()) logger.debug("facet created : " + facet);			

			// create and assign context if needed...
			if (facet instanceof IFacet) {
				if (logger.isDebugEnabled()) logger.debug("implements IFacet, creating and setting context...");
				// facet implements IFacet : we create and set the context for it
				IFacetContext ctx = facetContextFactory.create(facetName, profile, targetObject, fd);
				((IFacet)facet).setFacetContext(ctx);
				if (logger.isDebugEnabled()) logger.debug("context created and assigned : " + ctx);
			}
			
			if (facet instanceof IInstanceFacet) {
				// instance facet, check if it matches...
				if (logger.isDebugEnabled()) logger.debug("implements IInstanceFacet, checking wether it matches or not...");
				if (((IInstanceFacet)facet).matchesTargetObject(targetObject)) {
					// facet matches ! we return it
					if (logger.isDebugEnabled()) logger.debug("instance facet " + facet + " is matching");
				} else {
					// instance facet does NOT match, continue to search.
					// we add the descriptor to the discarded list and restart climbing 
					// from here.
					if (logger.isDebugEnabled()) logger.debug("instance facet " + facet + " is NOT matching ! continue to search");
					discardedDescriptors.add(fd);
					facet = climbTypes(facetName, profile, targetObject, targetObjectClass, alreadyCheckedClasses, discardedDescriptors);	
				}
			}

            return facet;
		}
	}

}
