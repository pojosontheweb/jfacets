package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.read.ViewPropertyValuePropertySpecific;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;

@FacetKey(name="viewPropertyValue_sourceCode",
		profileId="ROLE_WOKO_DEVELOPER",
		targetObjectType= WokoDbGroovyFacetDescriptor.class)
public class ViewPropValueSourceCodeGroovyFacet extends ViewPropertyValuePropertySpecific {

	public Object execute() {
		super.execute();
        return "/WEB-INF/woko/fragments/developer/prop-java-source.jsp";
	}

}