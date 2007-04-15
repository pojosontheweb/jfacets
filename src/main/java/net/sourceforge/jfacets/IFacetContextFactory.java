package net.sourceforge.jfacets;

/**
 * Base interface for ContextFactories, which serves to 
 * create contexts and pass them to facets when they are retrieved, 
 * just after instanciation.<br/> 
 *
 * @see net.sourceforge.jfacets.IFacetContext
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public interface IFacetContextFactory {

	/**
	 * Invoked by the framework to create facet contexts
	 * @param facetName The facet name
	 * @param profile The profile
	 * @param targetObject The target object
	 * @return The freshly created context
	 * @deprecated This method won't be used in 2.0 final. It's been replaced by the version that also accepts the FacetDescriptor used to retrieve the facet.
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject);

	/**
	 * Invoked by the framework to create facet contexts
	 * @param facetName The facet name
	 * @param profile The profile
	 * @param targetObject The target object
	 * @param facetDescriptor The facet descriptor used for looking up the facet
	 * @return The freshly created context
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor);

}
