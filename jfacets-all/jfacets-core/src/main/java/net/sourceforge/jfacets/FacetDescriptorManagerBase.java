package net.sourceforge.jfacets;

import net.sourceforge.jfacets.log.JFacetsLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FacetDescriptorManagerBase implements IFacetDescriptorManager {

    private static final JFacetsLogger logger = JFacetsLogger.getLogger(FacetDescriptorManagerBase.class);

    /**
     * an array of all loaded descriptors
     */
    private List<FacetDescriptor> descriptors = null;

    /**
     * returns an array of all loaded descriptors
     */
    public List<FacetDescriptor> getDescriptors() {
        return descriptors;
    }

    protected void setDescriptors(List<FacetDescriptor> descriptors) {
        this.descriptors = Collections.unmodifiableList(new ArrayList<FacetDescriptor>(descriptors));
    }

    /**
     * returns the descriptor for passed parameters, null if not found.
     * strict match : does not handle inheritance
     */
    public List<FacetDescriptor> getDescriptors(String name, String profileId, Class targetObjectType) {
        if (logger.isDebugEnabled()) {
            logger.debug("getDescriptors() : trying to get descriptors for name='" + name + "' profileId='" + profileId + "' " +
                    "objType='" + targetObjectType.getName() + "' ...");
        }
        List<FacetDescriptor> result = new ArrayList<FacetDescriptor>();
        for (FacetDescriptor descriptor : descriptors) {
            if ((name.equals(descriptor.getName())) &&
                    (profileId.equals(descriptor.getProfileId())) &&
                    (targetObjectType.getName().equals(descriptor.getTargetObjectType().getName()))) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getDescriptors() : descriptor found : " + descriptor);
                }
                result.add(descriptor);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getDescriptors() : ... returning " + result.size() + " descriptor(s)");
        }
        return result;
    }

}
