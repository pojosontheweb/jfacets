package net.sourceforge.jfacets.instancefacets;

public class HugeString extends BaseInstanceFacet {

    public boolean matchesTargetObject(Object targetObject) {
        String s = (String)targetObject;
        return s.length() > 10;
    }
}
