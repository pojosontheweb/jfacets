package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import net.sf.woko.facets.read.ViewPropertyValuePropertySpecific;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;

@FacetKeyList(keys = {
    @FacetKey(name="viewPropertyValue_active",
	    	profileId="ROLE_WOKO_DEVELOPER",
		    targetObjectType= WokoDbGroovyFacetDescriptor.class),
    @FacetKey(name="viewPropertyValue_compiling",
            profileId="ROLE_WOKO_DEVELOPER",
            targetObjectType= WokoDbGroovyFacetDescriptor.class)
        })
public class ViewPropValueBoolGroovyFacet extends ViewPropertyValuePropertySpecific {

	@Override
	public Object execute() {
		super.execute();
		return "/WEB-INF/woko/fragments/developer/prop-boolean-icon.jsp";
	}
	
}
