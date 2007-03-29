package net.sf.woko.actions;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Pre-action for the /WEB-INF/woko/createObject.jsp page.
 * Triggered from nav bar.
 */
@UrlBinding("/tools/create-object.action")
public class CreateObjectActionBean extends BaseActionBean {

	@DefaultHandler
	public Resolution display() {
		return new ForwardResolution("/WEB-INF/woko/createObject.jsp");
	}
	
}
