package org.springframework.security.data.acl.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CurdAccess implements Serializable {

	private static final long serialVersionUID = 6612547912945301988L;

	private boolean create = true;

	private boolean update = true;

	private boolean read = true;

	private boolean delete = true;

	public CurdAccess() {
	}

	private CurdAccess(boolean create, boolean update, boolean read, boolean delete) {
		this();
		this.create = create;
		this.update = update;
		this.read = read;
		this.delete = delete;
	}

	public static CurdAccess from(boolean create, boolean update, boolean read, boolean delete) {
		return new CurdAccess(create, update, read, delete);
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
