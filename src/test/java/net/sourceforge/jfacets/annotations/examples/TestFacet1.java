package net.sourceforge.jfacets.annotations.examples;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="test", profileId="root_profile", targetObjectType=Object.class)
public class TestFacet1 extends BaseTestFacet {

	public Object execute() {
		System.out.println("Hello, this is a test !");
		return null;
	}

}
