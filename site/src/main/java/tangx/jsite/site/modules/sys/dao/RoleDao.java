package tangx.jsite.site.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.modules.sys.entity.Role;

@MyBatisDao
public interface RoleDao extends BaseDao<Role> {

	@Select("select * from sys_role LEFT JOIN sys_user_role on sys_role.id = sys_user_role.role_id "
			+ "WHERE sys_user_role.user_id = #{userId}")
	List<Role> findRolesByUserId(String userId);
}
