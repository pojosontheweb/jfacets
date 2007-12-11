package net.sourceforge.jfacets;

/**
 * FacetFactories serves to instanciate facets.<br/>
 * When you try to get a facet, the framework tries to find the appropriate 
 * descriptor for the (name, profile, targetObject) key. If found, it uses 
 * the FacetFactory to create an instance of the facet.<br/>
 * 
 * Facet factories, as of JFacets 2.0, can now create objects 
 * that doesn't implement the IFacet interface, for greater flexibility.
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public interface IFacetFactory {

	public Object createFacet(FacetDescriptor d);
	
}
