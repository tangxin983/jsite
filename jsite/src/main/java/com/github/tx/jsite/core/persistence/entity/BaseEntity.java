package com.github.tx.jsite.core.persistence.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通用Mybatis实体，其他实体需继承此类
 * 
 * @author tangx
 * 
 */
public class BaseEntity implements Serializable {
	
	public static final String ID_FIELD_NAME = "id";

	private static final long serialVersionUID = 1L;

	@Id
	protected String id;// 如果子类没定义@Id字段，用这个作为默认主键
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("DT_RowId")// 用于设置dataTable TR的id
	public String getDT_RowId() {
		return id;
	}

	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
