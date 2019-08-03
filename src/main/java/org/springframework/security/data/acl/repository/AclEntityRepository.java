package org.springframework.security.data.acl.repository;

import java.io.Serializable;

import org.springframework.security.data.acl.entity.AclEntity;

public interface AclEntityRepository extends AclJpaRepository<AclEntity, Serializable> {
}
