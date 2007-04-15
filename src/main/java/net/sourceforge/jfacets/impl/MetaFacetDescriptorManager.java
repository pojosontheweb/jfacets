package net.sourceforge.jfacets.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;

/**
 * The MetaFacetDescriptorManager : encapsulates several facet descriptor 
 * managers in a single one, allowing to use different facet types if required 
 * (e.g. GroovyFacets + "regular" facets). <br/>
 * Descriptors for registered managers are pre-loaded by the MetaFacetDescriptorManager 
 * at start-up time. 
 */
public class MetaFacetDescriptorManager implements IFacetDescriptorManager {

	private static final Logger logger = Logger.getLogger(MetaFacetDescriptorManager.class);
	
	/** the list of managers */
	private List<IFacetDescriptorManager> managers;
		
//	/**
//	 * Pre-loads all data from registered managers for faster access.
//	 */
	public void initialize() {
//		// aggregate descriptors from all managers...
//		descriptors = new ArrayList<FacetDescriptor>();
//		if (logger.isInfoEnabled()) logger.info("Loading descriptors from " + managers.size() + " managers...");
//		for (IFacetDescriptorManager manager : managers) {
//			FacetDescriptor[] ds = manager.getDescriptors();
//			List<FacetDescriptor> tmpList = Arrays.asList(ds);			
//			descriptors.addAll(tmpList);
//			if (logger.isDebugEnabled()) logger.debug("Added " + tmpList.size() + " descriptors from manager " + manager);
//		}
		logger.info(managers.size() + " managers registered");		
	}
	
	protected List<FacetDescriptor> concatAll() {
		ArrayList<FacetDescriptor> res = new ArrayList<FacetDescriptor>();
		for (IFacetDescriptorManager m : managers)
			res.addAll(Arrays.asList(m.getDescriptors()));
		return res;
	}
	
	/**
	 * Return the descriptor sctrictly associated to passed params if any, 
	 * null of not found.
	 */
	public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
//		FacetDescriptor res = null;
//		for (FacetDescriptor d : descriptors) {
//			if (d.getName().equals(name) &&
//					d.getProfileId().equals(profileId) &&
//					d.getTargetObjectType().equals(targetObjectType)) {
//				res = d;
//				break;
//			}
//		}
//		return res;
		FacetDescriptor res = null;
		for (IFacetDescriptorManager m : managers) {
			res = m.getDescriptor(name, profileId, targetObjectType);
			if (res!=null)
				break;
		}
		if (logger.isDebugEnabled()) logger.debug("getDescriptor(" + name +"," + profileId + "," + targetObjectType +") : returning " + res);
		return res;
	}

	/**
	 * Return all descriptors in an array.
	 */
	public FacetDescriptor[] getDescriptors() {
//		FacetDescriptor[] res = new FacetDescriptor[descriptors.size()];
//		res = (FacetDescriptor[])descriptors.toArray(res);
//		return res;
		List<FacetDescriptor> all = concatAll();
		FacetDescriptor[] res = new FacetDescriptor[all.size()];
		res = (FacetDescriptor[])all.toArray(res);
		if (logger.isDebugEnabled()) logger.debug("getDescriptors() : returning " + res.length + " descriptor(s)");
		return res;
	}
	
	/** return the managers to be used for descriptor lookup */
	public List<IFacetDescriptorManager> getManagers() {
		return managers;
	}

	/** set the managers to be used for descriptor lookup */
	public void setManagers(List<IFacetDescriptorManager> managers) {
		this.managers = managers;
	}
	
	

}
