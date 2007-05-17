package net.sourceforge.jfacets;

/**
 * This interface allows to initialize facets 
 * just after they have been retrieved, and before 
 * they are returned to the caller of <code>JFacets.getFacet(...)</code>.
 */
public interface IInitializableFacet {
	
	/**
	 * Invoked just after the facet has been successfully retrieved, and right 
	 * before it's returned to the caller of <code>JFacets.getFacet(...)</code>.
	 */
	public void initializeFacet();

}
