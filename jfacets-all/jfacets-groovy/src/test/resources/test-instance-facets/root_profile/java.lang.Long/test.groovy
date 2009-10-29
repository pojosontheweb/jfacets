import net.sourceforge.jfacets.*;

class TestFacetLong implements IFacet, IInstanceFacet {

	IFacetContext facetContext;
	
	public boolean matchesTargetObject(Object targetObject) {
		return targetObject >= 0;
	}

}  