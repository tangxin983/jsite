package tangx.jsite.site.modules.sys.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import tangx.jsite.site.core.persistence.entity.BaseEntity;

@SuppressWarnings("serial")
@Table(name = "sys_role")
public class Role extends BaseEntity {

	@Column(name = "office_id")
	private String officeId;

	@Column
	private String name;

	@Column(name = "en_name")
	private String enName;

	@Column(name = "data_scope")
	private String dataScope;

	@Transient
	private List<String> menuIds;

	public List<String> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
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

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

}