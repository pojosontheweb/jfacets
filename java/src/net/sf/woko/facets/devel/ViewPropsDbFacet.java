package net.sf.woko.facets.devel;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;
import net.sf.woko.facets.read.ViewObjectProperties;

@FacetKey(name = "viewProperties", 
		profileId = "ROLE_WOKO_DEVELOPER", 
		targetObjectType = WokoDbGroovyFacetDescriptor.class)
public class ViewPropsDbFacet extends ViewObjectProperties {

	@Override
	public List<String> getPropertyNames() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("active");
		res.add("name");
		res.add("profileId");
		res.add("targetObjectClassName");
		res.add("facetClass");
		res.add("compiling");
		res.add("sourceCode");
		return res;
	}
}
