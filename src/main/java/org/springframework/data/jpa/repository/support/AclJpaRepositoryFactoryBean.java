package org.springframework.data.jpa.repository.support;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.security.data.acl.specification.AclJpaSpecification;

public class AclJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends JpaRepositoryFactoryBean<T, S, ID> {

	private AclJpaSpecification<Object> aclJpaSpecification;

	private String application;

	public AclJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface, AclJpaSpecification<Object> aclJpaSpecification,	@Value(value = "${spring.application.name}") String application) {
		super(repositoryInterface);
		this.aclJpaSpecification = aclJpaSpecification;
		this.application = application;
	}

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		AclJpaRepositoryFactory jpaRepositoryFactory = new AclJpaRepositoryFactory(entityManager, aclJpaSpecification, application);
		return jpaRepositoryFactory;
	}
}
