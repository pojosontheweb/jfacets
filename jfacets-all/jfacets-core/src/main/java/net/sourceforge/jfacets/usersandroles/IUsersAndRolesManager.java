package net.sourceforge.jfacets.usersandroles;

/**
 * Users/Roles manager interface. <br/>
 * Developers using the Users/Roles profile repo should implement this interface 
 * and inject their object into the UsersAndRolesProfileRepository.
 * 
 * @author VANKEISBELCK Remi
 *
 */
public interface IUsersAndRolesManager {

	/**
	 * Return a User profile by ID (should be the user's login name) if found, 
	 * null if not found.
	 */
	public IUserProfile getUserProfile(String profileId);

	/**
	 * Return a Role profile by ID (should be the role name) if found, 
	 * null if not found.
	 */
	public IRoleProfile getRoleProfile(String profileId);
		
	/**
	 * Return an array of Role profiles with all parent Roles of the passed 
	 * Role profile (return a 0-length array if passed role has no parent roles).
	 */
	public IRoleProfile[] getParentRoles(IRoleProfile role);

	/**
	 * Return an array of Role profiles with all Roles for 
	 * passed User profile (return a 0-length array if 
	 * passed user has no roles).
	 */
	public IRoleProfile[] getRolesForUser(IUserProfile user);
	
}
