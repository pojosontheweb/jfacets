package net.sourceforge.jfacets.annotations.examples;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

public abstract class BaseTestFacet implements IFacet {

	private IFacetContext ctx;
	
	public IFacetContext getContext() {
		return ctx;
	}

	public void setContext(IFacetContext ctx) {
		this.ctx = ctx;
	}

}
