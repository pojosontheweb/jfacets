package net.sourceforge.jfacets.annotations.issue1;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="testIssue1", profileId = "root_profile", targetObjectType = Integer.class)
public class MyInstanceFacet2 implements IInstanceFacet {

    public boolean matchesTargetObject(Object targetObject) {
        Integer i = (Integer)targetObject;
        return i!=null && i % 2 != 0;
    }
}
