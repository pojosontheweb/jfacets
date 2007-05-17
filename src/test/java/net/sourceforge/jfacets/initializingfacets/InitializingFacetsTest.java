package net.sourceforge.jfacets.initializingfacets;

import org.apache.log4j.BasicConfigurator;

import net.sourceforge.jfacets.JFacets;
import junit.framework.TestCase;

/**
 * Tests retrievement of instance facets for 
 * Long and String objects. See the instanceFacetsAppCtx.xml test 
 * context, and the test-instance-facets folder, for more infos.
 */
public class InitializingFacetsTest extends TestCase {
	
	private JFacets jFacets;
	
	private static final String CONTEXT_PATH = "initFacetsAppCtx.xml";
	
//	static {
//		BasicConfigurator.configure();
//	}
	
	protected void setUp() throws Exception {
		super.setUp();
		jFacets = JFacets.get(CONTEXT_PATH);
		assertNotNull(jFacets);
	}

	public void testInitFacet() {
		BaseInitFacet f = (BaseInitFacet)jFacets.getFacet("test", "ivar", "blah");
		assertNotNull(f);
		assertTrue(f.isInitialized());
	}
}
