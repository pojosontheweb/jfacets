package net.sf.woko.facets.write;

import net.sourceforge.jfacets.IFacetContext;

import java.beans.PropertyDescriptor;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 20 févr. 2007
 * Time: 16:34:52
 * To change this template use File | Settings | File Templates.
 */
public class EditPropertyValuePropertySpecific extends EditPropertyValue {

    private static final Logger logger = Logger.getLogger(EditPropertyValuePropertySpecific.class);

    /**
	 * Sets prop val from the target object, and binds itself with the
     * "viewPropertyValue" name...
	 */
	@Override
	public void setContext(IFacetContext context) {
		super.setContext(context);
        Object propOwner = getTargetObject();
        PropertyDescriptor pd = getPropertyDescriptor();
        try {
            propVal = pd.getReadMethod().invoke(propOwner, new Object[0]);
        } catch (Exception e) {
            logger.error("Unable to retrieve property value for prop " + pd.getName() +
                    " of object " + propOwner + " ! setting to null");
            propVal = null;
        }
        getRequest().setAttribute("editPropertyValue", this);
    }
}
