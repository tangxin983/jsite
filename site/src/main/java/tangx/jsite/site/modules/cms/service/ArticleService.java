package tangx.jsite.site.modules.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.cms.dao.ArticleDao;
import tangx.jsite.site.modules.cms.entity.Article;

/**
 * 文章Service
 * 
 * @author tangx
 * @since 2014-09-22
 */
@Service
@Transactional
public class ArticleService extends BaseService<Article> {

	private ArticleDao articleDao;

	@Autowired
	public void setArticleDao(ArticleDao articleDao) {
		super.setDao(articleDao);
		this.articleDao = articleDao;
	}

	// ========== 以下为简单增删改示例。 修改以适应实际需求===========
	@Override
	public void insert(Article entity) {
		super.insert(entity);
	}

	@Override
	public void update(Article entity) {
		super.update(entity);
	}

	@Override
	public void delete(String id) {
		super.delete(id);
	}

	@Override
	public void delete(List<String> ids) {
		super.delete(ids);
	}

	@Override
	public Page<Article> selectByPage(Map<String, Object> paraMap,
			Map<String, String> orders, int pageNumber, int pageSize) {
		Page<Article> p = buildPage(pageNumber, pageSize);
		if(paraMap.get("categoryId") != null) {
			String categoryId = String.valueOf(paraMap.get("categoryId"));
			paraMap.put("categoryPid", "%," + categoryId + ",%");
		}
		p.setData(articleDao.findArticleByCategory(p, paraMap));
		return p;
	}

}
