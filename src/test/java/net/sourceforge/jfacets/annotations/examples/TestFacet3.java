package net.sourceforge.jfacets.annotations.examples;

import java.util.Date;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="test", profileId="admin_role", targetObjectType=Date.class)
public class TestFacet3 extends BaseTestFacet {

	public Object execute() {
		System.out.println("Date passed = " + getContext().getTargetObject());
		return null;
	}

}
