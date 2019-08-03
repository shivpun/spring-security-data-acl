package org.springframework.security.data.acl.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.data.acl.entity.AclEntity;

public interface AclEntityRepository extends JpaRepository<AclEntity, Serializable> {
}
