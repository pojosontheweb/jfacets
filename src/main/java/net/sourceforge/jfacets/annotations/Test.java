package net.sourceforge.jfacets.annotations;

import java.util.Date;

import net.sourceforge.jfacets.JFacets;

public class Test {

	public static void main(String[] args) {
		
//		BasicConfigurator.configure();

		String profileName = "root_profile";

		JFacets jf = JFacets.get("/annotatedFacetsAppCtx.xml");		
		jf.execFacet("test", profileName, new String("test toto"));
		jf.execFacet("test", profileName, new Integer(3));
		jf.execFacet("test", profileName, new Date());
		
		profileName = "admin_role";

		jf.execFacet("test", profileName, new String("test toto"));
		jf.execFacet("test", profileName, new Integer(3));
		jf.execFacet("test", profileName, new Date());
		
	}
	
	
}
