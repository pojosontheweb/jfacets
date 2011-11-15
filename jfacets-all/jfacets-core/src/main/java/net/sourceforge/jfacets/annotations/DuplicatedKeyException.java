package net.sourceforge.jfacets.annotations;

/**
 * Thrown when duplicated facet keys are found.
 */
public class DuplicatedKeyException extends RuntimeException {

    private final String name;
    private final String profileId;
    private final Class<?> targetObjectType;

    public DuplicatedKeyException(String name, String profileId, Class<?> targetObjectType) {
        super("The following facet key has already been added, it is duplicated : name=" + name + ", profileId=" +
          profileId + ", targetObjectType=" + targetObjectType + "). You can use another duplicated key policy if " +
            "you want the first key to be used only and others simply ignored instead of having an exception thrown.");
        this.name = name;
        this.profileId = profileId;
        this.targetObjectType = targetObjectType;
    }

    public String getName() {
        return name;
    }

    public String getProfileId() {
        return profileId;
    }

    public Class<?> getTargetObjectType() {
        return targetObjectType;
    }
}
