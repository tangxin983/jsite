package tangx.jsite.site.modules.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.modules.cms.entity.Link;

/**
 * 链接Dao
 * @author tangx
 * @since 2014-09-30
 */
@MyBatisDao
public interface LinkDao extends BaseDao<Link> {
	
	List<Link> findLinkByCategory(@Param(PAGE_KEY) Page<Link> page,
			@Param(PARA_KEY) Map<String, Object> paraMap);
}
