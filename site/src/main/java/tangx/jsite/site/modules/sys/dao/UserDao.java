package tangx.jsite.site.modules.sys.dao;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.modules.sys.entity.User;


@MyBatisDao
public interface UserDao extends BaseDao<User> {

}
