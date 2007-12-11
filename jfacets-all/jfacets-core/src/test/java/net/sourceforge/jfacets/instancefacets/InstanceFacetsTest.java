package net.sourceforge.jfacets.instancefacets;

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import net.sourceforge.jfacets.JFacets;
import net.sourceforge.jfacets.JFacetsSpringTestBase;
import junit.framework.TestCase;

/**
 * Tests retrievement of instance facets for 
 * Long and String objects. See the instanceFacetsAppCtx.xml test 
 * context, and the test-instance-facets folder, for more infos.
 */
public class InstanceFacetsTest extends JFacetsSpringTestBase {
	
	public InstanceFacetsTest() {
		super("instanceFacetsAppCtx.xml");
	}
	
	public void testGetFacetLongPositive() {
		Long l = 10l; 
		Object facet = jFacets.getFacet("test", "ivar", l);
		assertNotNull(facet);
	}

	public void testGetFacetLongNegative() {
		Long l = -10l; 
		Object facet = jFacets.getFacet("test", "ivar", l);
		assertNull(facet);
	}

	public void testGetFacetStringHuge() {
		String s = "this is a huuuuuuuuuuuuge string, yes it is !"; 
		Object facet = jFacets.getFacet("test", "ivar", s);
		assertNotNull(facet);
	}

	public void testGetFacetStringSmall() {
		String s = "short!";
		Object facet = jFacets.getFacet("test", "ivar", s);
		assertNull(facet);
	}
	
	public void testGetFacetArrayList0Elems() {
		ArrayList<String> al = new ArrayList<String>();
		Object facet = jFacets.getFacet("test", "ivar", al);
		assertNotNull(facet);
		assertEquals(facet.getClass().getName(), "TestFacetCollection");
	}
	
	public void testGetFacetArrayList1Elem() {
		ArrayList<String> al = new ArrayList<String>();
		al.add("elem1");
		Object facet = jFacets.getFacet("test", "ivar", al);
		assertNotNull(facet);
		assertEquals(facet.getClass().getName(), "TestFacetList");
	}
	
	public void testGetFacetArrayList2Elems() {
		ArrayList<String> al = new ArrayList<String>();
		al.add("elem1");
		al.add("elem2");
		Object facet = jFacets.getFacet("test", "ivar", al);
		assertNotNull(facet);
		assertEquals(facet.getClass().getName(), "TestFacetArrayList");
	}

}
