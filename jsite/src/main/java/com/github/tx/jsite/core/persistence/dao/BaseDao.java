package com.github.tx.jsite.core.persistence.dao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;

import com.github.tx.jsite.core.persistence.entity.BaseEntity;
import com.github.tx.mybatis.mapper.CrudMapper;

/**
 * 提供基本增删改查方法的基础接口 
 * @author tangx
 *
 * @param <T>
 */
public interface BaseDao<T> extends CrudMapper<T> {

	@InsertProvider(type = CrudProvider.class, method = "insert")
	@SelectKey(statement = "select REPLACE(UUID(),'-','')", keyProperty = BaseEntity.ID_FIELD_NAME, before = true, resultType = String.class)
	int insertWithUuid(T t);

}
