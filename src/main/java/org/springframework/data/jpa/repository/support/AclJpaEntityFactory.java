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

	public AclJpaEntityFactory(EntityManager em, CrudMethodMetadata metadata) {
		this.em = em;
		this.metadata = metadata;
	}

	public List<FilterRule> fetchFilterRule(String domain, String operation) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getDetails();
		Long userId = user.getUserId();
		String groupQuery = String.format(QueryConstant.FETCH_USER_RULE, operation);
		String filterQuery = String.format(QueryConstant.FETCH_USER_FILTER, operation);
		Query q = this.em.createNativeQuery("( "+filterQuery +" ) UNION ("+groupQuery+" )", FilterRule.class);
		q.setParameter(1, domain);
		q.setParameter(2, userId);
		q.setParameter(3, domain);
		q.setParameter(4, userId);
		List<FilterRule> filterRules = q.getResultList();
		return filterRules;
	}

	public List<FilterRule> read(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		return fetchFilterRule(domain, QueryConstant.READ);
	}

	public List<FilterRule> create(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		return fetchFilterRule(domain, "create");
	}
	
	public List<FilterRule> update(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		return fetchFilterRule(domain, "update");
	}
	
	public List<FilterRule> delete(Class<?> domainClass) {
		if (domainClass == null) {
			return null;
		}
		String domain = domainClass.getName();
		return fetchFilterRule(domain, "delete");
	}
}
