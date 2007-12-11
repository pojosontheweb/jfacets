package net.sourceforge.jfacets.impl;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * A default facet that does nothing !
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com) 
 */
public class DefaultFacet implements IFacet {

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(DefaultFacet.class);
	
	private IFacetContext ctx;
	
	public void setContext(IFacetContext ctx) {
		this.ctx = ctx;
		if (logger.isDebugEnabled()) logger.debug("Context set : " + ctx);
	}

	public IFacetContext getContext() {
		return ctx;
	}

}
