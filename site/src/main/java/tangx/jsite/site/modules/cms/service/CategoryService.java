package tangx.jsite.site.modules.cms.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.cms.dao.CategoryDao;
import tangx.jsite.site.modules.cms.entity.Category;

import com.github.tx.common.util.CollectionUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

/**
 * cms栏目Service
 * 
 * @author tangx
 * @since 2014-09-10
 */
@Service
@Transactional
public class CategoryService extends BaseService<Category> {

	private CategoryDao categoryDao;

	@Autowired
	public void setCategoryDao(CategoryDao categoryDao) {
		super.setDao(categoryDao);
		this.categoryDao = categoryDao;
	}

	/**
	 * 获取栏目TreeTable列表
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Category> getCategoryForTreeTable(Map<String, Object> searchParams) {
		List<Category> sourcelist = findCategoryBySort(null);
		List<Category> list = Lists.newArrayList();
		sortList(list, sourcelist, "1");
		return list;
	}

	/**
	 * 对原始list进行整理以适合treetable要求
	 * 
	 * @param list
	 * @param sourcelist
	 * @param parentId
	 */
	private void sortList(List<Category> list, List<Category> sourcelist,
			String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Category e = sourcelist.get(i);
			if (e.getParentId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					Category child = sourcelist.get(j);
					if (child.getParentId().equals(e.getId())) {
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	/**
	 * 根据排序和条件参数获取栏目
	 * 
	 * @param searchParams
	 * @return
	 */
	@Cacheable(value = "categoryCache")
	public List<Category> findCategoryBySort(Map<String, Object> searchParams) {
		Map<String, String> orders = Maps.newHashMap();
		orders.put("sort", "asc");
		return select(searchParams, orders);
	}

	@Override
	@CacheEvict(value = "categoryCache", allEntries = true)
	public void insert(Category entity) {
		Category parent = selectById(entity.getParentId());// 获取父栏目
		entity.setParentIds(parent.getParentIds() + parent.getId() + ",");
		categoryDao.insert(entity);
	}

	@Override
	@CacheEvict(value = "categoryCache", allEntries = true)
	public void update(Category entity) {
		String oldParentIds = entity.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		// 更新parentIds
		Category parent = selectById(entity.getParentId());
		entity.setParentIds(parent.getParentIds() + parent.getId() + ",");
		categoryDao.update(entity);
		// 更新子节点的parentIds
		if (StringUtils.isNotBlank(oldParentIds)) {
			List<Category> childs = findChildsByPid(entity.getId());
			for (Category e : childs) {
				e.setParentIds(e.getParentIds().replace(oldParentIds,
						entity.getParentIds()));
				categoryDao.update(e);
			}
		}
	}

	@Override
	@CacheEvict(value = "categoryCache", allEntries = true)
	public void delete(String id) {
		// 查找子节点的id
		List<Category> childs = findChildsByPid(id);
		List<String> ids = CollectionUtils.extractToList(childs, "id", true);
		// 将自身id加入
		ids.add(id);
		super.delete(ids);
	}

	/**
	 * 根据父节点id查找所有子节点
	 * 
	 * @param id
	 * @return
	 */
	public List<Category> findChildsByPid(String id) {
		Table<String, String, Object> table = HashBasedTable.create();
		table.put("parentIds", "like", "," + id + ",");
		return select(table);
	}
}
