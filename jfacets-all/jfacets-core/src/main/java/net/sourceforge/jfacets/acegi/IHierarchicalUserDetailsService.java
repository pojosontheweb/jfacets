package net.sourceforge.jfacets.acegi;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetailsService;

/**
 * An extension of Acegi's UserDetailsService for managing 
 * roles inheritance.
 */
public interface IHierarchicalUserDetailsService extends UserDetailsService {

	/**
	 * Return a GrantedAuthority for passed role name (null if not found)
	 */
	GrantedAuthority getGrantedAuthority(String roleName);

	/**
	 * Return an array of parent roles for passed G.A. (null or 0-length array if no parents)
	 */
	GrantedAuthority[] getParentRoles(GrantedAuthority grantedAuthority);

}
