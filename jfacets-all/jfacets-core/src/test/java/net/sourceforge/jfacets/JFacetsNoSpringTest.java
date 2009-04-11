package net.sourceforge.jfacets;

import junit.framework.TestCase;
import net.sourceforge.jfacets.impl.FacetDescriptorManager;
import net.sourceforge.jfacets.impl.DefaultFacetContextFactory;
import net.sourceforge.jfacets.impl.DefaultFacetFactory;
import net.sourceforge.jfacets.impl.FacetRepositoryImpl;
import net.sourceforge.jfacets.simpleprofiles.SimpleProfileRepository;
import net.sourceforge.jfacets.instancefacets.FacetCollection;
import net.sourceforge.jfacets.instancefacets.ListWith1Item;
import net.sourceforge.jfacets.instancefacets.ArrayListWith2Items;

import java.util.ArrayList;

/**
 * Tests JFacets without Spring wiring (pure J2SE, no deps)
 */
public class JFacetsNoSpringTest extends TestCase {

    private JFacets jFacets;

    @Override
    protected void setUp() throws Exception {
        jFacets = new JFacetsBuilder(
                new SimpleProfileRepository(),
                new FacetDescriptorManager("/test-instance-facets.xml")).
                build();
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
        assertEquals(facet.getClass(), FacetCollection.class);
    }

    public void testGetFacetArrayList1Elem() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("elem1");
        Object facet = jFacets.getFacet("test", "ivar", al);
        assertNotNull(facet);
        assertEquals(facet.getClass(), ListWith1Item.class);
    }

    public void testGetFacetArrayList2Elems() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("elem1");
        al.add("elem2");
        Object facet = jFacets.getFacet("test", "ivar", al);
        assertNotNull(facet);
        assertEquals(facet.getClass(), ArrayListWith2Items.class);
    }

}
