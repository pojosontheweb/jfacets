package net.sf.woko.facets.write;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.SimpleError;

/**
 * Facet used to save and update objects in hibernate. 
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : saveObject</li>
 * <li><b>profileId</b> : ROLE_WOKO_USER</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="saveObject",profileId="ROLE_WOKO_USER")
public class SaveObject extends BaseFacet {

	private static final Logger logger = Logger.getLogger(SaveObject.class);
	
	/**
	 * Invoked when an object has to be saved or updated. 
	 * You can override this method to behave differently or 
	 * perform custom (facet-based) validation if needed.<br/>
	 * Here, we simply invoke doSave().
	 * @param isNew save or update ?
	 * @param errors the Stripes validation errors to be used
	 */
	public void save(boolean isNew, ValidationErrors errors) {
		doSave(isNew, errors);
	}

    /**
     * Invoked to decide wether or not the target object can be saved or not.
     * Override this method in your facets in order to allow/disable edition and
     * saving of objects.
     * This implementation returns true if the target object is a persistent
     * entity.
     */
    public boolean isSaveEnabled() {
        return Util.isEntity(getTargetObject());
    }

    /**
	 * Saves or updates the object in current hibernate session.
	 * @param isNew save or update ?
	 */
	protected final void doSave(boolean isNew, ValidationErrors errors) {
        try {
            Object object = getTargetObject();
            logger.debug("Attempting to save " + object + " (isNew=" + isNew + ")");
            Session s = Util.getSession();
            if (isNew)
                s.save(object);
            else
                s.update(object);
            Util.commit();
            logger.info("Object " + object + " saved");
        } catch(Throwable t) {
            logger.error("Unable to save, throwable caught !", t);
            errors.addGlobalError(new SimpleError("woko.save.failed"));
        }
    }
	
}
