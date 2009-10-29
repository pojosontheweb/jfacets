import net.sourceforge.jfacets.*;

class TestFacetList implements IFacet, IInstanceFacet {

	IFacetContext facetContext;
	
	public boolean matchesTargetObject(Object targetObject) {
		return targetObject.size()==1;
	}

}  