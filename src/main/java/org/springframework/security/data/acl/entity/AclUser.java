package org.springframework.security.data.acl.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "ACL_USER")
public class AclUser implements Serializable {

	private static final long serialVersionUID = -8012580278870772464L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_user_id")
	@SequenceGenerator(name = "acl_user_id", sequenceName = "acl_user_id_seq")
	private Integer userId;

	@Column(name = "username")
	private String name;

	@Column(name = "password")
	private String password;

	@ManyToMany
	@JoinTable(name = "acl_user_group_rel", joinColumns = {
			@JoinColumn(name = "userId", table = "acl_user") }, inverseJoinColumns = {
					@JoinColumn(name = "groupId", table = "acl_group") })
	private Set<AclGroup> aclGroup;

	@ManyToMany
	@JoinTable(name = "acl_user_filter_rel", joinColumns = {
			@JoinColumn(name = "userId", table = "acl_user") }, inverseJoinColumns = {
					@JoinColumn(name = "filterId", table = "acl_filter") })
	private Set<AclFilter> aclFilter;

	public AclUser() {
	}

	private AclUser(String name, String password) {
		this();
		this.name = name;
		this.password = password;
	}

	public static AclUser from(String name, String password) {
		return new AclUser(name, password);
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<AclGroup> getAclGroup() {
		return aclGroup;
	}

	public void setAclGroup(Set<AclGroup> aclGroup) {
		this.aclGroup = aclGroup;
	}

	public void addAclGroup(AclGroup aclGroup) {
		if (CollectionUtils.isEmpty(getAclGroup())) {
			setAclGroup(new HashSet<AclGroup>());
		}
		getAclGroup().add(aclGroup);
	}

	public Set<AclFilter> getAclFilter() {
		return aclFilter;
	}

	public void setAclFilter(Set<AclFilter> aclFilter) {
		this.aclFilter = aclFilter;
	}

	public void addAclFilter(AclFilter aclFilter) {
		if (CollectionUtils.isEmpty(getAclFilter())) {
			setAclFilter(new HashSet<AclFilter>());
		}
		getAclFilter().add(aclFilter);
	}

	public <T> T convert(Converter<AclUser, T> converter) {
		return converter.convert(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int nameHashCode = ((name == null) ? 0 : name.hashCode());
		int passwordHashCode = ((password == null) ? 0 : password.hashCode());
		int uniqueHashCode = nameHashCode + passwordHashCode;
		int userIdHashCode = ((userId == null) ? uniqueHashCode : userId.hashCode());
		result = prime * result + userIdHashCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AclUser)) {
			return false;
		}
		AclUser other = (AclUser) obj;
		return equalToUserId(other) || equalToUnique(other) || equalToName(other);
	}

	private boolean equalToUserId(AclUser other) {
		if (userId == null || other.userId == null) {
			return false;
		}
		return userId.equals(other.userId);
	}

	private boolean equalToName(AclUser other) {
		if (name == null || other.name == null) {
			return false;
		}
		return name.equals(other.name);
	}

	private boolean equalToPassword(AclUser other) {
		if (password == null || other.password == null) {
			return false;
		}
		return password.equals(other.password);
	}

	private boolean equalToUnique(AclUser other) {
		return equalToName(other) && equalToPassword(other);
	}
}
