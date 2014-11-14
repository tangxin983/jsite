package com.github.tx.jsite.modules.sys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.sys.dao.MenuDao;
import com.github.tx.jsite.modules.sys.dao.RoleMenuDao;
import com.github.tx.jsite.modules.sys.entity.Menu;
import com.github.tx.jsite.modules.sys.security.ShiroAuthorizingRealm;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.criteria.UpdateCondition;
import com.google.common.collect.Lists;

@Service
@Transactional
public class MenuService extends BaseService<Menu> {
	
	@Autowired
	private RoleMenuDao roleMenuDao;
	
	@Autowired
	private ShiroAuthorizingRealm shiroAuthorizingRealm;

	private MenuDao menuDao;

	@Autowired
	public void setMenuDao(MenuDao menuDao) {
		super.setDao(menuDao);
		this.menuDao = menuDao;
	}
	
	/**
	 * 根据用户id获取资源（包括权限和菜单）
	 * @param id
	 * @return
	 */
	public List<Menu> getResourceByUserId(String id) {
		List<Menu> menus = menuDao.findByUserId(id);
		List<Menu> result = Lists.newArrayList();
		if(menus != null){
			for(Menu menu : menus){
				setParent(menus, menu);
				setChildren(menus, menu);
				result.add(menu);
			}
		}
		return result;
	}
	
	/**
	 * 获取侧边栏菜单
	 * 
	 * @param list 用户资源集合
	 */
	public List<Menu> getSidebarMenus(List<Menu> list) {
		List<Menu> result = Lists.newArrayList();
		for (Menu menu : list) {
			// 选出一级菜单项
			if (menu.getParentId().equals("1") && menu.getIsShow().equals("1")) {
				// 获取一级菜单下的二级可见菜单项
				List<Menu> children = menu.getChildren();
				List<Menu> visible = Lists.newArrayList();
				if(children != null && !children.isEmpty()){
					for(Menu child : children){
						if(child.getIsShow().equals("1")){
							visible.add(child);
						}
					}
				}
				menu.setChildren(visible);
				result.add(menu);
			}
		}
		return result;
	}
	
	/**
	 * 获取shiro权限列表
	 * 
	 * @param list 用户资源集合
	 */
	public List<Menu> getPerms(List<Menu> list) {
		List<Menu> result = Lists.newArrayList();
		for (Menu menu : list) {
			if (StringUtils.isNotBlank(menu.getPermission())) {
				result.add(menu);
			}
		}
		return result;
	}
	
	/**
	 * 获取可点击的菜单列表
	 * 
	 * @param list 用户资源集合
	 */
	public List<Menu> getNavs(List<Menu> list) {
		List<Menu> result = Lists.newArrayList();
		for (Menu menu : list) {
			if (StringUtils.isNotBlank(menu.getHref())) {
				result.add(menu);
			}
		}
		return result;
	}
	
	/**
	 * 递归设置子菜单
	 * @param list 用户资源集合
	 * @param parent 父菜单
	 */
	private void setChildren(List<Menu> list, Menu parent) {
		parent.setChildren(new ArrayList<Menu>());
		for (Menu menu : list) {
			if (menu.getParentId().equals(parent.getId())) {
				setChildren(list, menu);
				parent.getChildren().add(menu);
			}
		}
	}
	
	/**
	 * 设置父菜单属性
	 * @param list 用户资源集合
	 * @param child 要设置属性的子菜单
	 */
	private void setParent(List<Menu> list, Menu child) {
		for (Menu menu : list) {
			if (child.getParentId().equals(menu.getId())) {
				child.setParent(menu);
			}
		}
	}
	
	/**
	 * 按照排序获取菜单列表
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Menu> findAllMenuBySort(Map<String, Object> searchParams) {
		QueryCondition condition = new QueryCondition();
		condition.asc("sort");
		return menuDao.selectByCondition(condition);
	}

	/**
	 * 删除菜单及其子菜单（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param id 要删除的菜单id
	 */
	public void deleteMenu(String id) {
		List<Menu> childs = findChildsByPid(id);
		List<String> ids = CollectionUtils.extractToList(childs, "id", true);
		ids.add(id);
		super.deleteByIds(ids);
		// 删除关联的RoleMenu记录
		for(String menuId : ids){
			UpdateCondition condition = new UpdateCondition();
			condition.or(Criteria.newCriteria().eq("menu_id", menuId));
			roleMenuDao.deleteByCondition(condition);
		}
		shiroAuthorizingRealm.clearCacheAndReloadFilterChain();
	}

	/**
	 * 保存菜单（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param menu
	 */
	public void saveMenu(Menu menu) {
		Menu parent = selectById(menu.getParentId());// 获取父菜单
		menu.setParentIds(parent.getParentIds() + parent.getId() + ",");
		dao.insertWithUuid(menu);
		shiroAuthorizingRealm.clearCacheAndReloadFilterChain();
	}

	/**
	 * 更新菜单（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param menu
	 */
	public void updateMenu(Menu menu) {
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		// 更新parentIds
		Menu parent = selectById(menu.getParentId());
		menu.setParentIds(parent.getParentIds() + parent.getId() + ",");
		dao.updateByPrimaryKey(menu);
		// 更新子节点的parentIds
		if(StringUtils.isNotBlank(oldParentIds)){
			List<Menu> childs = findChildsByPid(menu.getId());
			for (Menu e : childs) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
				dao.updateByPrimaryKey(e);
			}
		}
		shiroAuthorizingRealm.clearCacheAndReloadFilterChain();
	}
	
	/**
	 * 根据父节点id查找所有子节点
	 * @param id
	 * @return
	 */
	public List<Menu> findChildsByPid(String id){
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().like("parent_ids", "," + id + ","));
		return menuDao.selectByCondition(condition);
	}
	
	/**
	 * 根据链接查询记录条数以判断链接是否重复
	 * 
	 * @param roleName
	 * @return
	 */
	public long countByHref(String href) {
		QueryCondition condition = new QueryCondition();
		condition.or(Criteria.newCriteria().eq("href", href));
		return dao.countByCondition(condition);
	}

}
