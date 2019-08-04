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
@Table(name = "ACL_ACCESS")
public class AclAccess implements Serializable {

	private static final long serialVersionUID = -1836019851865268606L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_access_id")
	@SequenceGenerator(name = "acl_access_id", sequenceName = "acl_access_id_seq")
	private Integer accessId;

	@ManyToOne
	@JoinColumn(name = "entity_Id", nullable = false)
	private AclEntity aclEntity;

	@ManyToOne
	@JoinColumn(name = "group_Id", nullable = true)
	private AclGroup aclGroup;

	@Embedded
	private CurdAccess curdAccess;

	public AclAccess() {
	}

	private AclAccess(AclEntity aclEntity, AclGroup aclGroup, CurdAccess curdAccess) {
		this.aclEntity = aclEntity;
		this.aclGroup = aclGroup;
		this.curdAccess = curdAccess;
	}

	public static AclAccess from(AclEntity aclEntity, AclGroup aclGroup, CurdAccess curdAccess) {
		return new AclAccess(aclEntity, aclGroup, curdAccess);
	}

	public Integer getAccessId() {
		return accessId;
	}

	public void setAccessId(Integer accessId) {
		this.accessId = accessId;
	}

	public AclEntity getAclEntity() {
		return aclEntity;
	}

	public void setAclEntity(AclEntity aclEntity) {
		this.aclEntity = aclEntity;
	}

	public AclGroup getAclGroup() {
		return aclGroup;
	}

	public void setAclGroup(AclGroup aclGroup) {
		this.aclGroup = aclGroup;
	}

	public CurdAccess getCurdAccess() {
		return curdAccess;
	}

	public void setCurdAccess(CurdAccess curdAccess) {
		this.curdAccess = curdAccess;
	}
}
