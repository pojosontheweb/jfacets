package net.sourceforge.jfacets;

import junit.framework.TestCase;

public class PerformanceTest extends TestCase {
	
	private JFacets jFacets;
	
	private static final String CONTEXT_PATH = "jFacetsTestAppCtx.xml";
	
	protected void setUp() throws Exception {
		super.setUp();
		jFacets = JFacets.get(CONTEXT_PATH);
		assertNotNull(jFacets);
	}

	public void testLotsOfInvocations() {
		for(long i=0 ; i<10000 ; i++) {
			jFacets.getFacet("test", "ivar", new Long(10));
			jFacets.getFacet("test", "john", new Long(10));
		}
	}


}
