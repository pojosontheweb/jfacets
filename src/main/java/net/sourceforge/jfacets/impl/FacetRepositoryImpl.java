package net.sourceforge.jfacets.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IFacetFactory;
import net.sourceforge.jfacets.IFacetRepository;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;

import org.apache.log4j.Logger;

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
	private static final Logger logger = Logger.getLogger(FacetRepositoryImpl.class);
	
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
		
	/** tries to get descriptor for all types for passed profile, and recurses in all super profiles */
	@SuppressWarnings("unchecked")
	protected void recurseInProfiles(String name, IProfile curPfl, Vector facetWrapper, Vector descriptorsWrapper, Vector allTypes) throws InstantiationException, IllegalAccessException {
		// tries all types for current profile...
		if (logger.isDebugEnabled()) logger.debug("recurseInProfiles() : entering method for profile ID='" + curPfl.getId() + "', handling all supertypes ...");
		FacetDescriptor d = null;
		for(Enumeration e = allTypes.elements(); ((e.hasMoreElements()) && (d==null)); ) {
			Class curType = (Class)e.nextElement();
			d = facetDescriptorManager.getDescriptor(name, curPfl.getId(), curType);
		}
		if (d!=null) {
			if (logger.isDebugEnabled()) logger.debug("recurseInProfiles() : found descriptor, trying to instantiate facet using supplied factory...");
			Object facet = facetFactory.createFacet(d);
			if (facet!=null) {
				if (logger.isDebugEnabled()) logger.debug("recurseInProfiles() : ... facet found OK");
				facetWrapper.add(facet);
				descriptorsWrapper.add(d);
			} else {
				logger.warn("recurseInProfiles() : ... problem while trying to get facet for descriptor ! Nothing done...");
			}
		} else {
			if (logger.isDebugEnabled()) logger.debug("recurseInProfiles() : descriptor not found, trying in parents...");
			// tries in parents...
			IProfile[] parents = getProfileRepository().getSuperProfiles(curPfl);
			for (int i = 0; i < parents.length; i++) {
				recurseInProfiles(name, parents[i], facetWrapper, descriptorsWrapper, allTypes);
			}
		}
	}
			
	/*
	 *
	 * Reflection utility methods...
	 *
	 */
	
	/** feeds <code>result</code> vector with all <code>objectType</code> super-types until <code>java.lang.Object</code> */
	@SuppressWarnings("unchecked")
	public static void buildObjectSuperTypesList(Class objectType, Vector result) {
		// obtains direct super types for passed object type
		Class[] superTypes = getDirectSuperTypes(objectType);
		// adds found supertypes to the vector
		for (int i=0;i<superTypes.length;i++)
			if ((!result.contains(superTypes[i])) && (!superTypes[i].getName().equals("java.lang.Object")))
				result.add(superTypes[i]);
			// recurses on each supertype
		for (int i=0;i<superTypes.length;i++)
			buildObjectSuperTypesList(superTypes[i],result);
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
				allTypes[0] = superClass;
				for (int i=0;i<intfs.length;i++)
					allTypes[i+1] = intfs[i];
				return allTypes;
			}
		}
	}
			
	@SuppressWarnings("unchecked")
	public FacetDescriptor[] getDescriptors(IProfile profile, Class targetObjectType) {
		if (logger.isDebugEnabled()) logger.debug("getting facet descriptors for profile id " + profile.getId() + " and type = " + targetObjectType);
		FacetDescriptor[] descriptors = facetDescriptorManager.getDescriptors();
		ArrayList res = new ArrayList();
		// get descriptors for profile
		for (int i = 0; i < descriptors.length; i++) {
			if (descriptors[i].getProfileId().equals(profile.getId())) {
				if (logger.isDebugEnabled()) logger.debug("descriptor " + descriptors[i] + " matches profile");
				// profile OK, look if type is assignable
				if (descriptors[i].getTargetObjectType().isAssignableFrom(targetObjectType)) {
					res.add(descriptors[i]);
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

	@SuppressWarnings("unchecked")
	public Object getFacet(String name, IProfile profile, Object targetObject, Class targetObjectType) {
		// builds target object super types list
		// TODO cache this : only compute types list once !
		if (logger.isDebugEnabled()) logger.debug("getFacet() : building target object's super types list...");
		Vector superTypes = new Vector();
		buildObjectSuperTypesList(targetObjectType, superTypes);
		Vector allTypes = new Vector();
		allTypes.add(targetObjectType);
		allTypes.addAll(1,superTypes);
		if (!targetObjectType.equals(Object.class))
			allTypes.add(Object.class);
		if (logger.isDebugEnabled()) logger.debug("getFacet() : ... " + allTypes.size() + " class(es)/interface(s) in types list");
		
		// recurses in profiles starting from 'pfl' to root(s), and tries for each type...
		if (logger.isDebugEnabled()) logger.debug("getFacet() : starting profiles recursion...");
		Vector wrapper = new Vector();
		Vector descriptorWrapper = new Vector();
		try {
			recurseInProfiles(name, profile, wrapper, descriptorWrapper, allTypes);
		} catch (InstantiationException e1) {
			logger.error("getFacet() : InstantiationException caught while trying to get facet !", e1);
		} catch (IllegalAccessException e1) {
			logger.error("getFacet() : IllegalAccessException caught while trying to get facet !", e1);
		}
		if (wrapper.size()>0) {
			Object facet = (Object)wrapper.get(0);
			if (facet instanceof IFacet) {
				// facet implements IFacet : we create and set the context for it
				FacetDescriptor fd = null;
				if (descriptorWrapper.size()==1) {
					fd = (FacetDescriptor)descriptorWrapper.get(0);
				}
				IFacetContext ctx = facetContextFactory.create(name, profile, targetObject, fd);
				((IFacet)facet).setContext(ctx);
			}
			if (logger.isDebugEnabled()) logger.debug("getFacet() : OK returning facet " + facet);
			return facet;
		} else {
			if (logger.isDebugEnabled()) logger.debug("getFacet() : could not find facet with parameters : name='" + name + 
					"', profileId='" + profile.getId() + "', objectType='" + 
					targetObjectType.getName() + "' and targetObject='" + 
					targetObject + "', returning null");
			return null;
		}
	}

}
