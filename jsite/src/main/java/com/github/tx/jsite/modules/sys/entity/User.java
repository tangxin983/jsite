package com.github.tx.jsite.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tx.jsite.core.persistence.entity.BaseEntity;

/**
 * 用户实体
 * 
 * @author tangx
 * @since 2014-11-07
 */
@SuppressWarnings("serial")
@Table(name = "sys_user")
public class User extends BaseEntity {

	@Column(name = "company_id")
	private String companyId; // 归属公司

	@Column(name = "office_id")
	private String officeId; // 归属部门

	@Column(name = "login_name")
	@NotNull(message = "登录名不能为空")
	private String loginName; // 登录名

	@Column(name = "password")
	private String password; // 密码

	@Column(name = "salt")
	private String salt; //

	@Column(name = "no")
	private String no; // 工号

	@Column(name = "name")
	@NotNull(message = "姓名不能为空")
	private String name; // 姓名

	@Column(name = "email")
	private String email; // 邮箱

	@Column(name = "phone")
	private String phone; // 电话

	@Column(name = "mobile")
	private String mobile; // 手机

	@Column(name = "user_type")
	private String userType; // 用户类型

	@Column(name = "login_ip")
	private String loginIp; // 最后登陆IP

	@Column(name = "login_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	// 默认输出格式
	private Date loginTime; // 最后登陆时间

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

	@JsonIgnore
	private List<Role> roles;

	@NotNull(message = "密码不能为空")
	private String plainPassword;

	@JsonIgnore
	private List<String> roleIds;// 用于设置编辑页的id
	
	private List<String> roleNames;// 用于列表显示
	
	public List<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
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
	
	@Transient
	public boolean isAdmin() {
		return isAdmin(this.id);
	}

	@Transient
	public static boolean isAdmin(String id) {
		return id != null && id.equals("1");
	}

	@Transient
	public static boolean isAdmin(List<String> ids) {
		for (String id : ids) {
			if (isAdmin(id)) {
				return true;
			}
		}
		return false;
	}

}
