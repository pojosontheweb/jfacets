package net.sourceforge.jfacets;

/**
 * Interface of the FacetRepository.
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public interface IFacetRepository {
	
	/**
	 * Return the ProfileRepository
	 */
	public IProfileRepository getProfileRepository();

	/**
	 * Return the FacetFactory
	 */
	public IFacetFactory getFacetFactory();

	/**
	 * Return the FacetContextFactory
	 */	
	public IFacetContextFactory getFacetContextFactory();
	
	/**
	 * Return the FacetDescriptorManager 
	 */
	public IFacetDescriptorManager getFacetDescriptorManager();

	/**
	 * Method for retrieving facets. Uses <code>targetObject</code>'s 
	 * run-time type.
	 * @return the facet (with assigned context if implementing IFacet) if found, null if not found 
	 */
	public Object getFacet(String name, IProfile profile, Object targetObject);

	/**
	 * Method for retrieving facets. Uses passed <code>targetObjectClass</code>, therefore 
	 * you can pass null targetObjects ! 
	 * @return the facet (with assigned context if implementing) if found, null if not found 
	 */
	public Object getFacet(String name, IProfile profile, Object targetObject, Class targetObjectType);
	
	/** 
	 * Return all the facet descriptors that match passed profile 
	 * and object type, handling inheritance.
	 */ 
	public FacetDescriptor[] getDescriptors(IProfile profile, Class targetObjectType);

}
