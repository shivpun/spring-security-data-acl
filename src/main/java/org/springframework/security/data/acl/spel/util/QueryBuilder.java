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

	private List<Predicate> rhs;

	private List<Predicate> lhs;

	public QueryBuilder(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		this.root = root;
		this.cq = cq;
		this.cb = cb;
		this.predicates = new ArrayList<Predicate>();
		this.rhs = new ArrayList<Predicate>();
		this.lhs = new ArrayList<Predicate>();
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
			System.out.println("TEST!!:"+value.getClass());
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
			//predicate = cb.greaterThan(root.get(field).as(Long.class), value);
			break;
		}
		return predicate;
	}

	public Predicate andOR(String condition, Predicate... predicates) {
		Predicate predicate = null;
		switch (condition) {
		case "AND":
			predicate = cb.and(predicates);
			break;
		case "OR":
			predicate = cb.or(predicates);
			break;
		}
		return predicate;
	}

	public Predicate andOR(String condition, Predicate rhs, Predicate lhs) {
		Predicate predicate = null;
		switch (condition) {
		case "AND":
			predicate = cb.and(rhs, lhs);
			break;
		case "OR":
			predicate = cb.or(rhs, lhs);
			break;
		}
		return predicate;
	}

	public void and() {
		if (!CollectionUtils.isEmpty(this.rhs) && !CollectionUtils.isEmpty(this.lhs)) {
			Predicate predicate = cb.and(this.rhs.get(0), this.lhs.get(0));
			reset();
			add(predicate);
		}
	}

	public void add(Predicate predicate) {
		this.predicates.add(predicate);
	}

	public void reset() {
		this.predicates = new ArrayList<Predicate>();
	}

	public void storeRHS() {
		if (!CollectionUtils.isEmpty(this.predicates)) {
			this.rhs = this.predicates;
			reset();
		}
	}

	public void storeLHS() {
		if (!CollectionUtils.isEmpty(this.predicates)) {
			this.lhs = this.predicates;
		}
	}

	public Predicate[] predicates() {
		if (CollectionUtils.isEmpty(this.predicates)) {
			return new Predicate[0];
		}
		return this.predicates.toArray(new Predicate[this.predicates.size()]);
	}

	public Predicate predicate() {
		if (CollectionUtils.isEmpty(this.predicates)) {
			return null;
		}
		return predicates()[0];
	}
}
