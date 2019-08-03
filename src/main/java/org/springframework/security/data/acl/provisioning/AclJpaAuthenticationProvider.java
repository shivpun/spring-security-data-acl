package org.springframework.security.data.acl.provisioning;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.data.acl.model.User;
import org.springframework.security.provisioning.UserDetailsManager;

public class AclJpaAuthenticationProvider extends DaoAuthenticationProvider {

	private UserDetailsManager userDetailsManager;

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	public AclJpaAuthenticationProvider(UserDetailsManager userDetailsManager) {
		this.userDetailsManager = userDetailsManager;
		super.setUserDetailsService(this.userDetailsManager);
	}

	protected void doAfterPropertiesSet() throws Exception {
		super.doAfterPropertiesSet();
	}

	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		// Ensure we return the original credentials the user supplied,
		// so subsequent attempts are successful even with encoded passwords.
		// Also ensure we return the original getDetails(), so that future
		// authentication events after cache expiry contain the details
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
				authentication.getCredentials(), authoritiesMapper.mapAuthorities(user.getAuthorities()));
		User users = (user instanceof User) ? (User) user : null;
		result.setDetails(users);

		return result;
	}

}
