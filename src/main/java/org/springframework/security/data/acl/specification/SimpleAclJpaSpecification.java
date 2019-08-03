package org.springframework.security.data.acl.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.data.acl.entity.FilterRule;
import org.springframework.security.data.acl.spel.util.ExpressionUtils;
import org.springframework.security.data.acl.spel.util.QueryBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class SimpleAclJpaSpecification implements AclJpaSpecification<Object> {

	private static final long serialVersionUID = -188391082457303930L;

	// String query = "{{'name','=', 'Mango'}}";
	String query = "{{'active','isTrue'}}";
	// String query = "{{'type','IN', {'IN'}}}";
	// String query = "{{'type', 'IN', {'OUT'}}, {'active', 'isTrue'}, 'AND'}";
	// String query = "{{{'type', 'IN', {'OUT'}}, {'active', 'isTrue'},
	// 'OR'},{{'type', 'IN', {'OUT'}}, {'active', 'isTrue'}, 'AND'}}";

	@Override
	public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		System.out.println("SimpleAclJpaSpecification | toPredicate");
		QueryBuilder qb = new QueryBuilder(root, cq, cb);
		return criteriaBuilder(qb, query);
	}

	private Predicate criteriaBuilder(QueryBuilder qb, String expression) {
		if (expression != null && !StringUtils.isEmpty((StringUtils.trimAllWhitespace(expression)))) {
			EvaluationContext context = ExpressionUtils.context();
			List expLst = ExpressionUtils.values(context, expression, List.class);
			int count = 0;
			for (Object obj : expLst) {
				if ((obj instanceof List)) {
					List src = (List) obj;
					if (src.size() > 2) {
						qb.add(qb.condition((String) src.get(0), (String) src.get(1), src.get(2)));
					} else {
						qb.add(qb.condition((String) src.get(0), (String) src.get(1), null));
					}
				} else if (obj instanceof String) {
					Predicate predicate = qb.andOR((String) obj, qb.predicates());
					qb.reset();
					qb.add(predicate);
				}
			}
			return qb.predicates()[0];
		}
		return null;
	}

	@Override
	public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> cq, CriteriaBuilder cb,
			List<FilterRule> filterRules) {
		if (CollectionUtils.isEmpty(filterRules)) {
			return null;
		}
		QueryBuilder qb = new QueryBuilder(root, cq, cb);
		for (FilterRule rule : filterRules) {
			predicates(qb, rule);
		}
		return qb.predicates()[0];
	}

	private void predicates(QueryBuilder qb, FilterRule rule) {
		List<Object> domains = rule.domain();
		for (Object obj : domains) {
			if (obj instanceof List) {
				List src = (List) obj;
				if (src.size() > 2) {
					qb.add(qb.condition((String) src.get(0), (String) src.get(1), src.get(2)));
				} else {
					qb.add(qb.condition((String) src.get(0), (String) src.get(1), null));
				}
			} else if (obj instanceof String) {
				Predicate predicate = qb.andOR((String) obj, qb.predicates());
				qb.reset();
				qb.add(predicate);
			}
		}
	}
}
