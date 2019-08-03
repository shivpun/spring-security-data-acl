package org.springframework.data.jpa.repository.support;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.security.data.acl.repository.AclJpaRepository;
import org.springframework.security.data.acl.specification.AclJpaSpecification;
import org.springframework.security.data.acl.spel.util.ReflectionUtils;

public class AclJpaRepositoryFactory extends JpaRepositoryFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(AclJpaRepositoryFactory.class);

	private AclJpaSpecification<Object> aclJpaSpecification;

	public AclJpaRepositoryFactory(EntityManager entityManager, AclJpaSpecification<Object> specification) {
		super(entityManager);
		this.aclJpaSpecification = specification;
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		if (isAcl(metadata)) {
			return SimpleAclJpaRepository.class;
		}
		return super.getRepositoryBaseClass(metadata);
	}

	@Override
	protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
			EntityManager entityManager) {
		JpaRepositoryImplementation<?, ?> repository = super.getTargetRepository(information, entityManager);
		if (isAcl(information)) {
			LOGGER.info(String.format("AclJpaRepository has been found in [%s]", information.getRepositoryInterface()));
			((SimpleAclJpaRepository<?, ?>) repository).setAclJpaSpecification(aclJpaSpecification);
		}
		return repository;

	}

	public boolean isAcl(RepositoryMetadata metadata) {
		if (metadata != null && metadata.getRepositoryInterface() != null) {
			Class<?> clazz = metadata.getRepositoryInterface();
			return ReflectionUtils.declareInterface(clazz, AclJpaRepository.class);
		}
		return false;
	}
}
