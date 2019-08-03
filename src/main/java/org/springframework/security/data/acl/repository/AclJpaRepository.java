package org.springframework.security.data.acl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AclJpaRepository<T, ID> extends JpaRepository<T, ID> {

}
