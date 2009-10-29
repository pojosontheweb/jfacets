package net.sourceforge.jfacets.annotations.examples;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

public abstract class BaseTestFacet implements IFacet {

	private IFacetContext ctx;
	
	public IFacetContext getFacetContext() {
		return ctx;
	}

	public void setFacetContext(IFacetContext ctx) {
		this.ctx = ctx;
	}

}
