package net.sf.woko.facets.user;

import net.sf.woko.facets.NotGrantedFacet;
import net.sf.woko.util.IWokoInternal;
import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name = "view", profileId = "ROLE_WOKO_USER", targetObjectType = IWokoInternal.class)
public class ViewWokoInternal extends NotGrantedFacet {
}
