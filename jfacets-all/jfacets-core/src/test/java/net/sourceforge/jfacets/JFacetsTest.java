package net.sourceforge.jfacets;

import net.sourceforge.jfacets.JFacets;
import junit.framework.TestCase;

/**
 * ugly ! should write real unit tests !
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
