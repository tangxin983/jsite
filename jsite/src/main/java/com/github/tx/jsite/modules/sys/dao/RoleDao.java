package com.github.tx.jsite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.modules.sys.entity.Role;

@MyBatisDao
public interface RoleDao extends BaseDao<Role> {

	@Select("select * from sys_role LEFT JOIN sys_user_role on sys_role.id = sys_user_role.role_id "
			+ "WHERE sys_user_role.user_id = #{userId}")
	List<Role> findRolesByUserId(String userId);
	
	Role findRoleByPrimaryKey(String id);
}
