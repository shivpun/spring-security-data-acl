package org.springframework.data.jpa.repository.support;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.data.acl.constant.QueryConstant;
import org.springframework.security.data.acl.entity.FilterRule;
import org.springframework.security.data.acl.model.User;

final class AclJpaEntityFactory {

	private final EntityManager em;

	private @Nullable CrudMethodMetadata metadata;

	private final String application;

	public AclJpaEntityFactory(EntityManager em, CrudMethodMetadata metadata, String application) {
		this.em = em;
		this.metadata = metadata;
		this.application = application;
	}

	public static AclJpaEntityFactory of(EntityManager em, CrudMethodMetadata metadata, String application) {
		return new AclJpaEntityFactory(em, metadata, application);
	}

	public List<FilterRule> fetchFilterRule(String domain, String operation) {
		String groupQuery = String.format(QueryConstant.FETCH_USER_RULE, operation);
		String filterQuery = String.format(QueryConstant.FETCH_USER_FILTER, operation);
		Query q = this.em.createNativeQuery("( " + filterQuery + " ) UNION (" + groupQuery + " )", FilterRule.class);
		Long userId = userId();
		q.setParameter(1, domain);
		q.setParameter(2, userId);
		q.setParameter(3, domain);
		q.setParameter(4, userId);
		List<FilterRule> filterRules = q.getResultList();
		return filterRules;
	}

	public boolean isGroupAccess(String domain, String operation) {
		String query = String.format(QueryConstant.ACL_ACCESS_RULE, operation);
		Query q = this.em.createNativeQuery(query);
		q.setParameter(1, userId());
		q.setParameter(2, domain);
		q.setParameter(3, application);
		Object count = q.getSingleResult();
		return (int) count > 0;
	}

	public boolean isGenericAccess(String domain, String operation) {
		String query = String.format(QueryConstant.ACL_ACCESS_GENERIC, operation);
		Query q = this.em.createNativeQuery(query);
		q.setParameter(1, domain);
		int count = (Integer) q.getSingleResult();
		return count > 0;
	}

	public boolean isAclEntityAccessExist(String domain, String operation) {
		boolean groupAccess = isGroupAccess(domain, operation);
		if (!groupAccess) {
			return isGenericAccess(domain, operation);
		}
		return groupAccess;
	}

	public void checkAclEntityAccess(String domain, String operation) {
		boolean aclAccess = isAclEntityAccessExist(domain, operation);
		if (!aclAccess) {
			throw new IllegalArgumentException(QueryConstant.ACL_ACCESS_ERROR_MSG.get(operation));
		}
	}

	public List<FilterRule> read(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		checkAclEntityAccess(domain, QueryConstant.READ);
		return fetchFilterRule(domain, QueryConstant.READ);
	}

	public List<FilterRule> create(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		checkAclEntityAccess(domain, QueryConstant.CREATE);
		return fetchFilterRule(domain, QueryConstant.CREATE);
	}

	public List<FilterRule> update(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		checkAclEntityAccess(domain, QueryConstant.UPDATE);
		return fetchFilterRule(domain, QueryConstant.UPDATE);
	}

	public List<FilterRule> delete(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		checkAclEntityAccess(domain, QueryConstant.DELETE);
		return fetchFilterRule(domain, QueryConstant.DELETE);
	}

	public Long userId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getDetails();
		Long userId = user.getUserId();
		return userId;
	}
}
