import net.sourceforge.jfacets.*;

class TestFacetLong implements IFacet, IInstanceFacet {

	IFacetContext context;
	
	public boolean matchesTargetObject(Object targetObject) {
		return targetObject >= 0;
	}

}  