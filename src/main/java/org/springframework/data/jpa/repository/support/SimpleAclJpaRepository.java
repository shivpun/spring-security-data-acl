package org.springframework.data.jpa.repository.support;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.QueryHints.NoHints;
import org.springframework.lang.Nullable;
import org.springframework.security.data.acl.entity.FilterRule;
import org.springframework.security.data.acl.repository.AclJpaRepository;
import org.springframework.security.data.acl.specification.AclJpaSpecification;
import org.springframework.util.Assert;

public class SimpleAclJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> implements AclJpaRepository<T, ID> {

	private AclJpaSpecification aclJpaSpecification;

	private final JpaEntityInformation<T, ?> entityInformation;

	private @Nullable CrudMethodMetadata metadata;

	private final EntityManager em;

	public SimpleAclJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.entityInformation = entityInformation;
		this.em = em;
	}

	public SimpleAclJpaRepository(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
	}

	public AclJpaSpecification<Object> getAclJpaSpecification() {
		return aclJpaSpecification;
	}

	public void setAclJpaSpecification(AclJpaSpecification<Object> aclJpaSpecification) {
		this.aclJpaSpecification = aclJpaSpecification;
	}

	/**
	 * Configures a custom {@link CrudMethodMetadata} to be used to detect
	 * {@link LockModeType}s and query hints to be applied to queries.
	 *
	 * @param crudMethodMetadata
	 */
	public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
		this.metadata = crudMethodMetadata;
	}

	@Nullable
	protected CrudMethodMetadata getRepositoryMethodMetadata() {
		return metadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
	public List<T> findAll() {
		AclJpaEntityFactory aclJpaEntityFactory = new AclJpaEntityFactory(em, metadata);
		List<FilterRule> filterRules = aclJpaEntityFactory.read(getDomainClass());
		return getQuery(aclJpaSpecification, Sort.unsorted(), filterRules).getResultList();
	}

	/**
	 * Deletes the given entities in a batch which means it will create a single
	 * {@link Query}. Assume that we will clear the
	 * {@link javax.persistence.EntityManager} after the call.
	 *
	 * @param entities
	 */
	public void deleteInBatch(Iterable<T> entities) {
		super.deleteInBatch(entities);
	}

	/**
	 * Deletes all entities in a batch call.
	 */
	public void deleteAllInBatch() {
		super.deleteAllInBatch();
	}

	/**
	 * Returns a reference to the entity with the given identifier. Depending on how
	 * the JPA persistence provider is implemented this is very likely to always
	 * return an instance and throw an
	 * {@link javax.persistence.EntityNotFoundException} on first access. Some of
	 * them will reject invalid identifiers immediately.
	 *
	 * @param id must not be {@literal null}.
	 * @return a reference to the entity with the given identifier.
	 * @see EntityManager#getReference(Class, Object) for details on when an
	 *      exception is thrown.
	 */
	public T getOne(ID id) {
		return super.getOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.
	 * springframework.data.domain.Example)
	 */
	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		return super.findAll(example);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.
	 * springframework.data.domain.Example, org.springframework.data.domain.Sort)
	 */
	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return super.findAll(example, sort);
	}

	protected <S extends T> TypedQuery<S> getQuery(@Nullable AclJpaSpecification<S> spec, Class<S> domainClass, List<FilterRule> filterRules, Sort sort) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<S> query = builder.createQuery(domainClass);
		Root<S> root = applySpecificationToCriteria(spec, domainClass, query, filterRules);
		query.select(root);
		if (sort.isSorted()) {
			query.orderBy(toOrders(sort, root, builder));
		}
		return applyRepositoryMethodMetadata(em.createQuery(query));
	}

	private <S, U extends T> Root<U> applySpecificationToCriteria(@Nullable AclJpaSpecification<U> spec, Class<U> domainClass,
			CriteriaQuery<S> query, List<FilterRule> filterRules) {
		Assert.notNull(domainClass, "Domain class must not be null!");
		Assert.notNull(query, "CriteriaQuery must not be null!");
		Root<U> root = query.from(domainClass);
		if (spec == null) {
			return root;
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate predicate = null;
		if (filterRules == null) {
			predicate = spec.toPredicate(root, query, builder);
		} else {
			predicate = spec.toPredicate(root, query, builder, filterRules);
		}
		if (predicate != null) {
			query.where(predicate);
		}
		return root;
	}

	private <S> TypedQuery<S> applyRepositoryMethodMetadata(TypedQuery<S> query) {
		if (metadata == null) {
			return query;
		}
		LockModeType type = metadata.getLockModeType();
		TypedQuery<S> toReturn = type == null ? query : query.setLockMode(type);
		applyQueryHints(toReturn);
		return toReturn;
	}

	private void applyQueryHints(Query query) {
		for (Entry<String, Object> hint : getQueryHints().withFetchGraphs(em)) {
			query.setHint(hint.getKey(), hint.getValue());
		}
	}

	/**
	 * Returns {@link QueryHints} with the query hints based on the current
	 * {@link CrudMethodMetadata} and potential {@link EntityGraph} information.
	 *
	 * @return
	 */
	protected QueryHints getQueryHints() {
		return metadata == null ? NoHints.INSTANCE : DefaultQueryHints.of(entityInformation, metadata);
	}

	/**
	 * Creates a {@link TypedQuery} for the given {@link Specification} and
	 * {@link Sort}.
	 *
	 * @param spec can be {@literal null}.
	 * @param sort must not be {@literal null}.
	 * @return
	 */
	protected TypedQuery<T> getQuery(@Nullable AclJpaSpecification<T> spec, Sort sort, List<FilterRule> filterRules) {
		return getQuery(spec, getDomainClass(), filterRules, sort);
	}

}
