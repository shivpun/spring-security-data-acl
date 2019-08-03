package org.springframework.security.data.acl.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.data.acl.entity.FilterRule;

public interface AclJpaSpecification<T> extends Specification<T> {
	
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb, List<FilterRule> filterRules);
	
}
