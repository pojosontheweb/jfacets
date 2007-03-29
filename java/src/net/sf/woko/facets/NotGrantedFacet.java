package net.sf.woko.facets;

import net.sourceforge.jfacets.IExecutable;

public class NotGrantedFacet extends BaseFacet implements IExecutable {

    public Object execute() {
        return "/woko/accessDenied.jsp";
    }

}
