package net.sourceforge.jfacets;

/**
 * Holds facet assignation parameters :
 * <ul>
 * 	<li><b>name</b> : the name of the facet</li>
 * 	<li><b>profileId</b> : the id of the profile</li>
 * 	<li><b>targetObjectType</b> : the <code>Class</code> of the target object</li>
 * 	<li><b>facetClass</b> : the implementation <code>Class</code> of facet</li>
 * </ul>
 * 
 * @see net.sourceforge.jfacets.IFacetDescriptorManager
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class FacetDescriptor {
	
	/** the profile ID */
	String profileId;
	/** the name */
	String name;
	/** the target object type */
	Class targetObjectType;
	/** the facet class */
	Class facetClass;

	public FacetDescriptor() {
	}

	public String getName() {
		return name;
	}

	public Class getTargetObjectType() {
		return targetObjectType;
	}

	public String getProfileId() {
		return profileId;
	}
	
	public Class getFacetClass() {
		return facetClass;
	}

	public void setFacetClass(Class theFacetClass) {
		facetClass = theFacetClass;
	}

	public void setName(String string) {
		name = string;
	}

	public void setProfileId(String string) {
		profileId = string;
	}

	public void setTargetObjectType(Class class1) {
		targetObjectType = class1;
	}

	public String toString() {
		return "[FacetDescriptor name='" + name + "' profileId='" + profileId + "' objType='" + targetObjectType +
			"' facetClass='" + facetClass + "']";
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FacetDescriptor that = (FacetDescriptor) o;

        if (facetClass != null ? !facetClass.equals(that.facetClass) : that.facetClass != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (profileId != null ? !profileId.equals(that.profileId) : that.profileId != null) return false;
        if (targetObjectType != null ? !targetObjectType.equals(that.targetObjectType) : that.targetObjectType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = profileId != null ? profileId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (targetObjectType != null ? targetObjectType.hashCode() : 0);
        result = 31 * result + (facetClass != null ? facetClass.hashCode() : 0);
        return result;
    }
}
