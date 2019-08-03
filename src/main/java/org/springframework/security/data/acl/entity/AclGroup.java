package org.springframework.security.data.acl.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "ACL_GROUP", uniqueConstraints = @UniqueConstraint(columnNames = { "groupId", "module" }))
public class AclGroup implements Serializable {

	private static final long serialVersionUID = -4961385018914814570L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_group_id")
	@SequenceGenerator(name = "acl_group_id", sequenceName = "acl_group_id_seq")
	private Integer groupId;

	@ManyToOne
	@JoinColumn(name = "module")
	private Module module;

	private String name;

	@Embedded
	private CurdAccess curdAccess;

	@ManyToMany
	@JoinTable(name = "acl_group_rule_rel", joinColumns = {
			@JoinColumn(name = "groupId", table = "acl_group") }, inverseJoinColumns = {
					@JoinColumn(name = "ruleId", table = "acl_rule") })
	private Set<AclRule> aclRule;

	@ManyToMany
	private Set<AclUser> aclUser;

	public AclGroup() {
	}

	private AclGroup(String name, Module module, CurdAccess curdAccess) {
		this.name = name;
		this.module = module;
		this.curdAccess = curdAccess;
	}

	public static AclGroup from(String name, Module module, CurdAccess curdAccess) {
		return new AclGroup(name, module, curdAccess);
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CurdAccess getCurdAccess() {
		return curdAccess;
	}

	public void setCurdAccess(CurdAccess curdAccess) {
		this.curdAccess = curdAccess;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Set<AclRule> getAclRule() {
		return aclRule;
	}

	public void setAclRule(Set<AclRule> aclRule) {
		this.aclRule = aclRule;
	}

	public void addAclRule(AclRule aclRule) {
		if (CollectionUtils.isEmpty(getAclRule())) {
			setAclRule(new HashSet<AclRule>());
		}
		getAclRule().add(aclRule);
	}

	public Set<AclUser> getAclUser() {
		return aclUser;
	}

	public void setAclUser(Set<AclUser> aclUser) {
		this.aclUser = aclUser;
	}

	public void addAclUser(AclUser aclUser) {
		if (CollectionUtils.isEmpty(getAclUser())) {
			setAclUser(new HashSet<AclUser>());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int moduleHashCode = ((module == null) ? 0 : module.hashCode());
		int nameHashcode = ((name == null) ? 0 : name.hashCode());
		int groupHashCode = moduleHashCode + nameHashcode;
		int groupdIdHashCode = ((groupId == null) ? groupHashCode : groupId.hashCode());
		result = prime * result + groupdIdHashCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AclGroup)) {
			return false;
		}
		AclGroup other = (AclGroup) obj;
		boolean isEqual = equalToGroupId(other) || equalToUnique(other);
		return isEqual || equalToName(other);
	}

	private boolean equalToGroupId(AclGroup other) {
		if (groupId == null || other.groupId == null) {
			return false;
		}
		return groupId.equals(other.groupId);
	}

	private boolean equalToModule(AclGroup other) {
		if (module == null || other.module == null) {
			return false;
		}
		return module.equals(other.module);
	}

	private boolean equalToName(AclGroup other) {
		if (name == null || other.name == null) {
			return false;
		}
		return name.equals(other.name);
	}

	private boolean equalToUnique(AclGroup other) {
		return equalToModule(other) && equalToName(other);
	}
}
