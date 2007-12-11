package net.sourceforge.jfacets.acegi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.InMemoryDaoImpl;
import org.acegisecurity.userdetails.memory.UserMap;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * Example of hierarchical roles using property files. 
 * Extends Acegi's InMemoryDaoImpl and implements the 
 * IHierarchicalUserDetailsService interface so that 
 * composite roles can be handled by WebFacets.
 * <br/>
 * Also, it makes use of HierarchicalUserDetails objects (see 
 * method loadUserByUsername) so that ACLs also work with 
 * hierarchical roles.
 */
public class InMemoryHierarchicalUDS extends InMemoryDaoImpl implements
		INavigableHierarchicalUserDetailsService {

	private static final Logger logger = Logger
			.getLogger(InMemoryHierarchicalUDS.class);

	private Properties roleProperties;
	private boolean wrapUserDetails = true;

	private HashMap<String, GrantedAuthority> roles = new HashMap<String, GrantedAuthority>();

	private HashMap<GrantedAuthority, ArrayList<GrantedAuthority>> rolesAndParents = new HashMap<GrantedAuthority, ArrayList<GrantedAuthority>>();

	private HashMap<String, UserDetails> userNamesAndDetails = new HashMap<String, UserDetails>();
	
	private int loadedRolesCount = 0;

	public void setRoleProperties(Properties roleProperties) {
		this.roleProperties = roleProperties;
	}
	
	/**
	 * Overriden in order to store all users internally (in order  
	 * to implement getUsersInRole()).
	 */
	@Override
	public void setUserProperties(Properties userProperties) {
		super.setUserProperties(userProperties);
		for (Iterator iter = userProperties.keySet().iterator(); iter.hasNext();) {
			String userName = (String)iter.next();
			userNamesAndDetails.put(userName, null);
		}
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.roleProperties,
				"A list of roles and their hierarchical structure must be set");
		super.afterPropertiesSet();
		loadRoles();
		loadUsers();
	}

	private void loadRoles() {
		if (logger.isInfoEnabled()) logger.info("Loading roles...");
		for (Iterator iter = roleProperties.keySet().iterator(); iter.hasNext();) {
			StringBuffer sb = new StringBuffer();
			String roleName = (String) iter.next();
			if (roleName!=null && !roleName.equals("")) {
				sb.append(" - ");
				sb.append(roleName);
				sb.append(" : parents=");
				GrantedAuthority ga = getOrCreate(roleName);
				String rolesStr = roleProperties.getProperty(roleName);
				String[] tokens = rolesStr.split(",");
				for (int i = 0; i < tokens.length; i++) {
					String parentName = tokens[i].trim();
					if (parentName!=null && !parentName.equals("")) {
						GrantedAuthority parent = getOrCreate(parentName);
						addToParents(ga, parent);
						sb.append(parentName);
						if (i < tokens.length - 1)
							sb.append(", ");
					}
				}
				if (logger.isInfoEnabled()) logger.info(sb.toString());
			}
		}
		if (logger.isInfoEnabled()) logger.info("OK, " + loadedRolesCount + " roles loaded");
	}
	
	private void loadUsers() {
		if (logger.isInfoEnabled()) logger.info("Loading users...");
		UserMap userMap = getUserMap();
		List<String> userNames = new ArrayList<String>(userNamesAndDetails.keySet());
		for(String userName : userNames) {
			UserDetails ud = userMap.getUser(userName);
			userNamesAndDetails.put(userName, ud);
		}
	}

	private GrantedAuthority getOrCreate(String name) {
		GrantedAuthority res = roles.get(name);
		if (res == null) {
			res = new GrantedAuthorityImpl(name);
			roles.put(name, res);
			loadedRolesCount++;
			if (logger.isDebugEnabled()) logger.debug("getOrCreate() : Role : " + name + " created (ga.toString()=" + res + ")");
		}		
		return res;
	}

	private void addToParents(GrantedAuthority child, GrantedAuthority parent) {
		ArrayList<GrantedAuthority> parents = rolesAndParents.get(child);
		if (parents == null) {
			parents = new ArrayList<GrantedAuthority>();
			rolesAndParents.put(child, parents);
		}
		if (!parents.contains(parent))
			parents.add(parent);
	}

	// -------------------------
	// Hierarchical UDS contract
	// -------------------------

	/**
	 * From IHierarchicalUserDetailsService. 
	 * Return a G.A. for passed role name.
	 */
	public GrantedAuthority getGrantedAuthority(String roleName) {
		return roles.get(roleName);
	}

	/**
	 * From IHierarchicalUserDetailsService. 
	 * Return an array of parent G.A.s for 
	 * passed G.A. (reflects roles inheritance). 
	 */
	public GrantedAuthority[] getParentRoles(GrantedAuthority grantedAuthority) {
		ArrayList<GrantedAuthority> res = rolesAndParents.get(grantedAuthority);
		if (res == null) {
			if (logger.isDebugEnabled()) logger.debug("getParentRoles() - Role " + grantedAuthority + " has no parents");
			return null;
		}
		else {
			GrantedAuthority[] gas = new GrantedAuthority[res.size()];
			gas = res.toArray(gas);
			if (logger.isDebugEnabled()) logger.debug("getParentRoles() - returning parent roles " + gas + " for " + grantedAuthority);
			return gas;
		}
	}

	// -----------------------------------------------
	// Enhanced UserDetails objects with all GAs in it
	// -----------------------------------------------

	/**
	 * Overriden method that wraps UserDetails into 
	 * HierarchicalUserDetails, so that ACLs also 
	 * benefit of roles inheritance.
	 * @see net.sourceforge.jfacets.acegi.HierarchicalUserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		UserDetails ud = super.loadUserByUsername(username);
		if (ud == null) {
			logger.warn("loadUserByUsername : use not found for name " + username + " returning null");
			return null;
		}
		else {
			if (wrapUserDetails) {
				HierarchicalUserDetails hud = new HierarchicalUserDetails(this, ud);
				if (logger.isDebugEnabled()) logger.debug("loadUserByUsername : Wrapping user '" + username + "' into HierarchicalUserDetails : " + hud);
				return hud;
			} else {				
				if (logger.isDebugEnabled()) logger.debug("loadUserByUsername : Returning user '" + username + "' into UserDetails : " + ud);
				return ud;
			}
		}
	}

	public boolean isWrapUserDetails() {
		return wrapUserDetails;
	}

	public void setWrapUserDetails(boolean wrapUserDetails) {
		this.wrapUserDetails = wrapUserDetails;
	}

	// -----------------------------------------------
	// Navigable User Details Service stuff
	// -----------------------------------------------
	
	public GrantedAuthority[] getRootRoles() {
		ArrayList<GrantedAuthority> res = new ArrayList<GrantedAuthority>();
		for (GrantedAuthority ga : roles.values()) {
			List<GrantedAuthority> parents = rolesAndParents.get(ga);
			if (parents==null || parents.size()==0) {
				res.add(ga);
			}
		}
		GrantedAuthority[] rootRoles = new GrantedAuthority[res.size()];
		rootRoles = res.toArray(rootRoles);
		return rootRoles;
	}

	public GrantedAuthority[] getChildRoles(GrantedAuthority parentRole) {
		ArrayList<GrantedAuthority> res = new ArrayList<GrantedAuthority>();
		// iterate on roles and find the ones which have passed role 
		// as a parent...
		for(GrantedAuthority ga : roles.values()) {
			List<GrantedAuthority> parents = rolesAndParents.get(ga);
			if (parents!=null && parents.contains(parentRole)) {
				res.add(ga);
			}
		}
		GrantedAuthority[] childRoles = new GrantedAuthority[res.size()];
		childRoles = res.toArray(childRoles);
		return childRoles;
	}

	public UserDetails[] getUsersInRole(GrantedAuthority role) {
		// iterate on users and try to find the ones that are childs 
		// of passed role.
		List<UserDetails> res = new ArrayList<UserDetails>();
		for(UserDetails ud : userNamesAndDetails.values()) {
			GrantedAuthority[] userRoles = ud.getAuthorities();
			for(int i=0 ; i<userRoles.length ; i++) {
				if (userRoles[i].equals(role)) {
					res.add(ud);
					break;
				}
			}
		}
		UserDetails[] uds = new UserDetails[res.size()];
		uds = res.toArray(uds);
		return uds;
	}

}
