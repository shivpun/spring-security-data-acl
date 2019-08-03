package org.springframework.security.data.acl.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "MODULE", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class Module implements Serializable {

	private static final long serialVersionUID = 6835610321767873331L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_module_id")
	@SequenceGenerator(name = "acl_module_id", sequenceName = "acl_module_id_seq")
	private Integer moduleId;

	private String name;

	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
	private Set<AclGroup> aclGroup;

	public Module() {
	}

	private Module(String name) {
		this();
		this.name = name;
	}

	public static Module from(String name) {
		return new Module(name);
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int nameHashCode = ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((moduleId == null) ? nameHashCode : moduleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Module)) {
			return false;
		}
		Module other = (Module) obj;
		return equalToModuleId(other) || equalToName(other);
	}

	private boolean equalToModuleId(Module other) {
		if (moduleId == null || other.moduleId == null) {
			return false;
		}
		return moduleId.equals(other.moduleId);
	}

	private boolean equalToName(Module other) {
		if (name == null || other.name == null) {
			return false;
		}
		return name.equals(other.name);
	}
}
