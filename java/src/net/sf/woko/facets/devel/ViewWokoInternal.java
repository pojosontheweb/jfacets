package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.util.IWokoInternal;
import net.sf.woko.facets.read.ViewObject;

@FacetKey(name = "view", profileId = "ROLE_WOKO_DEVELOPER", targetObjectType = IWokoInternal.class)
public class ViewWokoInternal extends ViewObject {
}
