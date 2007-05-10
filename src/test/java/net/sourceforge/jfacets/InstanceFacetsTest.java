package net.sourceforge.jfacets;

import net.sourceforge.jfacets.JFacets;
import junit.framework.TestCase;

/**
 * Tests retrievement of instance facets for 
 * Long and String objects. See the instanceFacetsAppCtx.xml test 
 * context, and the test-instance-facets folder, for more infos.
 */
public class InstanceFacetsTest extends TestCase {
	
	private JFacets jFacets;
	
	private static final String CONTEXT_PATH = "instanceFacetsAppCtx.xml";
	
	protected void setUp() throws Exception {
		super.setUp();
		jFacets = JFacets.get(CONTEXT_PATH);
		assertNotNull(jFacets);
	}

	public void test1GetFacetLongPositive() {
		Long l = 10l; 
		Object facet = jFacets.getFacet("test", "ivar", l);
		assertNotNull(facet);
	}

	public void test2GetFacetLongNegative() {
		Long l = -10l; 
		Object facet = jFacets.getFacet("test", "ivar", l);
		assertNull(facet);
	}

	public void test1GetFacetStringHuge() {
		String s = "this is a huuuuuuuuuuuuge string, yes it is !"; 
		Object facet = jFacets.getFacet("test", "ivar", s);
		assertNotNull(facet);
	}

	public void test1GetFacetStringSmall() {
		String s = "short!";
		Object facet = jFacets.getFacet("test", "ivar", s);
		assertNull(facet);
	}

}
