package org.springframework.security.data.acl.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.data.acl.entity.AclUser;

public interface AclUserRepository extends JpaRepository<AclUser, Serializable> {

	Optional<AclUser> findByNameAndPassword(String name, String password);

	Optional<AclUser> findByName(String name);
	
	Optional<AclUser> findByNameIgnoreCase(String name);
}
