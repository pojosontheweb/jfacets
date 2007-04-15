package net.sourceforge.jfacets.examples.hello;

import java.util.Date;

import net.sourceforge.jfacets.JFacets;

public class Main {

	public static void main(String[] args) {
		
		// create the target object (a dummy date)
		Date targetObject = new Date();		

		// get the JFacets bean
		JFacets jFacets = JFacets.get("net/sourceforge/jfacets/examples/hello/helloFacetsAppCtx.xml");

		// get the hello facet for ivar (standard_role)...
		BaseHelloFacet helloFacet =
			(BaseHelloFacet)jFacets.getFacet("hello", "ivar", targetObject);
		System.out.println("Facet obtained for (hello,john,Date) : " + helloFacet);
		// and invoke hello()
		String res = helloFacet.hello();
		System.out.println("helloFacet.hello() = " + res);
		
		// get the hello facet for john (admin_role)
		helloFacet =
			(BaseHelloFacet)jFacets.getFacet("hello", "john", targetObject);
		System.out.println("Facet obtained for (hello,john,Date) : " + helloFacet);
		// and invoke hello()
		res = helloFacet.hello();
		System.out.println("helloFacet.hello() = " + res);
		
	}

}
