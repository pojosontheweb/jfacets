package net.sourceforge.jfacets.annotations;

import junit.framework.TestCase;
import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.annotations.pkg1.MyFacet1;
import net.sourceforge.jfacets.annotations.pkg2.MyFacet2;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.Arrays;
import java.util.List;

public class AnnotatedFacetDescriptorManagerTest extends TestCase {

    // not a real test : we don't pass a loader that contains other facets
    // but still we go through the API...
    public void testWithSuppliedClassLoader() {
        AnnotatedFacetDescriptorManager afdm =
                new AnnotatedFacetDescriptorManager(
                    Arrays.asList(
                        "net.sourceforge.jfacets.annotations.pkg2",
                        "net.sourceforge.jfacets.annotations.pkg1"
                    )
                )
                .setClassLoader(getClass().getClassLoader())
                .initialize();
        assertEquals("unexpected number of descriptors found", 2, afdm.getDescriptors().size());
    }

}
