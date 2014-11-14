package tangx.jsite.site.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import tangx.jsite.site.core.persistence.entity.BaseEntity;

/**
 * 字典实体
 * @author tangx
 * @since 2014-06-25
 */
@SuppressWarnings("serial")
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

	@Column(name = "label")
	private String label;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "create_by")
	private String createBy;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "update_by")
	private String updateBy;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "del_flag")
	private String delFlag;
	
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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


