package net.sourceforge.jfacets.acegi;

import java.util.ArrayList;


import net.sourceforge.jfacets.log.JFacetsLogger;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

/**
 * Wrapper for UserDetails objects that allows to 
 * know about all GrantedAuthorities (transitively) 
 * when using roles inheritance.
 * <br/>
 * Such instances have to be created by 
 * <code>IHierarchicalUserDetailsService</code> objects, 
 * in the <code>loadUserByUsername</code> method.
 */
public class HierarchicalUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private static final JFacetsLogger logger = JFacetsLogger
			.getLogger(HierarchicalUserDetails.class);
	
	private UserDetails delegate;
	private IHierarchicalUserDetailsService huds;
	private ArrayList<GrantedAuthority> allAuthorities = new ArrayList<GrantedAuthority>();
	
	public HierarchicalUserDetails(IHierarchicalUserDetailsService huds, UserDetails delegate) {
		super();
		if (logger.isDebugEnabled()) logger.debug("Creating wrapper for UserDetails : " + delegate + " (username=" + delegate.getUsername() + ")");
		this.huds = huds;
		this.delegate = delegate;
		loadAllAuthorities(delegate.getAuthorities());
		StringBuffer sb = new StringBuffer();
		for (GrantedAuthority ga : allAuthorities) {
			sb.append(ga.getAuthority());
			sb.append(" ");
		}
		if (logger.isDebugEnabled()) logger.debug("Wrapper created, all authorities = " + sb.toString());
	}

	private void loadAllAuthorities(GrantedAuthority[] authorities) {
		for (int i = 0; i < authorities.length; i++) {
			if (!allAuthorities.contains(authorities[i])) {
				allAuthorities.add(authorities[i]);
				if (logger.isDebugEnabled()) logger.debug("Added GA " + authorities[i]);
			}
			GrantedAuthority[] parents = huds.getParentRoles(authorities[i]);
			if (parents!=null && parents.length>0)
				loadAllAuthorities(parents);
		}
	}
	
	public GrantedAuthority[] getAuthoritiesStrict() {
		return delegate.getAuthorities();
	}

	public GrantedAuthority[] getAuthorities() {
		GrantedAuthority[] res = new GrantedAuthority[allAuthorities.size()];
		res = allAuthorities.toArray(res);
		return res;
	}

	public String getPassword() {
		return delegate.getPassword();
	}

	public String getUsername() {
		return delegate.getUsername();
	}

	public boolean isAccountNonExpired() {
		return delegate.isAccountNonExpired();
	}

	public boolean isAccountNonLocked() {
		return delegate.isAccountNonLocked();
	}

	public boolean isCredentialsNonExpired() {
		return delegate.isCredentialsNonExpired();
	}

	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	public UserDetails getDelegate() {
		return delegate;
	}

	

}
