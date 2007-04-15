package net.sourceforge.jfacets;

import net.sourceforge.jfacets.JFacets;
import junit.framework.TestCase;

/**
 * ugly ! should write real unit tests !
 */
public class JFacetsTest extends TestCase {
	
	private JFacets jFacets;
	
	private static final String CONTEXT_PATH = "jFacetsTestAppCtx.xml";
	
	protected void setUp() throws Exception {
		super.setUp();
		jFacets = JFacets.get(CONTEXT_PATH);
		assertNotNull(jFacets);
	}

	public void test1GetFacetRepository() {
		assertNotNull(jFacets.getProfileRepository());
	}

	public void test2GetProfileRepository() {
		assertNotNull(jFacets.getFacetRepository());
	}

}
