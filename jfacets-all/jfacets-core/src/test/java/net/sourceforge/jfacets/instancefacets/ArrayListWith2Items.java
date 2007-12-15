package net.sourceforge.jfacets.instancefacets;

import java.util.List;
import java.util.ArrayList;

public class ArrayListWith2Items extends BaseInstanceFacet {

    public boolean matchesTargetObject(Object targetObject) {
        ArrayList l = (ArrayList)targetObject;
        return l.size() == 2;
    }
}
