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

	private final String facetName;
	private final IProfile profile;
	private final Object targetObject;
    private final Class<?> targetObjectClass;
	private final FacetDescriptor facetDescriptor;

    public DefaultFacetContext(String facetName, IProfile profile, Object targetObject, Class<?> targetObjectClass, FacetDescriptor facetDescriptor) {
        this.facetName = facetName;
        this.profile = profile;
        this.targetObject = targetObject;
        this.targetObjectClass = targetObjectClass;
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

    public Class<?> getTargetObjectClass() {
        return targetObjectClass;
    }
}
