package net.sf.woko.facets.devel;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;
import net.sf.woko.facets.write.EditObjectProperties;

@FacetKey(name = "editProperties", 
		profileId = "ROLE_WOKO_DEVELOPER", 
		targetObjectType = WokoDbGroovyFacetDescriptor.class)
public class EditPropsDbFacet extends EditObjectProperties {

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getPropertyNames() {
		super.execute();
		ArrayList res = new ArrayList<String>();
		res.add("active");
		res.add("name");
		res.add("profileId");
		res.add("targetObjectClassName");
		res.add("compiling");
		res.add("facetClass");
		res.add("facet");
		res.add("sourceCode");
		return res;
	}
}
