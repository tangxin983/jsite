package com.github.tx.jsite.modules.sys.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.sys.dao.OrgDao;
import com.github.tx.jsite.modules.sys.entity.Org;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;

/**
 * 机构Service
 * @author tangx
 * @since 2014-05-15
 */
@Service
@Transactional
public class OrgService extends BaseService<Org> {

	private OrgDao orgDao;

	@Autowired
	public void setOrgDao(OrgDao orgDao) {
		super.setDao(orgDao);
		this.orgDao = orgDao;
	}
	
	/**
	 * 按照编码排序获取机构列表
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Org> findOrgOrderByCode() {
		QueryCondition condition = new QueryCondition();
		condition.asc("code");
		return select(condition);
	}
	
	/**
	 * 保存机构
	 * @param org
	 */
	public void saveOrg(Org org) {
		Org parent = selectById(org.getParentId());// 获取父区域
		org.setParentIds(parent.getParentIds() + parent.getId() + ",");
		orgDao.insertWithUuid(org);
	}
	
	/**
	 * 更新机构
	 * @param org
	 */
	public void updateOrg(Org org) {
		String oldParentIds = org.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		// 更新parentIds
		Org parent = selectById(org.getParentId());
		org.setParentIds(parent.getParentIds() + parent.getId() + ",");
		orgDao.updateByPrimaryKey(org);
		// 更新子节点的parentIds
		if(StringUtils.isNotBlank(oldParentIds)){
			List<Org> childs = findChildsByPid(org.getId());
			for (Org e : childs) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, org.getParentIds()));
				orgDao.updateByPrimaryKey(e);
			}
		}
	}
	
	/**
	 * 删除机构及其子机构
	 * @param id
	 */
	public void deleteOrg(String id) {
		List<Org> childs = findChildsByPid(id);
		List<String> ids = CollectionUtils.extractToList(childs, "id", true);
		ids.add(id);
		deleteByIds(ids);
	}
	
	/**
	 * 根据父节点id查找所有子节点
	 * @param id
	 * @return
	 */
	private List<Org> findChildsByPid(String id){
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().like("parent_ids", "," + id + ","));
		return select(condition);
	}
}
