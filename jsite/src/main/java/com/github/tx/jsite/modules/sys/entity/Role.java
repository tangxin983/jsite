package com.github.tx.jsite.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tx.jsite.core.persistence.entity.BaseEntity;

/**
 * 评论实体
 * 
 * @author tangx
 * @since 2014-11-06
 */
@SuppressWarnings("serial")
@Table(name = "sys_role")
public class Role extends BaseEntity {

	@Column(name = "office_id")
	private String officeId; // 归属机构

	@Column(name = "name")
	@NotNull(message = "角色名称不能为空")
	private String name; // 角色名称

	@Column(name = "en_name")
	@NotNull(message = "角色英文名(即act_id_group的id_)不能为空")
	private String enName; // 角色英文名(即act_id_group的id_)

	@Column(name = "data_scope")
	private String dataScope; // 数据范围

	@Column(name = "create_by")
	private String createBy; // 创建者

	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	// 默认输出格式
	private Date createTime; // 创建时间

	@Column(name = "update_by")
	private String updateBy; // 更新者

	@Column(name = "update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	// 默认输出格式
	private Date updateTime; // 更新时间

	@Column(name = "remarks")
	private String remarks; // 备注信息

	@Column(name = "del_flag")
	private String delFlag; // 删除标记

	@Transient
	private List<Menu> menus;// 用于修改时展现菜单信息

	@Transient
	private List<String> menuIds;// 用于提交时修改role_menu表信息

	public List<String> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
