package net.sourceforge.jfacets.examples.hello;

import java.util.Date;

import net.sourceforge.jfacets.annotations.FacetKey;

/**
 * Hello Facet for all users (root_profile is the 
 * top-level profile in the profiles hierarchy) and date object.
 */
@FacetKey(name="hello", 
		profileId="root_profile",
		targetObjectType=Date.class)
public class ViewDateRootProfile extends BaseHelloFacet {

	public String hello() {
		// get the facet's target object (a Date)
		Date d = (Date)getContext().getTargetObject();
		// return a dummy message
		return "Hello ! the time is " + d.toString();
	}

}
