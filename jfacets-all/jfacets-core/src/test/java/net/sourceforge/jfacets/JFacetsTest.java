package net.sourceforge.jfacets;

import net.sourceforge.jfacets.JFacets;
import junit.framework.TestCase;

/**
 * Very basic unit test that checks if the profile repo and facet repo
 * are well assembled when wiring with Spring.
 */
public class JFacetsTest extends JFacetsSpringTestBase {
	
	public JFacetsTest() {
		super("jFacetsTestAppCtx.xml");
	}

	public void test1GetFacetRepository() {
		assertNotNull(jFacets.getProfileRepository());
	}

	public void test2GetProfileRepository() {
		assertNotNull(jFacets.getFacetRepository());
	}

}
