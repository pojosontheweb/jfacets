package net.sourceforge.jfacets.initializingfacets;

import org.apache.log4j.BasicConfigurator;

import net.sourceforge.jfacets.JFacets;
import net.sourceforge.jfacets.JFacetsSpringTestBase;
import junit.framework.TestCase;

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
		BaseInitFacet f = (BaseInitFacet)jFacets.getFacet("test", "ivar", "blah");
		assertNotNull(f);
		assertTrue(f.isInitialized());
	}
}
