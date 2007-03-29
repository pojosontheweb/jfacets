package net.sf.woko.facets.write;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.SimpleError;
import org.hibernate.Session;
import org.apache.log4j.Logger;

@FacetKey(name="deleteObject", profileId="ROLE_WOKO_USER", targetObjectType=Object.class)
public class DeleteObject extends BaseFacet {

    private static final Logger logger = Logger.getLogger(DeleteObject.class);

    public void delete(ValidationErrors errors) {
        doDelete(errors);        
    }

    /**
     * Invoked to decide wether or not the target object can be deleted.
     * Override this method in your facets in order to allow/disable delete of objects.
     * This implementation returns true if the target object is a persistent
     * entity.
     */
    public boolean isDeleteEnabled() {
        return Util.isEntity(getTargetObject());
    }

	protected final void doDelete(ValidationErrors errors) {
        try {
            Object object = getTargetObject();
            logger.debug("Attempting to delete " + object);
            Session s = Util.getSession();
            s.delete(object);
            Util.commit();
            logger.info("Object " + object + " deleted");
        } catch(Throwable t) {
            logger.error("Unable to delete, throwable caught", t);
            errors.addGlobalError(new SimpleError("woko.delete.failed"));
        }
    }

}
