package net.sourceforge.jfacets.acegi;

import org.acegisecurity.GrantedAuthority;

import net.sourceforge.jfacets.IProfile;

/**
 * Profile class for Granted Authorities.
 */
class GrantedAuthorityProfile implements IProfile {

	private GrantedAuthority ga;

	/**
	 * Create the profile from passed ga
	 * @param ga
	 */
	public GrantedAuthorityProfile(GrantedAuthority ga) {
		this.ga = ga;
	}

	/**
	 * Return the granted authority.
	 */
	public GrantedAuthority getGrantedAuthority() {
		return ga;
	}

	/**
	 * Return the profile ID (GrantedAuthority.getAuthority)
	 */
	public String getId() {
		return ga.getAuthority();
	}
	
}
