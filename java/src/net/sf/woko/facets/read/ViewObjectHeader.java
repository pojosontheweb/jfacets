package net.sf.woko.facets.read;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;

/**
 * Facet used to render the header of an object for the WRITE role. 
 * Basically it adds the "edit object" link.
 * fctDesc
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewHeader</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="viewHeader",profileId="ROLE_WOKO_GUEST")
public class ViewObjectHeader extends BaseFacet implements IExecutable {
	
	/**
	 * Set id, className and entity and return the path to the 
	 * fragment to be used :
	 * <code>/WEB-INF/woko/fragments/read/header.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/read/header.jsp";
	}

}