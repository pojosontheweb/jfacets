package net.sourceforge.jfacets.initializingfacets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

public class BaseInitFacet implements IFacet {

	private IFacetContext context;
	
	private boolean initialized = false;

	public IFacetContext getContext() {
		return context;
	}

	public void setContext(IFacetContext context) {
		this.context = context;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

}
