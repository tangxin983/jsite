package tangx.jsite.site.modules.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.cms.dao.LinkDao;
import tangx.jsite.site.modules.cms.entity.Link;

/**
 * 链接Service
 * @author tangx
 * @since 2014-09-30
 */
@Service
@Transactional
public class LinkService extends BaseService<Link> {

	private LinkDao linkDao;

	@Autowired
	public void setLinkDao(LinkDao linkDao) {
		super.setDao(linkDao);
		this.linkDao = linkDao;
	}
	
	// ========== 以下为简单增删改示例。 修改以适应实际需求===========
	@Override
	public void insert(Link entity) {
		super.insert(entity);
	}
	
	@Override
	public void update(Link entity) {
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
	public Page<Link> selectByPage(Map<String, Object> paraMap,
			Map<String, String> orders, int pageNumber, int pageSize) {
		Page<Link> p = buildPage(pageNumber, pageSize);
		if(paraMap.get("categoryId") != null) {
			String categoryId = String.valueOf(paraMap.get("categoryId"));
			paraMap.put("categoryPid", "%," + categoryId + ",%");
		}
		p.setData(linkDao.findLinkByCategory(p, paraMap));
		return p;
	}
}
