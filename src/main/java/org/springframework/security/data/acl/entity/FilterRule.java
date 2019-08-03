package org.springframework.security.data.acl.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.data.acl.spel.util.ExpressionUtils;

@Entity
@Immutable
@Table(name = "FILTER_RULE")
public class FilterRule {
	
	@Id
	private String id;
	
	private String name;
	
	private String domain;
	
	private String groupBy;

	private String sortBy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	public List<Object> domain() {
		EvaluationContext context = ExpressionUtils.context();
		List<Object> expLst = ExpressionUtils.values(context, getDomain(), List.class);
		return expLst;
	}
}
