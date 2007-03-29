package net.sf.woko.actions.devel;

import net.sf.woko.actions.BaseActionBean;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.controller.LifecycleStage;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletContext;

/**
 * Allows to turn debug on/off
 * @author vankeisb
 *
 */
@UrlBinding("/tools/devel/debug.action")
public class DebugActionBean extends BaseActionBean {

	public static final String DEBUG_INIT_PARAM = "WOKO_DEBUG";
    private static final String KEY_APP_DEBUGGED_PROFILES = "wokoDebuggedProfiles";

    private Boolean debug;

    @Validate(required = true)
    private String userName;

    @Before(LifecycleStage.BindingAndValidation)
    public void preBind() {
        // init fields
        debug = (getContext().getServletContext().getInitParameter(DEBUG_INIT_PARAM)!=null);
        List<String> debuggedProfiles = (List<String>)getContext().getServletContext().getAttribute(KEY_APP_DEBUGGED_PROFILES);
        if (debuggedProfiles==null) {
            debuggedProfiles = new ArrayList<String>();
            getContext().getServletContext().setAttribute(KEY_APP_DEBUGGED_PROFILES, debuggedProfiles);
        }
    }

    @DontValidate
    @DefaultHandler
	public Resolution display() {
        if (!debug) {
            getContext().getMessages().add(new SimpleMessage("Debug is disabled ! Check your web.xml settings"));
        }
		return new ForwardResolution("/WEB-INF/woko/debug-mode.jsp");
	}

	public Boolean getDebug() {
		return debug;
	}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getDebuggedUsers() {
        return getDebuggedUsers(getContext().getServletContext());
    }

    public static List<String> getDebuggedUsers(ServletContext ctx) {
        return (List<String>)ctx.getAttribute(KEY_APP_DEBUGGED_PROFILES);
    }

    public Resolution addUserForDebug() {
        List<String> currentlyDebugged = getDebuggedUsers();
        if (currentlyDebugged.contains(userName)) {
            getContext().getValidationErrors().add("userName", new SimpleError("The specified user is already being debugged !"));
        } else {
            currentlyDebugged.add(userName);
            getContext().getMessages().add(new SimpleMessage("User <i>" + userName + "</i> added to debug"));
        }
        return new RedirectResolution(getClass());
    }

    public Resolution removeUserForDebug() {
        List<String> currentlyDebugged = getDebuggedUsers();
        if (currentlyDebugged.contains(userName)) {
            currentlyDebugged.remove(userName);
            getContext().getMessages().add(new SimpleMessage("User <i>" + userName + "</i> removed from debug"));
        }
        return new RedirectResolution(getClass());
    }

	
}
