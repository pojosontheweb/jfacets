package net.sf.woko.actions;

import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.LocalizableError;

/**
 * Login action bean : acts as a facade on Acegi so that you don't 
 * see it ! Handles login error as well.
 */
@UrlBinding("/login.action")
public class LoginActionBean extends BaseActionBean {

	@DefaultHandler
	public Resolution displayForm() {
		if (isError()) {
			getContext().getValidationErrors().addGlobalError(
					new LocalizableError("woko.login.failed"));
		}
		return new ForwardResolution("/WEB-INF/woko/login.jsp");
	}
	
	public Resolution submitLogin() {
		return new ForwardResolution("/j_acegi_security_check");		
	}
	
	public String getUsernameStr() {
		if (isError())
			return (String)getRequest().getSession().getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY);
		else
			return null;
	}
	
	public boolean isError() {
		return getRequest().getParameter("login_error")!=null;
	}	
	
	public Resolution logout() {
		return new ForwardResolution("/j_acegi_logout");
	}
}
