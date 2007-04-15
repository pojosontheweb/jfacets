package net.sourceforge.jfacets.impl;

import org.apache.log4j.Logger;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

/**
 * A default facet that does nothing !
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com) 
 */
public class DefaultFacet implements IFacet {

	private static final Logger logger = Logger.getLogger(DefaultFacet.class);
	
	private IFacetContext ctx;
	
	public void setContext(IFacetContext ctx) {
		this.ctx = ctx;
		if (logger.isDebugEnabled()) logger.debug("Context set : " + ctx);
	}

	public IFacetContext getContext() {
		return ctx;
	}

}
