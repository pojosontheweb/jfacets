package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;
import net.sf.woko.facets.read.ViewObjectTitle;

@FacetKey(name="viewTitle", 
		profileId="ROLE_WOKO_DEVELOPER",
		targetObjectType=WokoDbGroovyFacetDescriptor.class)
public class ViewDbFacetTitle extends ViewObjectTitle {

	public String getTitle() {
		WokoDbGroovyFacetDescriptor d = (WokoDbGroovyFacetDescriptor)getContext().getTargetObject();
		if (d.getId() != null 
				&& d.getName()!=null
				&& d.getProfileId()!=null
				&& d.getTargetObjectType()!=null) {
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			sb.append(d.getName());
			sb.append(", ");
			sb.append(d.getProfileId());
			sb.append(", ");
			sb.append(d.getTargetObjectType().getName());
			sb.append(")");
			return sb.toString();
		} else {
			return null;
		}
	}

	@Override
	public String getType() {
		return "Dynamic Facet";
	}


}