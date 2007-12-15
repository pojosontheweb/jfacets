package net.sourceforge.jfacets.acegi;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

/**
 * An extension of the {@link IHierarchicalUserDetailsService} that allows 
 * full navigation in the users/roles graph.
 * 
 * @author Remi VANKEISBELCK - remi 'at' rvkb.com
 *
 */
public interface INavigableHierarchicalUserDetailsService extends
		IHierarchicalUserDetailsService {
	
	public GrantedAuthority[] getRootRoles();
	
	public GrantedAuthority[] getChildRoles(GrantedAuthority parentRole);
	
	public UserDetails[] getUsersInRole(GrantedAuthority role);

}
