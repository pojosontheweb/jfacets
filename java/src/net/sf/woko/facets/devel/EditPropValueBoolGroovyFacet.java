package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.write.EditPropertyValuePropertySpecific;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;

@FacetKey(name="editPropertyValue_active",
	    	profileId="ROLE_WOKO_DEVELOPER",
		    targetObjectType=WokoDbGroovyFacetDescriptor.class)
public class EditPropValueBoolGroovyFacet extends EditPropertyValuePropertySpecific {

	public Object execute() {
		super.execute();
        return "/WEB-INF/woko/fragments/developer/prop-boolean-icon-edit.jsp";
	}

}