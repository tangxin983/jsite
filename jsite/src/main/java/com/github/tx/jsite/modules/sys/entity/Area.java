package com.github.tx.jsite.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tx.jsite.core.persistence.entity.BaseEntity;

/**
 * 字典实体
 * @author tangx
 * @since 2014-11-04
 */
@SuppressWarnings("serial")
@Table(name = "sys_area")
public class Area extends BaseEntity {

	@Column(name = "parent_id")
	@NotNull(message = "父级编号不能为空")
	private String parentId; //父级编号
	
	@Column(name = "parent_ids")
	@NotNull(message = "所有父级编号不能为空")
	private String parentIds; //所有父级编号
	
	@Column(name = "code")
	private String code; //区域编码
	
	@Column(name = "name")
	@NotNull(message = "区域名称不能为空")
	private String name; //区域名称
	
	@Column(name = "type")
	private String type; //区域类型
	
	@Column(name = "create_by")
	private String createBy; //创建者
	
	@Column(name = "create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")// 默认输出格式
	private Date createTime; //创建时间
	
	@Column(name = "update_by")
	private String updateBy; //更新者
	
	@Column(name = "update_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")// 默认输出格式
	private Date updateTime; //更新时间
	
	@Column(name = "remarks")
	private String remarks; //备注信息
	
	@Column(name = "del_flag")
	@NotNull(message = "删除标记不能为空")
	private String delFlag; //删除标记
	
	private String parentName;
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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


