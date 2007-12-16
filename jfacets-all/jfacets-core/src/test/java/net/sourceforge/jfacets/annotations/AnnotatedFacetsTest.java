package net.sourceforge.jfacets.annotations.examples;

import net.sourceforge.jfacets.JFacetsSpringTestBase;

import java.util.Date;

public class AnnotatedFacetsTest extends JFacetsSpringTestBase {

    public AnnotatedFacetsTest() {
        super("annotatedFacetsAppCtx.xml");
    }

    public void testDescriptorsCount() {
        int nbDescriptors = jFacets.getFacetRepository().getFacetDescriptorManager().getDescriptors().length;
        assertEquals("invalid number of descriptors found", 3, nbDescriptors);
    }

    public void testStringIvar() {
        Object facet = jFacets.getFacet("test", "ivar", new String("yikes"));
        assertNotNull("facet is null", facet);
        assertEquals("invalid facet class found", TestFacet1.class, facet.getClass());
    }


    public void testIntegerIvar() {
        Object facet = jFacets.getFacet("test", "ivar", new Integer(123));
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



}
