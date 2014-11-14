package tangx.jsite.site.modules.sys.dao;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.modules.sys.entity.Org;

/**
 * 机构Dao
 * @author tangx
 * @since 2014-05-15
 */
@MyBatisDao
public interface OrgDao extends BaseDao<Org> {
	
}
