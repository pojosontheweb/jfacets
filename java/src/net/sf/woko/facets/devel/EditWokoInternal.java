package net.sf.woko.facets.devel;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.util.IWokoInternal;
import net.sf.woko.facets.write.EditObject;

@FacetKey(name = "edit", profileId = "ROLE_WOKO_DEVELOPER", targetObjectType = IWokoInternal.class)
public class EditWokoInternal extends EditObject {
}
