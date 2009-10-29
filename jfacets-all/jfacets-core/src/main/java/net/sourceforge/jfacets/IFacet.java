package net.sourceforge.jfacets;

/**
 * Base interface for facets.
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public interface IFacet {

	/**
	 * Invoked by the framework, passing it the facet context
	 * @param ctx the facet fontext
	 */
	public void setFacetContext(IFacetContext ctx);
	
	/**
	 * Return the facet context
	 */
	public IFacetContext getFacetContext();

}
