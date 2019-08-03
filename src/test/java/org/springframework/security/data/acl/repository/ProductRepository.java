package org.springframework.security.data.acl.repository;

import java.io.Serializable;

import org.springframework.security.data.acl.entity.Product;

public interface ProductRepository extends AclJpaRepository<Product, Serializable> {
}
