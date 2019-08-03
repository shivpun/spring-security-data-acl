package org.springframework.security.data.acl.model;

/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * Models core Users information retrieved by a {@link UsersDetailsService}.
 * <p>
 * Developers may use this class directly, subclass it, or write their own
 * {@link UsersDetails} implementation from scratch.
 * <p>
 * {@code equals} and {@code hashcode} implementations are based on the
 * {@code Usersname} property only, as the intention is that lookups of the same
 * Users principal object (in a Users registry, for example) will match where
 * the objects represent the same Users, not just when all the properties
 * (authorities, password for example) are the same.
 * <p>
 * Note that this implementation is not immutable. It implements the
 * {@code CredentialsContainer} interface, in order to allow the password to be
 * erased after authentication. This may cause side-effects if you are storing
 * instances in-memory and reusing them. If so, make sure you return a copy from
 * your {@code UsersDetailsService} each time it is invoked.
 *
 * @author Ben Alex
 * @author Luke Taylor
 */
public class User implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private static final Log logger = LogFactory.getLog(User.class);

	// ~ Instance fields
	// ================================================================================================
	private final Long userId;
	private String password;
	private final String username;
	private final Set<GrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * Calls the more complex constructor with all boolean arguments set to
	 * {@code true}.
	 */
	public User(Long userId, String usersname, String password, Collection<? extends GrantedAuthority> authorities) {
		this(userId, usersname, password, true, true, true, true, authorities);
	}

	/**
	 * Construct the <code>Users</code> with the details required by
	 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
	 *
	 * @param Usersname             the Usersname presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param password              the password that should be presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param enabled               set to <code>true</code> if the Users is enabled
	 * @param accountNonExpired     set to <code>true</code> if the account has not
	 *                              expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have
	 *                              not expired
	 * @param accountNonLocked      set to <code>true</code> if the account is not
	 *                              locked
	 * @param authorities           the authorities that should be granted to the
	 *                              caller if they presented the correct Usersname
	 *                              and password and the Users is enabled. Not null.
	 *
	 * @throws IllegalArgumentException if a <code>null</code> value was passed
	 *                                  either as a parameter or as an element in
	 *                                  the <code>GrantedAuthority</code> collection
	 */
	public User(Long userId, String usersname, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {

		if (((usersname == null) || "".equals(usersname)) || (password == null)) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}
		this.userId = userId;
		this.username = usersname;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void eraseCredentials() {
		password = null;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UsersDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}

	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			// Neither should ever be null as each entry is checked before adding it to
			// the set.
			// If the authority is null, it is a custom authority and should precede
			// others.
			if (g2.getAuthority() == null) {
				return -1;
			}

			if (g1.getAuthority() == null) {
				return 1;
			}

			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}

	/**
	 * Returns {@code true} if the supplied object is a {@code Users} instance with
	 * the same {@code Usersname} value.
	 * <p>
	 * In other words, the objects are equal if they have the same Usersname,
	 * representing the same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof User) {
			return username.equals(((User) rhs).username);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code Usersname}.
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("Usersname: ").append(this.username).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

		if (!authorities.isEmpty()) {
			sb.append("Granted Authorities: ");

			boolean first = true;
			for (GrantedAuthority auth : authorities) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append(auth);
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}

	/**
	 * Creates a UsersBuilder with a specified Users name
	 *
	 * @param Usersname the Usersname to use
	 * @return the UsersBuilder
	 */
	public static UsersBuilder withUsersname(String Usersname) {
		return builder().username(Usersname);
	}

	/**
	 * Creates a UsersBuilder
	 *
	 * @return the UsersBuilder
	 */
	public static UsersBuilder builder() {
		return new UsersBuilder();
	}

	/**
	 * <p>
	 * <b>WARNING:</b> This method is considered unsafe for production and is only
	 * intended for sample applications.
	 * </p>
	 * <p>
	 * Creates a Users and automatically encodes the provided password using
	 * {@code PasswordEncoderFactories.createDelegatingPasswordEncoder()}. For
	 * example:
	 * </p>
	 *
	 * <pre>
	 * <code>
	 * UsersDetails Users = Users.withDefaultPasswordEncoder()
	 *     .Usersname("Users")
	 *     .password("password")
	 *     .roles("Users")
	 *     .build();
	 * // outputs {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
	 * System.out.println(Users.getPassword());
	 * </code>
	 * </pre>
	 *
	 * This is not safe for production (it is intended for getting started
	 * experience) because the password "password" is compiled into the source code
	 * and then is included in memory at the time of creation. This means there are
	 * still ways to recover the plain text password making it unsafe. It does
	 * provide a slight improvement to using plain text passwords since the
	 * UsersDetails password is securely hashed. This means if the UsersDetails
	 * password is accidentally exposed, the password is securely stored.
	 *
	 * In a production setting, it is recommended to hash the password ahead of
	 * time. For example:
	 *
	 * <pre>
	 * <code>
	 * PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	 * // outputs {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
	 * // remember the password that is printed out and use in the next step
	 * System.out.println(encoder.encode("password"));
	 * </code>
	 * </pre>
	 *
	 * <pre>
	 * <code>
	 * UsersDetails Users = Users.withUsersname("Users")
	 *     .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
	 *     .roles("Users")
	 *     .build();
	 * </code>
	 * </pre>
	 *
	 * @return a UsersBuilder that automatically encodes the password with the
	 *         default PasswordEncoder
	 * @deprecated Using this method is not considered safe for production, but is
	 *             acceptable for demos and getting started. For production
	 *             purposes, ensure the password is encoded externally. See the
	 *             method Javadoc for additional details. There are no plans to
	 *             remove this support. It is deprecated to indicate that this is
	 *             considered insecure for production purposes.
	 */
	@Deprecated
	public static UsersBuilder withDefaultPasswordEncoder() {
		logger.warn(
				"Users.withDefaultPasswordEncoder() is considered unsafe for production and is only intended for sample applications.");
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return builder().passwordEncoder(encoder::encode);
	}

	public static UsersBuilder withUsersDetails(UserDetails UsersDetails) {
		return withUsersname(UsersDetails.getUsername()).password(UsersDetails.getPassword())
				.accountExpired(!UsersDetails.isAccountNonExpired()).accountLocked(!UsersDetails.isAccountNonLocked())
				.authorities(UsersDetails.getAuthorities()).credentialsExpired(!UsersDetails.isCredentialsNonExpired())
				.disabled(!UsersDetails.isEnabled());
	}

	/**
	 * Builds the Users to be added. At minimum the Usersname, password, and
	 * authorities should provided. The remaining attributes have reasonable
	 * defaults.
	 */
	public static class UsersBuilder {
		private Long userId;
		private String username;
		private String password;
		private List<GrantedAuthority> authorities;
		private boolean accountExpired;
		private boolean accountLocked;
		private boolean credentialsExpired;
		private boolean disabled;
		private Function<String, String> passwordEncoder = password -> password;
		private User user;

		/**
		 * Creates a new instance
		 */
		private UsersBuilder() {
		}

		/**
		 * Populates the userId. This attribute is required.
		 *
		 * @param Usersname the userId. Cannot be null.
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder userId(Long userId) {
			Assert.notNull(userId, "userId cannot be null");
			this.userId = userId;
			return this;
		}

		/**
		 * Populates the Usersname. This attribute is required.
		 *
		 * @param Usersname the Usersname. Cannot be null.
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder username(String username) {
			Assert.notNull(username, "Usersname cannot be null");
			this.username = username;
			return this;
		}

		/**
		 * Populates the password. This attribute is required.
		 *
		 * @param password the password. Cannot be null.
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder password(String password) {
			Assert.notNull(password, "password cannot be null");
			this.password = password;
			return this;
		}

		/**
		 * Encodes the current password (if non-null) and any future passwords supplied
		 * to {@link #password(String)}.
		 *
		 * @param encoder the encoder to use
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder passwordEncoder(Function<String, String> encoder) {
			Assert.notNull(encoder, "encoder cannot be null");
			this.passwordEncoder = encoder;
			return this;
		}

		/**
		 * Populates the roles. This method is a shortcut for calling
		 * {@link #authorities(String...)}, but automatically prefixes each entry with
		 * "ROLE_". This means the following:
		 *
		 * <code>
		 *     builder.roles("Users","ADMIN");
		 * </code>
		 *
		 * is equivalent to
		 *
		 * <code>
		 *     builder.authorities("ROLE_Users","ROLE_ADMIN");
		 * </code>
		 *
		 * <p>
		 * This attribute is required, but can also be populated with
		 * {@link #authorities(String...)}.
		 * </p>
		 *
		 * @param roles the roles for this Users (i.e. Users, ADMIN, etc). Cannot be
		 *              null, contain null values or start with "ROLE_"
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder roles(String... roles) {
			List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
			for (String role : roles) {
				Assert.isTrue(!role.startsWith("ROLE_"),
						() -> role + " cannot start with ROLE_ (it is automatically added)");
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return authorities(authorities);
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this Users. Cannot be null, or contain
		 *                    null values
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 * @see #roles(String...)
		 */
		public UsersBuilder authorities(GrantedAuthority... authorities) {
			return authorities(Arrays.asList(authorities));
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this Users. Cannot be null, or contain
		 *                    null values
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 * @see #roles(String...)
		 */
		public UsersBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
			this.authorities = new ArrayList<>(authorities);
			return this;
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this Users (i.e. ROLE_Users,
		 *                    ROLE_ADMIN, etc). Cannot be null, or contain null values
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 * @see #roles(String...)
		 */
		public UsersBuilder authorities(String... authorities) {
			return authorities(AuthorityUtils.createAuthorityList(authorities));
		}

		/**
		 * Defines if the account is expired or not. Default is false.
		 *
		 * @param accountExpired true if the account is expired, false otherwise
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder accountExpired(boolean accountExpired) {
			this.accountExpired = accountExpired;
			return this;
		}

		/**
		 * Defines if the account is locked or not. Default is false.
		 *
		 * @param accountLocked true if the account is locked, false otherwise
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder accountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
			return this;
		}

		/**
		 * Defines if the credentials are expired or not. Default is false.
		 *
		 * @param credentialsExpired true if the credentials are expired, false
		 *                           otherwise
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder credentialsExpired(boolean credentialsExpired) {
			this.credentialsExpired = credentialsExpired;
			return this;
		}

		/**
		 * Defines if the account is disabled or not. Default is false.
		 *
		 * @param disabled true if the account is disabled, false otherwise
		 * @return the {@link UsersBuilder} for method chaining (i.e. to populate
		 *         additional attributes for this Users)
		 */
		public UsersBuilder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		public User users() {
			return this.user;
		}

		public UserDetails build() {
			String encodedPassword = this.passwordEncoder.apply(password);
			this.user = new User(userId, username, encodedPassword, !disabled, !accountExpired, !credentialsExpired,
					!accountLocked, authorities);
			return user;
		}
	}
}
