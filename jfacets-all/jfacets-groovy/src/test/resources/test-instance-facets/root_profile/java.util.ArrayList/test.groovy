import net.sourceforge.jfacets.*;

class TestFacetArrayList implements IFacet, IInstanceFacet {

	IFacetContext facetContext;
	
	public boolean matchesTargetObject(Object targetObject) {
		return targetObject.size()==2;
	}

}  