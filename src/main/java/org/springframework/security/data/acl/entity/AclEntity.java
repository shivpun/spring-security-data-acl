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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ACL_ENTITY", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class AclEntity implements Serializable {

	private static final long serialVersionUID = 6515278728596426722L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_entity_id")
	@SequenceGenerator(name = "acl_entity_id", sequenceName = "acl_entity_id_seq")
	private Integer entityId;

	private String name;

	@ManyToOne
	@JoinColumn(name = "module")
	private Module module;

	public AclEntity() {
	}

	private AclEntity(String name, Module module) {
		this.name = name;
		this.module = module;
	}

	public static AclEntity from(String name, Module module) {
		return new AclEntity(name, module);
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
}
