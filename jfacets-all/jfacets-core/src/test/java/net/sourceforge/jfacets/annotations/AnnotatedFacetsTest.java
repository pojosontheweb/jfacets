package net.sourceforge.jfacets.annotations;

import net.sourceforge.jfacets.JFacetsSpringTestBase;
import net.sourceforge.jfacets.annotations.examples.TestFacet1;
import net.sourceforge.jfacets.annotations.examples.TestFacet2;
import net.sourceforge.jfacets.annotations.examples.TestFacet3;
import net.sourceforge.jfacets.annotations.pkg2.MyFacet2;

import java.util.Date;

public class AnnotatedFacetsTest extends JFacetsSpringTestBase {

    public AnnotatedFacetsTest() {
        super("annotatedFacetsAppCtx.xml");
    }

    public void testDescriptorsCount() {
        int nbDescriptors = jFacets.getFacetRepository().getFacetDescriptorManager().getDescriptors().size();
        assertEquals("invalid number of descriptors found", 5, nbDescriptors);
    }

    public void testStringIvar() {
        Object facet = jFacets.getFacet("test", "ivar", "yikes");
        assertNotNull("facet is null", facet);
        assertEquals("invalid facet class found", TestFacet1.class, facet.getClass());
    }


    public void testIntegerIvar() {
        Object facet = jFacets.getFacet("test", "ivar", 123);
        assertNotNull("facet is null", facet);
        assertEquals("invalid facet class found", TestFacet2.class, facet.getClass());
    }


    public void testDateIvar() {
        Object facet = jFacets.getFacet("test", "ivar", new Date());
        assertNotNull("facet is null", facet);
        assertEquals("invalid facet class found", TestFacet3.class, facet.getClass());
    }

    public void testDateJohn() {
        Object facet = jFacets.getFacet("test", "john", new Date());
        assertNotNull("facet is null", facet);
        assertEquals("invalid facet class found", TestFacet1.class, facet.getClass());
    }

    public void testIssue1() {
        Object facet1 = jFacets.getFacet("testIssue1", "john", 1);
        Object facet2 = jFacets.getFacet("testIssue1", "john", 2);
        Class<?> c1 = facet1.getClass();
        Class<?> c2 = facet2.getClass();
        assertFalse(c1.equals(c2));
    }

}
