package org.springframework.security.data.acl.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.data.acl.entity.AclAccess;

public interface AclAccessRepository extends JpaRepository<AclAccess, Serializable> {
}
