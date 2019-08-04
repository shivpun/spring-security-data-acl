package org.springframework.security.data.acl.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.security.data.acl.entity.FilterRule;
import org.springframework.security.data.acl.spel.util.QueryBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class SimpleAclJpaSpecification implements AclJpaSpecification<Object> {

	private static final long serialVersionUID = -188391082457303930L;

	@Override
	public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		QueryBuilder qb = new QueryBuilder(root, cq, cb);
		return qb.predicate();
	}

	@Override
	public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb,
			List<FilterRule> filterRules) {
		if (CollectionUtils.isEmpty(filterRules)) {
			return null;
		}
		QueryBuilder qb = new QueryBuilder(root, cq, cb);
		for (FilterRule rule : filterRules) {
			qb.storeRHS();
			predicates(qb, rule);
			qb.storeLHS();
			qb.and();
		}
		return qb.predicate();
	}

	private void predicates(QueryBuilder qb, FilterRule rule) {
		List<Object> domains = rule.domain();
		Inorder in = new Inorder();
		resolve(domains, qb, in);
		qb.add(inOrderQuery(qb, in));
		in.reset();
	}

	private Predicate inOrderQuery(QueryBuilder qb, Inorder in) {
		if (in.inLHS == null && in.predicate != null) {
			return in.predicate;
		} else {
			return recursive(qb, in, in.inLHS);
		}
	}

	private Predicate recursive(QueryBuilder qb, Inorder rhs, Inorder lhs) {
		if (lhs != null && lhs.root != null) {
			Predicate lrhs = lhs.predicate == null ? lhs.lhsVrhs() : lhs.predicate;
			Predicate predicate = qb.andOR(lhs.root, rhs.predicate, lrhs);
			lhs.reset();
			lhs.predicate = predicate;
			return inOrderQuery(qb, lhs);
		}
		return rhs.predicate == null ? rhs.lhsVrhs() : rhs.predicate;
	}

	private void resolve(List domains, QueryBuilder qb, Inorder in) {
		Inorder inOrder = in;
		for (Object obj : domains) {
			if (obj instanceof List && ((List) obj).get(0) instanceof List) {
				resolve((List) obj, qb, inOrder);
				int size = domains.size();
				if (size > 0) {
					inOrder = inOrder.next();
				}
			} else {
				query(obj, qb, inOrder);
			}
		}
	}

	private void query(Object obj, QueryBuilder qb, Inorder in) {
		if (obj instanceof List) {
			List src = (List) obj;
			int size = src.size();
			if (size > 2) {
				in.add(qb.condition((String) src.get(0), (String) src.get(1), src.get(2)));
			} else {
				in.add(qb.condition((String) src.get(0), (String) src.get(1), null));
			}
		} else if (obj instanceof String) {
			in.root = (String) obj;
		}
		in.query(qb);
	}

	static class Inorder {
		private Predicate lhs;

		private Predicate rhs;

		private String root;

		private Predicate predicate;

		Inorder inLHS;

		public Inorder() {
		}

		void add(Predicate predicate) {
			if (this.root == null) {
				this.rhs = predicate;
			} else {
				this.lhs = predicate;
			}
		}

		Predicate query(QueryBuilder qb) {
			if (!StringUtils.isEmpty(this.root) && this.lhs != null && this.rhs != null) {
				this.predicate = qb.andOR(root, rhs, lhs);
				reset();
				return this.predicate;
			}
			return null;
		}

		Predicate predicate() {
			if (this.predicate != null) {
				return this.predicate;
			}
			return lhsVrhs();
		}

		Predicate lhsVrhs() {
			if (this.predicate == null) {
				boolean isLHS = this.lhs != null;
				boolean isRHS = this.rhs != null;
				if (isLHS) {
					return this.lhs;
				}
				return isRHS ? this.rhs : null;
			}
			return null;
		}

		void reset() {
			this.root = null;
			this.lhs = null;
			this.rhs = null;
		}

		boolean isLNREmpty() {
			return this.rhs == null && this.lhs == null && this.root == null;
		}

		Inorder next() {
			if (this.predicate != null && isLNREmpty()) {
				this.inLHS = new Inorder();
				return this.inLHS;
			}
			return this;
		}
	}
}
