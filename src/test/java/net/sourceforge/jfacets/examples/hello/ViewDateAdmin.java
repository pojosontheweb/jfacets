package net.sourceforge.jfacets.examples.hello;

import java.util.Date;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="hello", 
		profileId="admin_role",
		targetObjectType=Date.class)
public class ViewDateAdmin extends BaseHelloFacet {

	public String hello() {
		Date date = (Date)getContext().getTargetObject();
		return "Hello, admin ! here is the time, in ms = " + date.getTime();
	}

}
