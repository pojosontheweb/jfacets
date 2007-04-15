package net.sourceforge.jfacets.acegi;

import org.acegisecurity.userdetails.UserDetails;

import net.sourceforge.jfacets.IProfile;

/**
 * Profile class for User Details
 */
public class UserDetailsProfile implements IProfile {

	private UserDetails ud;
	
	public UserDetailsProfile(UserDetails ud) {
		super();
		this.ud = ud;
	}

	/**
	 * Return the UserDetails
	 */
	public UserDetails getUserDetails()  {
		return ud;
	}

	/**
	 * Return the profile ID (username)
	 */
	public String getId() {
		return ud.getUsername();
	}
	
}
