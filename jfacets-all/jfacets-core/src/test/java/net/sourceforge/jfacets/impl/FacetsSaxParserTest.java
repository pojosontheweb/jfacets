package net.sourceforge.jfacets.impl;

import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;
import net.sourceforge.jfacets.FacetDescriptor;

public class FacetsSaxParserTest extends TestCase {

	public void testGetDescriptors() {
		InputStream is = getClass().getResourceAsStream("/test-facets.xml");
		FacetsSaxParser p = null;
		try {
			p = new FacetsSaxParser(is);
		} catch (Exception e) {			
			e.printStackTrace();
			fail("Exception caught : " + e);
		}
		assertNotNull(p);
		List<FacetDescriptor> descriptors = p.getDescriptors();
		assertNotNull(descriptors);
		assertEquals(descriptors.size(), 3);
	}

}
