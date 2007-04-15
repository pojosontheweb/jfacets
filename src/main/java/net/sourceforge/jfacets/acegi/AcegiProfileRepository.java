package net.sourceforge.jfacets.acegi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jfacets.INavigableProfileRepository;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.apache.log4j.Logger;

/**
 * The AcegiProfileRepository enable easy use of WebFacets with Acegi. 
 * It relies on injected <code>UserDetailsService</code> in order to 
 * retrieve users and related roles.
 * <br/>
 * <b>IMPORTANT</b> : Acegi does not support hierarchical roles and does 
 * not allow to get informations about a role. Thereby, in order to use 
 * these features, your UserDetailsService has to implement the 
 * <code>IHierarchicalUserDetailsService</code> interface. Otherwise, 
 * it'll work only for flat roles and you will only be able to use facets 
 * for user profiles (not roles). 
 * 
 * @author Remi VANKEISBELCK
 *
 */
public class AcegiProfileRepository implements INavigableProfileRepository {

	/** the injected UserDetailsService */
	private UserDetailsService userDetailsService;
	
	private static final Logger logger = Logger
			.getLogger(AcegiProfileRepository.class);
	
	private HashMap<String, UserDetails> cachedUserDetails 
		= new HashMap<String, UserDetails>();
	
	private boolean useCache = false;
		
	/**
	 * Return a profile by ID. 
	 * Returns either a <code>UserDetailsProfile</code> in case passed 
	 * profileId represents a role name, or a <code>GrantedAuthorityProfile</code> if 
	 * passed profileId identifies a valid user (uses <code>UserDetailsService</code>).
	 */
	public IProfile getProfileById(String profileId) {
		// try roles 
		if (logger.isDebugEnabled()) logger.debug("getProfileById() - getting profile for ID " + profileId);
		GrantedAuthority ga = getGrantedAuthority(profileId);
		if (ga!=null) {
			if (logger.isDebugEnabled()) logger.debug("getProfileById() - found GA : " + ga);
			return new GrantedAuthorityProfile(ga);
		}
		else {
			if (logger.isDebugEnabled()) logger.debug("useCache = " + useCache);
			if (useCache) {
				// use cache for users
				UserDetails ud = cachedUserDetails.get(profileId);
				if (ud==null) {
					ud = userDetailsService.loadUserByUsername(profileId);
					if (ud!=null)
						cachedUserDetails.put(profileId, ud);
				}
				if (ud==null) {
					logger.warn("Requested profile ID " + profileId + " does not identify any valid user or role !");
					return null;					
				} else {
					if (logger.isDebugEnabled()) logger.debug("getProfileById() - found user details : " + ud);
					return new UserDetailsProfile(ud);
				}
			} else {
				// don't use cache
				UserDetails ud = userDetailsService.loadUserByUsername(profileId);
				if (ud==null) {
					logger.warn("Requested profile ID " + profileId + " does not identify any valid user or role !");
					return null;
				} else {
					if (logger.isDebugEnabled()) logger.debug("getProfileById() - found user details : " + ud);
					return new UserDetailsProfile(ud);
				}
			}
		}
	}

	/**
	 * Return the super profiles for passed profile if found, 
	 * a 0-length array if not found.
	 */
	public IProfile[] getSuperProfiles(IProfile profile) {
		if (logger.isDebugEnabled()) logger.debug("getSuperProfiles() : getting super profiles for profile ID = " + profile.getId());
		// is it a role ?
		if (profile instanceof GrantedAuthorityProfile) {
			// role : return parents if any
			GrantedAuthority[] parentRoles = getParentRoles(
					((GrantedAuthorityProfile)profile).getGrantedAuthority() );
			if (parentRoles!=null && parentRoles.length>0) {
				ArrayList<IProfile> res = new ArrayList<IProfile>();
				for (int i = 0; i < parentRoles.length; i++)
					res.add(new GrantedAuthorityProfile(parentRoles[i]));
				IProfile[] superProfiles = new IProfile[res.size()];
				superProfiles = res.toArray(superProfiles);
				if (logger.isDebugEnabled()) logger.debug("getSuperProfiles() : returning super roles : " + superProfiles);
				return superProfiles;
			} else {
				if (logger.isDebugEnabled()) logger.debug("getSuperProfiles() : this role has no parents");
				return new IProfile[0];
			}
		} else if (profile instanceof UserDetailsProfile) {
			// user : return granted authorities
			UserDetails ud = ((UserDetailsProfile)profile).getUserDetails();
			GrantedAuthority[] roles;
			if (ud instanceof HierarchicalUserDetails)
				roles = ((HierarchicalUserDetails)ud).getAuthoritiesStrict();
			else
				roles = ud.getAuthorities();
			ArrayList<IProfile> res = new ArrayList<IProfile>();
			for (int i = 0; i < roles.length; i++)
				res.add(new GrantedAuthorityProfile(roles[i]));
			IProfile[] superProfiles = new IProfile[res.size()];
			superProfiles = res.toArray(superProfiles);
			if (logger.isDebugEnabled()) logger.debug("getSuperProfiles() : returning roles for user : " + superProfiles);
			return superProfiles;
		} else {
			logger.warn("getSuperProfiles() : unhandled profile " + profile);
			return new IProfile[0];
		}
	}
	
	/**
	 * Return the GrantedAuthority for passed role name. 
	 */
	public GrantedAuthority getGrantedAuthority(String roleName) {
		if (userDetailsService instanceof IHierarchicalUserDetailsService) {
			IHierarchicalUserDetailsService huds = 
				(IHierarchicalUserDetailsService)userDetailsService;
			return huds.getGrantedAuthority(roleName);
		} else 
			return null;
	}

	/**
	 * Return a list of parent roles for passed Granted Authority. 
	 */
	public GrantedAuthority[] getParentRoles(GrantedAuthority grantedAuthority) {
		if (logger.isDebugEnabled()) logger.debug("getParentRoles() : obtaining parent roles for GA " + grantedAuthority);
		if (userDetailsService instanceof IHierarchicalUserDetailsService) {
			if (logger.isDebugEnabled()) logger.debug("getParentRoles() : userDetailsService supports hierarchical roles, delegating");
			IHierarchicalUserDetailsService huds = 
				(IHierarchicalUserDetailsService)userDetailsService;
			return huds.getParentRoles(grantedAuthority);
		} else 
			return null;
	}

	/** get the UserDetailsService */
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	/** set the UserDetailsService */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public IProfile[] getRootProfiles() {
		if (userDetailsService instanceof INavigableHierarchicalUserDetailsService) {
			INavigableHierarchicalUserDetailsService nhuds = 
				(INavigableHierarchicalUserDetailsService)userDetailsService;
			GrantedAuthority[] rootRoles = nhuds.getRootRoles();
			IProfile[] res = new IProfile[rootRoles.length];
			for (int i = 0; i < rootRoles.length; i++) {
				res[i] = new GrantedAuthorityProfile(rootRoles[i]);
			}
			return res;
		} else {
			logger.warn("Unable to get root roles : injected UserDetailsService does NOT implement INavigableHierarchicalUserDetailsService !");
			return null;
		}
	}

	public IProfile[] getSubProfiles(IProfile parentProfile) {
		// users have no children...
		if (parentProfile instanceof UserDetailsProfile) {
			return new IProfile[0];
		} else {
			// it's a role, get sub roles and users in that role...
			if (userDetailsService  instanceof INavigableHierarchicalUserDetailsService) {
				GrantedAuthority ga = ((GrantedAuthorityProfile)parentProfile).getGrantedAuthority();
				INavigableHierarchicalUserDetailsService nhuds = 
					(INavigableHierarchicalUserDetailsService)userDetailsService;
				GrantedAuthority[] childRoles = nhuds.getChildRoles(ga);
				UserDetails[] usersInRole = nhuds.getUsersInRole(ga);
				IProfile[] res = new IProfile[childRoles.length + usersInRole.length];
				int indx = 0;
				for (int i = 0; i < childRoles.length; i++) {
					res[indx++] = new GrantedAuthorityProfile(childRoles[i]);
				}
				for (int i = 0; i < usersInRole.length; i++) {
					res[indx++] = new UserDetailsProfile(usersInRole[i]);
				}
				return res;
			} else {
				logger.warn("Unable to get sub profiles : injected UserDetailsService does NOT implement INavigableHierarchicalUserDetailsService !");
				return null;				
			}			
		}
	}

}
