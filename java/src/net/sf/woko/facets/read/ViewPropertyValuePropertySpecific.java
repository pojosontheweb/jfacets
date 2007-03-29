package net.sf.woko.facets.read;

import net.sourceforge.jfacets.IFacetContext;

import java.beans.PropertyDescriptor;

import org.apache.log4j.Logger;

public class ViewPropertyValuePropertySpecific extends ViewPropertyValue {

    private static final Logger logger = Logger.getLogger(ViewPropertyValuePropertySpecific.class);

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
        getRequest().setAttribute("viewPropertyValue", this);
    }
}
