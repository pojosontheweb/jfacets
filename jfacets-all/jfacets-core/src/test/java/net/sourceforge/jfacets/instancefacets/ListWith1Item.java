package net.sourceforge.jfacets.instancefacets;

import java.util.List;

public class ListWith1Item extends BaseInstanceFacet {

    public boolean matchesTargetObject(Object targetObject) {
        List l = (List)targetObject;
        return l.size() == 1;
    }
}
