package net.sourceforge.jfacets;

/**
 * Interface to be implemented by instance facets. Such facets are assigned to 
 * a target object type <b>and</b> must match a given instance in order to be returned 
 * by JFacets to clients.
 * <br/>
 * <b>IMPORTANT</b> : You can't assign 2 instance facets with the same name to the same 
 * target type and profile, as this would introduce a duplicate in the facet descriptors. 
 * During lookup, the Facet Repository will stop climbing the 
 * types/profiles graph at the first facet found. Then, only, instance matching is 
 * checked.
 */
public interface IInstanceFacet {
	
	/**
	 * Invoked on the facet just after creation (and assignation of the facet 
	 * context if the facet also implements <code>IFacet</code>) to check 
	 * wether this instance facet matches the target object or not. 
	 * the target object, <code>false</code> otherwise. <br/>
	 * @param targetObject The target object of the facet, as passed to <code>jfacets.getFacet(...)</code>. <b>CAN BE NULL</b> in case of facets retrieved only by their type (with a null target object)
	 * @return <code>true</code> if this instance facet matches 
	 */
	public boolean matchesTargetObject(Object targetObject);

}
