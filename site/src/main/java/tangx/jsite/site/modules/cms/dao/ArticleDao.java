package tangx.jsite.site.modules.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import tangx.jsite.site.core.persistence.annotation.MyBatisDao;
import tangx.jsite.site.core.persistence.dao.BaseDao;
import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.modules.cms.entity.Article;

/**
 * 文章Dao
 * 
 * @author tangx
 * @since 2014-09-22
 */
@MyBatisDao
public interface ArticleDao extends BaseDao<Article> {

	List<Article> findArticleByCategory(@Param(PAGE_KEY) Page<Article> page,
			@Param(PARA_KEY) Map<String, Object> paraMap);
}
