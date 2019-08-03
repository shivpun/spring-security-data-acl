package org.springframework.security.data.acl.repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.data.acl.entity.AclGroup;

public interface AclGroupRepository extends JpaRepository<AclGroup, Serializable> {

	Set<AclGroup> findAllByName(String name);

	Optional<AclGroup> findByNameAndModule(String name, String module);
}
