package com.github.tx.jsite.modules.sys.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.sys.dao.AreaDao;
import com.github.tx.jsite.modules.sys.entity.Area;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.google.common.collect.Lists;

/**
 * 区域Service
 * @author tangx
 * @since 2014-05-12
 */
@Service
@Transactional
public class AreaService extends BaseService<Area> {

	private AreaDao areaDao;

	@Autowired
	public void setAreaDao(AreaDao areaDao) {
		super.setDao(areaDao);
		this.areaDao = areaDao;
	}
	
	/**
	 * 获取区域树形列表
	 * @return
	 */
	public List<Area> getTreeList() {
		QueryCondition condition = new QueryCondition();
		condition.asc("code");
		List<Area> sourcelist = areaDao.selectByCondition(condition);
		List<Area> list = Lists.newArrayList();
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
	private void sortList(List<Area> list, List<Area> sourcelist,
			String parentId) {
		for (Area e : sourcelist) {
			if (e.getParentId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (Area child : sourcelist) {
					if (child.getParentId().equals(e.getId())) {
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 根据名称获取区域列表
	 * 
	 * @param name
	 * @return
	 */
	public List<Area> findAreaByName(String name) {
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().eq("name", name));
		return areaDao.selectByCondition(condition);
	}
	 
	/**
	 * 创建区域
	 */
	@Override
	public int insert(Area area) {
		Area parent = areaDao.selectByPrimaryKey(area.getParentId());// 获取父区域
		area.setParentIds(parent.getParentIds() + parent.getId() + ",");
		return areaDao.insertWithUuid(area);
	}
	
	/**
	 * 更新区域
	 * @param area
	 */
	public void updateArea(Area area) {
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		// 更新parentIds
		Area parent = areaDao.selectByPrimaryKey(area.getParentId());// 获取父区域
		area.setParentIds(parent.getParentIds() + parent.getId() + ",");
		areaDao.updateByPrimaryKey(area);
		// 更新子节点的parentIds
		if(StringUtils.isNotBlank(oldParentIds)){
			List<Area> childs = findChildsByPid(area.getId());
			for (Area e : childs) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
				areaDao.updateByPrimaryKey(e);
			}
		}
	}
	
	/**
	 * 删除区域及其子区域
	 * @param id
	 */
	public void deleteArea(String id) {
		List<Area> childs = findChildsByPid(id);
		List<String> ids = CollectionUtils.extractToList(childs, "id", true);
		ids.add(id);
		super.deleteByIds(ids);
	}
	
	/**
	 * 根据父节点id查找所有子节点
	 * @param id
	 * @return
	 */
	private List<Area> findChildsByPid(String id){
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().like("parent_ids", "," + id + ","));
		return areaDao.selectByCondition(condition);
	}
	
}
