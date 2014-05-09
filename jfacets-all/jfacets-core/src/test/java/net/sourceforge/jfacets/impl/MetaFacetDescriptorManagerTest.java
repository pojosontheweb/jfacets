package net.sourceforge.jfacets.impl;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;

import junit.framework.TestCase;

public class MetaFacetDescriptorManagerTest extends TestCase {

	private MetaFacetDescriptorManager metaManager;
	private FacetDescriptorManager xmlManager;
	
	protected void setUp() throws Exception {
		super.setUp();
		xmlManager = new FacetDescriptorManager("test-facets.xml");
		metaManager = new MetaFacetDescriptorManager();
		ArrayList<IFacetDescriptorManager> managers = 
			new ArrayList<IFacetDescriptorManager>();
		managers.add(xmlManager);
		metaManager.setManagers(managers);
	}

	public void testGetDescriptor() {
		FacetDescriptor fd = metaManager.getDescriptor("test", "root_profile", Object.class);
		assertNotNull(fd);
		fd = metaManager.getDescriptor("test", "admin_role", Object.class);
		assertNotNull(fd);
	}

	public void testGetDescriptors() {
		List<FacetDescriptor> descriptors = metaManager.getDescriptors();
		assertNotNull(descriptors);
		assertEquals(3,	descriptors.size());
	}

	public void testGetManagers() {
		List<IFacetDescriptorManager> managers = metaManager.getManagers();
		assertNotNull(managers);
		assertEquals(1, managers.size());
		assertTrue(managers.contains(xmlManager));
	}

}
