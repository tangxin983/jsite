package com.github.tx.jsite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.modules.sys.entity.Menu;

@MyBatisDao
public interface MenuDao extends BaseDao<Menu> {

	@Select("select distinct sys_menu.* from sys_user "
			+ "left join sys_user_role on sys_user.id = sys_user_role.user_id "
			+ "left join sys_role on sys_user_role.role_id = sys_role.id "
			+ "left join sys_role_menu on sys_role.id = sys_role_menu.role_id "
			+ "left join sys_menu on sys_role_menu.menu_id = sys_menu.id "
			+ "where sys_user.id = #{userId} order by sys_menu.sort")
	List<Menu> findByUserId(String userId);
}
