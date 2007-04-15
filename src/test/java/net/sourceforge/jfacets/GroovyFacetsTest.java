package net.sourceforge.jfacets;

import net.sourceforge.jfacets.JFacets;

import junit.framework.TestCase;

/**
 * ugly ! should write real unit tests !
 */
public class GroovyFacetsTest extends TestCase {
	
	private JFacets jFacets;
	
	protected void setUp() throws Exception {
		super.setUp();
		jFacets = JFacets.get("groovyFacetsAppCtx.xml");
		assertNotNull(jFacets);
	}

	public void testGetFacet() {
		Object facet = jFacets.getFacet("test", "john", new Long(3));
		assertNotNull(facet);
	}

	public void testExecFacet() {
		Object res = jFacets.execFacet("test", "ivar", new Long(3));
		assertNotNull(res);
	}

	public void testGetFacetRepository() {
		assertNotNull(jFacets.getProfileRepository());
	}

	public void testGetProfileRepository() {
		assertNotNull(jFacets.getFacetRepository());
	}

}
