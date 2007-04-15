package net.sourceforge.jfacets.impl;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetFactory;

/**
 * Default facet factory implementation : creates facets using <code>Class.newInstance()</code>.
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class DefaultFacetFactory implements IFacetFactory {

	private static final Logger logger = Logger.getLogger(DefaultFacetFactory.class);
	
	/**
	 * Creates the facet for passed descriptor using <code>Class.newInstance()</code>. 
	 * Return null and logs an ERROR if an error occured while invoking no-args constructor. 
	 */
	public Object createFacet(FacetDescriptor d) {
		if (logger.isDebugEnabled()) logger.debug("createFacet() : attempting to create facet for descriptor " + d + "...");
		Class facetClass = d.getFacetClass();
		// null-check
		if (facetClass == null) {
			logger.error("createFacet() : facetClass is null in supplied descriptor !");
			return null;
		}
		// let's create the facet instance using no-args constructor
		try {
			Object facet = facetClass.newInstance();
			if (logger.isDebugEnabled()) logger.debug("createFacet() : facet created OK, returning " + facet);
			return facet;
		} catch (Throwable e) {
			logger.error("createFacet() : unable to create using no-args constructor, exception caught !", e);
			return null;
		}
	}

}
