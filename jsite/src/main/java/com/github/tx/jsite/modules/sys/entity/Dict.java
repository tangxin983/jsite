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
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

	@Column(name = "label")
	@NotNull(message = "字典名称不能为空")
	private String label; //字典名称
	
	@Column(name = "value")
	@NotNull(message = "字典值不能为空")
	private String value; //字典值
	
	@Column(name = "type")
	@NotNull(message = "字典类型不能为空")
	private String type; //字典类型
	
	@Column(name = "description")
	@NotNull(message = "描述不能为空")
	private String description; //描述
	
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


