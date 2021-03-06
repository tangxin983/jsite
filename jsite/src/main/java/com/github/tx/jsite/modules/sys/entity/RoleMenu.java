package com.github.tx.jsite.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Table(name = "sys_role_menu")
public class RoleMenu implements Serializable {

	@Column(name = "role_id")
	private String roleId;

	@Column(name = "menu_id")
	private String menuId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}