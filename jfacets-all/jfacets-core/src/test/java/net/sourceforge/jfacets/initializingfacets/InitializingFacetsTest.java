package net.sourceforge.jfacets.initializingfacets;

import net.sourceforge.jfacets.JFacetsSpringTestBase;

/**
 * Test for instance facets : verifies that facets implementing
 * IInstanceFacet are well initialized when obtained from the
 * JFacets bean.
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
