package net.sourceforge.jfacets.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.jfacets.JFacets;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * WebFacets is an extension of the <code>JFacets</code> class that is 
 * includes specific features for the web (authentication etc.). 
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 * @see net.sourceforge.jfacets.web.WebFacetsFilter
 */
public class WebFacets extends JFacets {
	
	private static final JFacetsLogger logger = JFacetsLogger.getLogger(WebFacets.class);
		
	/**
	 * Return the WebFacets bean associated to passed 
	 * request (uses WebFacetsFilter).
	 * @return The WebFacets bean for passed request if found, null if the bean isn't bound to request (filter didn't work)
	 */
	public static WebFacets get(HttpServletRequest request) {
		return (WebFacets)request.getAttribute(WebFacetsFilterBean.KEY_REQ_WEBFACETS);
	}

	/**
	 * Return the profileId for passed request using the user's principal from the 
	 * request.
	 * @return the ID of the profile for the currently logged in user.
	 */
	public String getProfileId(HttpServletRequest request) {
		String profileId = null;
		Principal p = request.getUserPrincipal();
		if (p!=null) {
			if (logger.isDebugEnabled()) logger.debug("Principal found in request : " + p + ", using name as the profile ID");
			profileId = p.getName();
		}
		return profileId;
	}
	
	/**
	 * Retrieves a facet for current user (uses request.getUserPrincipal()).
	 */
	public Object getFacet(String facetName, Object targetObject, Class targetObjectClass, HttpServletRequest request) {
		return super.getFacet(facetName, getProfileId(request), targetObject, targetObjectClass);
	}

	/**
	 * Retrieves a facet for current user (uses request.getUserPrincipal()).
	 */
	public Object getFacet(String facetName, Object targetObject, HttpServletRequest request) {
		return super.getFacet(facetName, getProfileId(request), targetObject);
	}	
	
	/**
	 * Retrieves a facet for current user (uses request.getUserPrincipal()), using a 
	 * dummy Object as the target object (for use without target objects).
	 */
	public Object getFacet(String facetName, HttpServletRequest request) {
		return super.getFacet(facetName, getProfileId(request));
	}	

}
