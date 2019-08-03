package org.springframework.security.data.acl.entity;

import java.io.Serializable;

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
	@JoinColumn(name = "entityId", nullable = false)
	private AclEntity aclEntity;

	public AclRule() {
	}

	private AclRule(String name, String domain, AclEntity aclEntity) {
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
}
