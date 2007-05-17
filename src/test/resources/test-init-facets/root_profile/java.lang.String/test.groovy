import net.sourceforge.jfacets.IInitializableFacet;
import net.sourceforge.jfacets.initializingfacets.BaseInitFacet;

public class InitFacetImpl extends BaseInitFacet implements IInitializableFacet {

	public void initializeFacet() {
		setInitialized(true);
	}
	
}
