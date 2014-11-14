package tangx.jsite.site.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.sys.dao.OrgDao;
import tangx.jsite.site.modules.sys.entity.Org;

import com.github.tx.common.util.CollectionUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

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
		Map<String, String> orders = Maps.newHashMap();
		orders.put("code", "asc");
		return selectByOrder(orders);
	}
	
	/**
	 * 保存机构
	 * @param org
	 */
	public void saveOrg(Org org) {
		Org parent = selectById(org.getParentId());// 获取父区域
		org.setParentIds(parent.getParentIds() + parent.getId() + ",");
		orgDao.insert(org);
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
		orgDao.update(org);
		// 更新子节点的parentIds
		if(StringUtils.isNotBlank(oldParentIds)){
			List<Org> childs = findChildsByPid(org.getId());
			for (Org e : childs) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, org.getParentIds()));
				orgDao.update(e);
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
		delete(ids);
	}
	
	/**
	 * 根据父节点id查找所有子节点
	 * @param id
	 * @return
	 */
	private List<Org> findChildsByPid(String id){
		Table<String, String, Object> table = HashBasedTable.create();
		table.put("parentIds", "like", "," + id + ",");
		return select(table);
	}
}
