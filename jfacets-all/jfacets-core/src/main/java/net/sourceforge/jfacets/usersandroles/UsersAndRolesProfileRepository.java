package net.sourceforge.jfacets.usersandroles;

import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;

/**
 * The Users/Roles Profile Repository. 
 * Provides a higher abstraction level compared to the <code>IProfileRepository</code> 
 * base interface, for managing Users and Roles.
 * <br/> 
 * Nothing has to be modified here in order to use this ProfileRepository. 
 * You just have to implement the <code>IUsersAndRolesManager</code> and inject 
 * your object via the Spring ApplicationContext.
 * 
 * @author VANKEISBELCK Remi - remi 'at' rvkb.com
 *
 */
public class UsersAndRolesProfileRepository implements IProfileRepository {
	
	private IUsersAndRolesManager usersAndRolesManager;
	
	public UsersAndRolesProfileRepository() { 		
	}
	
	public IProfile getProfileById(String profileId) {
		IProfile res = null;
		IUserProfile user = usersAndRolesManager.getUserProfile(profileId);
		if (user!=null) {
			res = user;
		} else {
			// try in roles...
			res = usersAndRolesManager.getRoleProfile(profileId);
		}
		return res;
	}		

	public IProfile[] getSuperProfiles(IProfile child) {
		if (child instanceof IRoleProfile) {
			IRoleProfile role = (IRoleProfile)child;
			return usersAndRolesManager.getParentRoles(role);
		} else {
			IUserProfile user = (IUserProfile)child;
			return usersAndRolesManager.getRolesForUser(user);
		}
	}

	public IUsersAndRolesManager getUsersAndRolesManager() {
		return usersAndRolesManager;
	}

	public void setUsersAndRolesManager(IUsersAndRolesManager usersAndRolesManager) {
		this.usersAndRolesManager = usersAndRolesManager;
	}

}
