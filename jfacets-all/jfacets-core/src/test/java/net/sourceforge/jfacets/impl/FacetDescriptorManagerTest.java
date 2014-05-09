package net.sourceforge.jfacets.impl;

import junit.framework.TestCase;

public class FacetDescriptorManagerTest extends TestCase {

	private FacetDescriptorManager manager;
	
	public void testLoadAndGetDescriptors() {
		try {
			manager = new FacetDescriptorManager("test-facets.xml");
			assertNotNull(manager);
		} catch (Exception e) {
			fail("Caught exception " + e);
			e.printStackTrace();
		}
		assertNotNull(manager.getDescriptors());
		assertEquals(manager.getDescriptors().size(), 3);
	}
	
}
