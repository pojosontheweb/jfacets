import net.sourceforge.jfacets.*;

class TestFacetString implements IFacet, IInstanceFacet {

	IFacetContext facetContext;
	
	public boolean matchesTargetObject(Object targetObject) {
		return targetObject.length() > 10;
	}

}  