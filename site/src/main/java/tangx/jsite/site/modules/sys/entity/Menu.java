package tangx.jsite.site.modules.sys.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import tangx.jsite.site.core.persistence.entity.BaseEntity;

@SuppressWarnings("serial")
@Table(name = "sys_menu")
public class Menu extends BaseEntity {

	@Column(name = "parent_id")
	private String parentId;

	@Column(name = "parent_ids")
	private String parentIds;

	@Column
	private String name;

	@Column
	private String permission;

	@Column
	private String href;

	@Column
	private String target;

	@Column
	private String icon;

	@Column
	private Integer sort;

	@Column(name = "is_show")
	private String isShow;

	private String parentName;

	private Menu parent;

	private List<Menu> children;// 子资源

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
 
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

}