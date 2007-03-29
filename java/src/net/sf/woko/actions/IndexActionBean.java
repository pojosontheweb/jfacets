package net.sf.woko.actions;

import net.sourceforge.jfacets.web.WebFacets;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Index action : executes the homePageResolution for the current 
 * profile and returns the result. 
 * Allows profiled home pages.
 */
@UrlBinding("/index.action")
public class IndexActionBean extends BaseActionBean {
	
	@DefaultHandler
	public Resolution view() {
		// find the home page resolution using a facet
		WebFacets wf = WebFacets.get(getRequest());
        return (Resolution)wf.execFacet("homePageResolution", new Object(), getRequest());
	}
	
}
