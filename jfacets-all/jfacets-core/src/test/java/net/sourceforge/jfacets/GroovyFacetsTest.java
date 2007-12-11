package net.sourceforge.jfacets;

/**
 * ugly ! should write real unit tests !
 */
public class GroovyFacetsTest extends JFacetsSpringTestBase {
	
	public GroovyFacetsTest() {
		super("groovyFacetsAppCtx.xml");
	}

	public void testGetFacet() {
		Object facet = jFacets.getFacet("test", "john", new Long(3));
		assertNotNull(facet);
		assertEquals("invalid class name", "MyGroovyTestFacet1", facet.getClass().getSimpleName());
		facet = jFacets.getFacet("test", "standard_role", new Long(3));
		assertNotNull(facet);
		assertEquals("invalid class name", "MyGroovyTestFacet2", facet.getClass().getSimpleName());
	}

	public void testGetFacetRepository() {
		assertNotNull(jFacets.getProfileRepository());
	}

	public void testGetProfileRepository() {
		assertNotNull(jFacets.getFacetRepository());
	}

}
