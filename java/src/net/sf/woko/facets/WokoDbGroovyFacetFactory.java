package net.sf.woko.facets;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetFactory;
import net.sourceforge.jfacets.impl.DefaultFacetFactory;

/**
 * The Woko DB/Groovy facet factory : creates groovy facets 
 * from WokoDbGroovyFacetDescriptors.
 * @see WokoDbGroovyFacetDescriptor
 * @see WokoDbFacetsDescriptorManager
 */
public class WokoDbGroovyFacetFactory implements IFacetFactory {

	/**
	 * The default factory, used to create annotated facets.
	 */
	private final DefaultFacetFactory defaultFactory = new DefaultFacetFactory();

	public Object createFacet(FacetDescriptor fd) {
		if (fd instanceof WokoDbGroovyFacetDescriptor)
			return ((WokoDbGroovyFacetDescriptor)fd).getFacet();
		else 
			return defaultFactory.createFacet(fd);
	}

}
