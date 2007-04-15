package net.sourceforge.jfacets.impl;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IProfile;

/**
 * Default facet context implementation
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class DefaultFacetContext implements IFacetContext {

	private String facetName;
	private IProfile profile;
	private Object targetObject;
	private FacetDescriptor facetDescriptor;
	
	public DefaultFacetContext(String facetName, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
		this.facetName = facetName;
		this.profile = profile;
		this.targetObject = targetObject;
		this.facetDescriptor = facetDescriptor;
	}
	
	public String getFacetName() {
		return facetName;
	}

	public IProfile getProfile() {
		return profile;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public FacetDescriptor getFacetDescriptor() {
		return facetDescriptor;
	}

}
