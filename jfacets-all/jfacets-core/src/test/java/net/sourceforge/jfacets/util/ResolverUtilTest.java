package net.sourceforge.jfacets.util;

import junit.framework.TestCase;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.annotations.FacetKey;

import java.util.Set;

public class ResolverUtilTest extends TestCase {

    public void testResolveIFacetClasses() {
        ResolverUtil<IFacet> resolverUtil = new ResolverUtil<IFacet>();
        resolverUtil.findImplementations(IFacet.class, "net.sourceforge.jfacets.annotations.examples");
        Set<Class<? extends IFacet>> facetClasses = resolverUtil.getClasses();
        assertEquals("invalid number of IFacet implementations in package", 4, facetClasses.size());
    }


    public void testResolveAnnotatedClasses() {
        ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
        resolverUtil.findAnnotated(FacetKey.class, "net.sourceforge.jfacets.annotations.examples");
        Set<Class<? extends Object>> facetClasses = resolverUtil.getClasses();
        assertEquals("invalid number of IFacet implementations in package", 3, facetClasses.size());
    }

}
