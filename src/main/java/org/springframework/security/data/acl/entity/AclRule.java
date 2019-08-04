package org.springframework.security.data.acl.entity;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ACL_RULE")
public class AclRule implements Serializable {

	private static final long serialVersionUID = -8384003316448221719L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_rule_id")
	@SequenceGenerator(name = "acl_rule_id", sequenceName = "acl_rule_id_seq")
	private Integer ruleId;

	private String name;

	private String domain;

	@ManyToOne
	@JoinColumn(name = "entity_Id", nullable = false)
	private AclEntity aclEntity;
	
	private boolean gobal = true;
	
	/*
	 * CurdAccess applied during CURD operation like create, update, read, delete
	 * For Example:
	 * Case 1:
	 * Apply Rule to read i.e. to view only operations, than use create=0, update=0, read=1, delete=0
	 */
	@Embedded
	private CurdAccess curdAccess;

	public AclRule() {
	}

	private AclRule(String name, String domain, AclEntity aclEntity) {
		this();
		this.name = name;
		this.domain = domain;
		this.aclEntity = aclEntity;
	}

	public static AclRule from(String name, String domain, AclEntity aclEntity) {
		return new AclRule(name, domain, aclEntity);
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

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

	public AclEntity getAclEntity() {
		return aclEntity;
	}

	public void setAclEntity(AclEntity aclEntity) {
		this.aclEntity = aclEntity;
	}

	public CurdAccess getCurdAccess() {
		return curdAccess;
	}

	public void setCurdAccess(CurdAccess curdAccess) {
		this.curdAccess = curdAccess;
	}

	public boolean isGobal() {
		return gobal;
	}

	public void setGobal(boolean gobal) {
		this.gobal = gobal;
	}
}
