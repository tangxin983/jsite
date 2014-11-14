package com.github.tx.jsite.core.persistence.dao;

import org.apache.ibatis.jdbc.SQL;

import com.github.tx.mybatis.util.ReflectUtil;

/**
 * 增删改查模板
 * 
 * @author tangx
 * 
 */
public class CrudProvider<T> {

	public String insert(final Object t) {
		final Class<?> clazz = t.getClass();
		return new SQL() {
			{
				INSERT_INTO(ReflectUtil.getTableName(clazz));
				VALUES(ReflectUtil.getIdColumnName(clazz) + ","
						+ ReflectUtil.insertColumnNameList(t), "#{"
						+ ReflectUtil.getIdFieldName(clazz) + "},"
						+ ReflectUtil.insertFieldNameList(t));
			}
		}.toString();
	}
}
