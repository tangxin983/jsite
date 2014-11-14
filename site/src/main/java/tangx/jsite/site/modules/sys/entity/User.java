package tangx.jsite.site.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import tangx.jsite.site.core.persistence.entity.BaseEntity;

@SuppressWarnings("serial")
@Table(name = "sys_user")
public class User extends BaseEntity {

	@Column(name = "company_id")
	private String companyId;

	@Column(name = "office_id")
	private String officeId;

	@Column(name = "login_name")
	private String loginName;

	@Column
	private String password;

	@Column
	private String salt;

	@Column
	private String no;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String phone;

	@Column
	private String mobile;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "login_ip")
	private String loginIp;

	@Column(name = "login_time")
	private Date loginTime;

	private List<Role> roles;

	private String plainPassword;

	private List<String> roleIds;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
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