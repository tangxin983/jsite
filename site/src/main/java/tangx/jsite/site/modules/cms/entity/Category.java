package tangx.jsite.site.modules.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import tangx.jsite.site.core.persistence.entity.BaseEntity;

/**
 * cms栏目实体
 * 
 * @author tangx
 * @since 2014-09-10
 */
@SuppressWarnings("serial")
@Table(name = "cms_category")
public class Category extends BaseEntity {

	@Column(name = "site_id")
	private String siteId;

	@Column(name = "office_id")
	private String officeId;

	@Column(name = "parent_id")
	private String parentId;

	@Column(name = "parent_ids")
	private String parentIds;

	@Column(name = "module")
	private String module;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String image;

	@Column(name = "href")
	private String href;

	@Column(name = "target")
	private String target;

	@Column(name = "description")
	private String description;

	@Column(name = "keywords")
	private String keywords;

	@Column(name = "sort")
	private Integer sort;

	@Column(name = "in_menu")
	private String inMenu;

	@Column(name = "in_list")
	private String inList;

	@Column(name = "show_modes")
	private String showModes;

	@Column(name = "allow_comment")
	private String allowComment;

	@Column(name = "is_audit")
	private String isAudit;

	@Column(name = "custom_list_view")
	private String customListView;

	@Column(name = "custom_content_view")
	private String customContentView;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "update_date")
	private Date updateDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "del_flag")
	private String delFlag;

	@Column(name = "view_config")
	private String viewConfig;

	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getInMenu() {
		return inMenu;
	}

	public void setInMenu(String inMenu) {
		this.inMenu = inMenu;
	}

	public String getInList() {
		return inList;
	}

	public void setInList(String inList) {
		this.inList = inList;
	}

	public String getShowModes() {
		return showModes;
	}

	public void setShowModes(String showModes) {
		this.showModes = showModes;
	}

	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getCustomListView() {
		return customListView;
	}

	public void setCustomListView(String customListView) {
		this.customListView = customListView;
	}

	public String getCustomContentView() {
		return customContentView;
	}

	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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

	public String getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

}
