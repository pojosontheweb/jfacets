package net.sourceforge.jfacets.impl;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacet;
import junit.framework.TestCase;

public class DefaultFacetFactoryTest extends TestCase {

	public void testCreateDefaultFacet() {
		DefaultFacetFactory ff = new DefaultFacetFactory();
		FacetDescriptor fd = new FacetDescriptor();
		fd.setName("test");
		fd.setProfileId("john");
		fd.setTargetObjectType(Object.class);
		fd.setFacetClass(DefaultFacet.class);
		Object facet = ff.createFacet(fd);
		assertNotNull(facet);		
	}
	
}
