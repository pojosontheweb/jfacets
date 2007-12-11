package net.sourceforge.jfacets;

/**
 * Base interface for facet contexts. Allows facets to know about :
 * <ul>
 * 	<li>The <b>name</b> of the facet when it was retrieved</li>
 * 	<li>The <b>profile</b> that requested for this facet</li>
 * 	<li>The <b>targetObject</b> for this facet</li>
 * </ul>
 * <br/>
 * Context can be extended using custom ContextFactories.
 * 
 * @see net.sourceforge.jfacets.IFacetContextFactory
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *
 */
public interface IFacetContext {
	
	/**
	 * Return the <b>name</b> of the facet when it was retrieved. 
	 * Should return the same result than <code>getFacetDsecriptor().getName()</code>.
	 */
	public String getFacetName();

	/**
	 * Return the <b>profile</b> that requested for the facet
	 */
	public IProfile getProfile();
	
	/**
	 * Return the <b>targetObject</b> for the facet
	 */
	public Object getTargetObject();
	
	/**
	 * Return the facet descriptor that was used to lookup the facet
	 * @return
	 */
	public FacetDescriptor getFacetDescriptor();
	

}
