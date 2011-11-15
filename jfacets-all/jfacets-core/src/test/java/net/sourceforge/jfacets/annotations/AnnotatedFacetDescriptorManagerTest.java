package net.sourceforge.jfacets.annotations;

import junit.framework.TestCase;
import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.annotations.pkg1.MyFacet1;
import net.sourceforge.jfacets.annotations.pkg2.MyFacet2;

import java.util.Arrays;
import java.util.List;

public class AnnotatedFacetDescriptorManagerTest extends TestCase {

    public void testDuplicatesThrowExceptionByDefault() {
        try {
            new AnnotatedFacetDescriptorManager(
              Arrays.asList(
                "net.sourceforge.jfacets.annotations.pkg2",
                "net.sourceforge.jfacets.annotations.pkg1"
              )
            ).
            initialize();
            // should have thrown exception
            fail("duplicated keys : should have thrown !");
        } catch (DuplicatedKeyException e) {
            // normal behavior
        }
    }

    private void testOrdering(Class<?> expectedFacetClass, String... packageNames) {
        AnnotatedFacetDescriptorManager afdm = new AnnotatedFacetDescriptorManager(Arrays.asList(packageNames)).
            setDuplicatedKeyPolicy(DuplicatedKeyPolicyType.FirstScannedWins).
            initialize();
        boolean found = false;
        for (FacetDescriptor fd : afdm.getDescriptors()) {
            if (fd.getName().equals("my")) {
                if (found) {
                    fail("found several facets with name 'my'");
                }
                found = true;
                assertEquals("invalid facet class", expectedFacetClass, fd.getFacetClass());
            }
        }
    }

    public void testPackageOrderingWithDuplicates() {
        testOrdering(
          MyFacet2.class,
          "net.sourceforge.jfacets.annotations.pkg2",
          "net.sourceforge.jfacets.annotations.pkg1");

        testOrdering(
          MyFacet1.class,
          "net.sourceforge.jfacets.annotations.pkg1",
          "net.sourceforge.jfacets.annotations.pkg2");
    }

}
