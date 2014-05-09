package net.sourceforge.jfacets;

import java.util.List;

/**
 * The interface for Facet Descriptor Managers, which manages the facet descriptors. 
 * Provides access to the descriptors.
 */
public interface IFacetDescriptorManager {
	
	/**
	 * Return all managed facet descriptors
	 */
	public abstract List<FacetDescriptor> getDescriptors();
	
	/** 
	 * returns the descriptor for passed parameters, null if not found.
	 * <br/><b>strict match</b> : does not handle inheritance
	 */
	public abstract FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType);
	
	
}
