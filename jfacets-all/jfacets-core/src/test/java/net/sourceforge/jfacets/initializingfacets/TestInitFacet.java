package net.sourceforge.jfacets.initializingfacets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IInitializableFacet;

public class TestInitFacet implements IFacet, IInitializableFacet {

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

    public void initializeFacet() {
        setInitialized(true);
    }
}
