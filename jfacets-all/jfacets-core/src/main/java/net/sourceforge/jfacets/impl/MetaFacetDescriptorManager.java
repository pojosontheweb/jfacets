package net.sourceforge.jfacets.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * The MetaFacetDescriptorManager : encapsulates several facet descriptor 
 * managers in a single one, allowing to use different facet types if required 
 * (e.g. GroovyFacets + "regular" facets). <br/>
 * Descriptors for registered managers are pre-loaded by the MetaFacetDescriptorManager 
 * at start-up time. 
 */
public class MetaFacetDescriptorManager implements IFacetDescriptorManager {

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(MetaFacetDescriptorManager.class);
	
	/** the list of managers */
	private List<IFacetDescriptorManager> managers;
	
	protected List<FacetDescriptor> concatAll() {
		ArrayList<FacetDescriptor> res = new ArrayList<FacetDescriptor>();
		for (IFacetDescriptorManager m : managers)
			res.addAll(m.getDescriptors());
		return res;
	}
	
	/**
	 * Return the descriptor sctrictly associated to passed params if any, 
	 * null of not found.
	 */
	public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
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
	public List<FacetDescriptor> getDescriptors() {
		List<FacetDescriptor> all = concatAll();
		if (logger.isDebugEnabled()) logger.debug("getDescriptors() : returning " + all.size() + " descriptor(s)");
		return Collections.unmodifiableList(all);
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
