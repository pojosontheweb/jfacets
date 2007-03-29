package net.sf.woko.facets.user;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.util.IWokoInternal;
import net.sf.woko.facets.NotGrantedFacet;

@FacetKey(name = "edit", profileId = "ROLE_WOKO_USER", targetObjectType = IWokoInternal.class)
public class EditWokoInternal extends NotGrantedFacet {
}
