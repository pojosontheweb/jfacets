package net.sourceforge.jfacets.examples.command;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

public abstract class BaseCommandFacet implements IFacet {

	private IFacetContext ctx;
	
	public void setFacetContext(IFacetContext ctx) {
		this.ctx = ctx;
	}

	public IFacetContext getFacetContext() {
		return ctx;
	}

	public abstract Object execute();

}
