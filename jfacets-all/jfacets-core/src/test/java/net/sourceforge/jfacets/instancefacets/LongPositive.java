package net.sourceforge.jfacets.instancefacets;

public class LongPositive extends BaseInstanceFacet {

    public boolean matchesTargetObject(Object targetObject) {
        Long l = (Long)targetObject;
        return l >= 0;
    }
}
