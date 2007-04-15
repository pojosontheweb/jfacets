package net.sourceforge.jfacets.examples.command;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

public abstract class BaseCommandFacet implements IFacet, IExecutable {

	private IFacetContext ctx;
	
	public void setContext(IFacetContext ctx) {
		this.ctx = ctx;
	}

	public IFacetContext getContext() {
		return ctx;
	}

	public abstract Object execute();
	
}
