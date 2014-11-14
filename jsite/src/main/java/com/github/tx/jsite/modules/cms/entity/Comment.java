package com.github.tx.jsite.modules.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.tx.jsite.core.persistence.entity.BaseEntity;

/**
 * 评论实体
 * @author tangx
 * @since 2014-11-06
 */
@SuppressWarnings("serial")
@Table(name = "cms_comment")
public class Comment extends BaseEntity {

	@Column(name = "category_id")
	@NotNull(message = "栏目编号不能为空")
	private String categoryId; //栏目编号
	
	@Column(name = "content_id")
	@NotNull(message = "栏目内容的编号不能为空")
	private String contentId; //栏目内容的编号
	
	@Column(name = "title")
	private String title; //栏目内容的标题
	
	@Column(name = "content")
	private String content; //评论内容
	
	@Column(name = "name")
	private String name; //评论姓名
	
	@Column(name = "ip")
	private String ip; //评论IP
	
	@Column(name = "create_date")
	@NotNull(message = "评论时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")// 默认输出格式
	private Date createDate; //评论时间
	
	@Column(name = "audit_user_id")
	private String auditUserId; //审核人
	
	@Column(name = "audit_date")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")// 表单提交时默认日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")// 默认输出格式
	private Date auditDate; //审核时间
	
	@Column(name = "del_flag")
	@NotNull(message = "删除标记不能为空")
	private String delFlag; //删除标记
	
	@Column(name = "salary")
	@NotNull(message = "不能为空")
	private Double salary; //
	
	@Column(name = "age")
	@NotNull(message = "不能为空")
	private Integer age; //
	
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}
	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
}


