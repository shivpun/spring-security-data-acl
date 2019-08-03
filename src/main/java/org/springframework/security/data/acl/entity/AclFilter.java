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
@Table(name = "ACL_FILTER")
public class AclFilter implements Serializable {

	private static final long serialVersionUID = 1232910129662798719L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_filter_id")
	@SequenceGenerator(name = "acl_filter_id", sequenceName = "acl_filter_id_seq")
	private Integer filterId;

	private String name;

	private String domain;
	
	private String groupBy;

	private String sortBy;
	
	@ManyToOne
	@JoinColumn(name = "entity_Id", nullable = false)
	private AclEntity aclEntity;
	
	public Integer getFilterId() {
		return filterId;
	}

	public void setFilterId(Integer filterId) {
		this.filterId = filterId;
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

	public AclEntity getAclEntity() {
		return aclEntity;
	}

	public void setAclEntity(AclEntity aclEntity) {
		this.aclEntity = aclEntity;
	}
}
