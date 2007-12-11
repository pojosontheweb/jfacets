package net.sourceforge.jfacets;

public class PerformanceTest extends JFacetsSpringTestBase {
	
	public PerformanceTest() {
		super("jFacetsTestAppCtx.xml");
	}

	public void testLotsOfInvocations() {
		for(long i=0 ; i<10000 ; i++) {
			jFacets.getFacet("test", "ivar", new Long(10));
			jFacets.getFacet("test", "john", new Long(10));
		}
	}


}
