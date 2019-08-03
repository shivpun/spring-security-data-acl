package org.springframework.security.data.acl.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.data.acl.entity.AclRule;

public interface AclRuleRepository extends JpaRepository<AclRule, Serializable> {

	Optional<AclRule> findByName(String name);
}
