package org.springframework.security.data.acl.spel.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.CollectionUtils;

public class QueryBuilder {

	private CriteriaBuilder cb;

	private CriteriaQuery<?> cq;

	private Root<Object> root;

	private List<Predicate> predicates;

	public QueryBuilder(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		this.root = root;
		this.cq = cq;
		this.cb = cb;
		this.predicates = new ArrayList<Predicate>();
	}

	public Predicate condition(String field, String condition, Object value) {
		Predicate predicate = null;
		switch (condition) {
		case "IN":
			// add(root.get(field).in(value));
			predicate = root.get(field).in(value);
			break;
		case "=":
			// add(cb.equal(root.get(field), value));
			predicate = cb.equal(root.get(field), value);
			break;
		case "<>":
			// add(cb.notEqual(root.get(field), value));
			predicate = cb.notEqual(root.get(field), value);
			break;
		case "isTrue":
			// add(cb.isTrue(root.get(field)));
			predicate = cb.isTrue(root.get(field));
			break;
		case "isFalse":
			// add(cb.isFalse(root.get(field)));
			predicate = cb.isFalse(root.get(field));
			break;
		case "isNull":
			// add(cb.isNull(root.get(field)));
			predicate = cb.isNull(root.get(field));
			break;
		case "isNotNull":
			// add(cb.isNotNull(root.get(field)));
			predicate = cb.isNotNull(root.get(field));
			break;
		case ">":
			// add(cb.gt(root.get(field), value));
			break;
		}
		return predicate;
	}

	public Predicate andOR(String condition, Predicate... predicates) {
		Predicate predicate = null;
		switch (condition.toUpperCase()) {
		case "OR":
			predicate = cb.or(predicates);
			break;
		case "AND":
			predicate = cb.and(predicates);
			break;
		}
		return predicate;
	}

	public void add(Predicate predicate) {
		this.predicates.add(predicate);
	}

	public void reset() {
		this.predicates = new ArrayList<Predicate>();
	}

	public Predicate[] predicates() {
		if (CollectionUtils.isEmpty(this.predicates)) {
			return new Predicate[0];
		}
		return this.predicates.toArray(new Predicate[this.predicates.size()]);
	}
}
