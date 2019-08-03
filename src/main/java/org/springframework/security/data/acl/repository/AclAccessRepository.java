package org.springframework.security.data.acl.repository;

import java.io.Serializable;

import org.springframework.security.data.acl.entity.AclAccess;

public interface AclAccessRepository extends AclJpaRepository<AclAccess, Serializable> {
}
