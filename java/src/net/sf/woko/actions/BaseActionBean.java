package net.sf.woko.actions;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;

import net.sourceforge.jfacets.acegi.HierarchicalUserDetails;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;

/**
 * Base action bean class with utility methods.
 */
public class BaseActionBean implements ActionBean {
	
	private ActionBeanContext ctx;
	private UserDetails user;

	public ActionBeanContext getContext() {
		return ctx;
	}

	public void setContext(ActionBeanContext ctx) {
		this.ctx = ctx;
	}
	
	protected Resolution error(String msg, Object[] params) {
		getContext().getValidationErrors().addGlobalError(new SimpleError(msg, params));
		return getContext().getSourcePageResolution();		
	}
	
	protected HttpServletRequest getRequest() {
		return getContext().getRequest();
	}
	
	/**
	 * Return the current user's <code>UserDetails</code> object if 
	 * any (null if nobody's logged in). Unwraps the User Details object that is returned from the 
	 * configured Acegi UserDetailsService. 
	 * Unwraps the delegate in case you use <code>HierarchicalUserDetails</code>.
	 */
	public UserDetails getUser() {
		if (user==null) {
			SecurityContext sCtx = SecurityContextHolder.getContext();
			if (sCtx!=null) {
				Authentication auth = sCtx.getAuthentication();
				if (auth!=null) {
                    Object principal = auth.getPrincipal();
                    if (principal instanceof HierarchicalUserDetails) {
                        user = ((HierarchicalUserDetails)principal).getDelegate();
                    } else {
                        user = (UserDetails)principal;                        
					}
				}
			}
		}
		return user;
	}


}
