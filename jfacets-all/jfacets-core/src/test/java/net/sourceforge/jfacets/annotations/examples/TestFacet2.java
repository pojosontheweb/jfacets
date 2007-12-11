package net.sourceforge.jfacets.annotations.examples;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="test", profileId="root_profile", targetObjectType=Integer.class)
public class TestFacet2 extends BaseTestFacet {

	public Object execute() {
		System.out.println("Integer passed = " + getContext().getTargetObject());
		return null;
	}

}
