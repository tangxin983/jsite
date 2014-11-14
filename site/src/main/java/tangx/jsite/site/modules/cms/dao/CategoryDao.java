package tangx.jsite.site.modules.cms.dao;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.modules.cms.entity.Category;

/**
 * cms栏目Dao
 * @author tangx
 * @since 2014-09-10
 */
@MyBatisDao
public interface CategoryDao extends BaseDao<Category> {
	
}
