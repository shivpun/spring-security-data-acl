package org.springframework.security.data.acl.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.security.data.acl.entity.AclRule;

public interface AclRuleRepository extends AclJpaRepository<AclRule, Serializable> {

	Optional<AclRule> findByName(String name);
}
