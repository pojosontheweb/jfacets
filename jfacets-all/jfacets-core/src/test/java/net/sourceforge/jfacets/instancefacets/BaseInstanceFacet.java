package net.sourceforge.jfacets.instancefacets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.IFacetContext;

public abstract class BaseInstanceFacet implements IFacet, IInstanceFacet {

    private IFacetContext facetContext;

    public void setFacetContext(IFacetContext ctx) {
        facetContext = ctx;
    }

    public IFacetContext getFacetContext() {
        return facetContext;
    }


    public abstract boolean matchesTargetObject(Object targetObject);
}
