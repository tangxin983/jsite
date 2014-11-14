package tangx.jsite.site.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.sys.dao.AreaDao;
import tangx.jsite.site.modules.sys.entity.Area;

import com.github.tx.common.util.CollectionUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

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
	 * 按照编码排序获取区域列表
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Area> findAreaOrderByCode() {
		Map<String, String> orders = Maps.newHashMap();
		orders.put("code", "asc");
		return selectByOrder(orders);
	}
	
	/**
	 * 根据名称获取区域列表
	 * 
	 * @param name
	 * @return
	 */
	public List<Area> findAreaByName(String name) {
		Map<String, Object> para = Maps.newHashMap();
		para.put("name", name);
		return select(para);
	}
	 
	/**
	 * 保存区域
	 * @param area
	 */
	public void saveArea(Area area) {
		Area parent = selectById(area.getParentId());// 获取父区域
		area.setParentIds(parent.getParentIds() + parent.getId() + ",");
		areaDao.insert(area);
	}
	
	/**
	 * 更新区域
	 * @param area
	 */
	public void updateArea(Area area) {
		String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		// 更新parentIds
		Area parent = selectById(area.getParentId());
		area.setParentIds(parent.getParentIds() + parent.getId() + ",");
		areaDao.update(area);
		// 更新子节点的parentIds
		if(StringUtils.isNotBlank(oldParentIds)){
			List<Area> childs = findChildsByPid(area.getId());
			for (Area e : childs) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
				areaDao.update(e);
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
		super.delete(ids);
	}
	
	/**
	 * 根据父节点id查找所有子节点
	 * @param id
	 * @return
	 */
	private List<Area> findChildsByPid(String id){
		Table<String, String, Object> table = HashBasedTable.create();
		table.put("parentIds", "like", "," + id + ",");
		return select(table);
	}
	
}
