package net.sourceforge.jfacets.initializingfacets;

import net.sourceforge.jfacets.JFacetsSpringTestBase;

/**
 * Tests retrievement of instance facets for 
 * Long and String objects. See the instanceFacetsAppCtx.xml test 
 * context, and the test-instance-facets folder, for more infos.
 */
public class InitializingFacetsTest extends JFacetsSpringTestBase {
	
	public InitializingFacetsTest() {
		super("initFacetsAppCtx.xml");
	}
	
	public void testInitFacet() {
		TestInitFacet f = (TestInitFacet)jFacets.getFacet("test", "ivar", "blah");
		assertNotNull(f);
		assertTrue(f.isInitialized());
	}
}
