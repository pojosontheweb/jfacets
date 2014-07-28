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
     * @param targetObjectClass the target object class
     * @param facetDescriptor The facet descriptor used for looking up the facet
     * @return The freshly created context
     */
    public IFacetContext create(String facetName, IProfile profile, Object targetObject, Class<?> targetObjectClass, FacetDescriptor facetDescriptor);

}
