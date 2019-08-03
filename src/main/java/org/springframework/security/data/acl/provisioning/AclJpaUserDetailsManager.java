package org.springframework.security.data.acl.provisioning;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.data.acl.convert.AclUserToUser;
import org.springframework.security.data.acl.entity.AclUser;
import org.springframework.security.data.acl.repository.AclUserRepository;
import org.springframework.security.provisioning.UserDetailsManager;

public class AclJpaUserDetailsManager implements UserDetailsManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(AclJpaUserDetailsManager.class);

	private final AclUserRepository aclUserRepository;

	public AclJpaUserDetailsManager(AclUserRepository aclUserRepository) {
		this.aclUserRepository = aclUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info(String.format("Authenticate User [%s] from DB via loadUserByUsername of UserDetailsManager", username));
		Converter<AclUser, UserDetails> aclUser = new AclUserToUser();
		Optional<AclUser> aclUserOpt = aclUserRepository.findByName(username);
		return aclUserOpt.isPresent() ? aclUser.convert(aclUserOpt.get()) : null;
	}

	@Override
	public void createUser(UserDetails user) {
		LOGGER.info("AclJpaUserDetailsManager | createUser:" + user);
	}

	@Override
	public void updateUser(UserDetails user) {
		LOGGER.info("AclJpaUserDetailsManager | updateUser:" + user);
	}

	@Override
	public void deleteUser(String username) {
		LOGGER.info("AclJpaUserDetailsManager | deleteUser:" + username);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		LOGGER.info("AclJpaUserDetailsManager | changePassword:" + oldPassword + " | NEW:" + newPassword);
	}

	@Override
	public boolean userExists(String username) {
		LOGGER.info(String.format("Verify User [%s] exist in System or not:", username));
		return aclUserRepository.findByName(username).isPresent();
	}
}
