package net.sourceforge.jfacets.impl;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IProfile;

/**
 * Default facet context factory implementation
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class DefaultFacetContextFactory implements IFacetContextFactory {

	private static final Logger logger = Logger.getLogger(DefaultFacetContextFactory.class);
	
	/**
	 * Creates a DefaultFacetContext for passed parameters, with a null facetDescriptor.
	 * @deprecated use the one that takes the facetDescriptor as the 4th param 
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject) {
		return create(facetName, profile, targetObject, null);
	}

	/**
	 * Creates a DefaultFacetContext for passed parameters. 
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
		if (logger.isDebugEnabled()) logger.debug("create() : creating default context...");
		DefaultFacetContext ctx = new DefaultFacetContext(facetName, profile, targetObject, facetDescriptor);
		if (logger.isDebugEnabled()) logger.debug("create() : OK returning " + ctx);
		return ctx;
	}

}
