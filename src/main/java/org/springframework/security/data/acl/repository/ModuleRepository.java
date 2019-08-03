package org.springframework.security.data.acl.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.security.data.acl.entity.Module;

public interface ModuleRepository extends AclJpaRepository<Module, Serializable> {
	
	Optional<Module> findByName(String name);
}
