package com.github.tx.jsite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.modules.sys.entity.User;
import com.github.tx.mybatis.criteria.QueryCondition;

@MyBatisDao
public interface UserDao extends BaseDao<User> {

	User findUserByPrimaryKey(String id);

	User findUserByLoginName(@Param("condition") QueryCondition condition);

	List<User> findUserList(@Param("condition") QueryCondition condition);
}
