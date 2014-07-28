package net.sourceforge.jfacets.impl;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * Default facet context factory implementation
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class DefaultFacetContextFactory implements IFacetContextFactory {

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(DefaultFacetContextFactory.class);
	
	/**
	 * Creates a DefaultFacetContext for passed parameters. 
	 */
	public IFacetContext create(String facetName, IProfile profile, Object targetObject, Class<?> targetObjectClass, FacetDescriptor facetDescriptor) {
		if (logger.isDebugEnabled()) logger.debug("create() : creating default context...");
		DefaultFacetContext ctx = new DefaultFacetContext(facetName, profile, targetObject, targetObjectClass, facetDescriptor);
		if (logger.isDebugEnabled()) logger.debug("create() : OK returning " + ctx);
		return ctx;
	}

}
