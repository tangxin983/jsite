package com.github.tx.jsite.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Table(name = "sys_user_role")
public class UserRole implements Serializable {

	@Column(name = "role_id")
	private String roleId;

	@Column(name = "user_id")
	private String userId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}