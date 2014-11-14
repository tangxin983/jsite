package tangx.jsite.site.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.exception.ServiceException;
import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.sys.dao.RoleDao;
import tangx.jsite.site.modules.sys.dao.RoleMenuDao;
import tangx.jsite.site.modules.sys.dao.UserRoleDao;
import tangx.jsite.site.modules.sys.entity.Role;
import tangx.jsite.site.modules.sys.entity.RoleMenu;
import tangx.jsite.site.modules.sys.entity.UserRole;
import tangx.jsite.site.modules.sys.security.ShiroAuthorizingRealm;

import com.github.tx.common.util.CollectionUtils;
import com.google.common.collect.Maps;

@Service
@Transactional
public class RoleService extends BaseService<Role> {

	private RoleDao roleDao;

	@Autowired
	private RoleMenuDao roleMenuDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private IdentityService identityService;

	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		super.setDao(roleDao);
		this.roleDao = roleDao;
	}

	/**
	 * 根据角色名称查询记录数
	 * 
	 * @param roleName
	 * @return
	 */
	public long countRoleByName(String roleName) {
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("name", roleName);
		return dao.countByCondition(genericType, searchParams);
	}
	
	/**
	 * 根据角色英文名查询记录数
	 * 
	 * @param enName
	 * @return
	 */
	public long countRoleByEnName(String enName) {
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("enName", enName);
		return dao.countByCondition(genericType, searchParams);
	}

	/**
	 * 查询角色对应的菜单id列表
	 * 
	 * @param roleName
	 * @return
	 */
	public List<String> findRoleMenu(String id) {
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("roleId", id);
		List<RoleMenu> roleMenus = roleMenuDao.selectByCondition(RoleMenu.class, null, searchParams, null);
		return CollectionUtils.extractToList(roleMenus, "menuId", true);
	}

	/**
	 * 保存角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param entity
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	public void saveRole(Role entity) {
		dao.insert(entity);
		saveRoleMenu(entity);
		saveActiviti(entity);
	}

	/**
	 * 更新角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param entity
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	public void updateRole(Role entity) {
		dao.update(entity);
		saveRoleMenu(entity);
		saveActiviti(entity);
	}

	/**
	 * 删除角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param id
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	public void deleteRole(String id) {
		dao.deleteById(genericType, id);
		// 删除角色与菜单对应关系
		Map<String, Object> para = Maps.newHashMap();
		para.put("roleId", id);
		roleMenuDao.deleteByCondition(RoleMenu.class, para);
		deleteActiviti(id);
	}
	
	/**
	 * 批量删除角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param ids
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	public void deleteRole(List<String> ids) {
		for(String id : ids){
			deleteRole(id);
		}
	}
	

	/**
	 * 判断是否能够删除角色（已关联用户则不能删除）
	 * @param id
	 * @return 可以删除true不能删除false
	 */
	public boolean isDelete(String id) {
		Map<String, Object> para = Maps.newHashMap();
		para.put("roleId", id);
		if(userRoleDao.countByCondition(UserRole.class, para) > 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否能够删除角色（已关联用户则不能删除）
	 * @param ids
	 * @return 可以删除true不能删除false
	 */
	public boolean isDelete(List<String> ids) {
		for(String id : ids){
			if(!isDelete(id)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 保存角色菜单关系
	 * 
	 * @param entity
	 */
	private void saveRoleMenu(Role entity) {
		// 删除旧的角色与菜单对应关系
		Map<String, Object> para = Maps.newHashMap();
		para.put("roleId", entity.getId());
		roleMenuDao.deleteByCondition(RoleMenu.class, para);
		// 保存新的角色菜单对应关系
		if (entity.getMenuIds() != null && !entity.getMenuIds().isEmpty()) {
			for (String menuId : entity.getMenuIds()) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(entity.getId());
				roleMenu.setMenuId(menuId);
				roleMenuDao.insertWithoutId(roleMenu);
			}
		}
	}
	
	/**
	 * 将用户组数据同步到activiti
	 * @param role
	 */
	private void saveActiviti(Role role) {
		if (role != null) {
			String groupId = role.getId();
			List<Group> activitiGroupList = identityService.createGroupQuery().groupId(groupId).list();
			if (activitiGroupList.size() == 1) {
				// 更新activiti用户组数据
				Group group = activitiGroupList.get(0);
				group.setName(role.getName());
				identityService.saveGroup(group);
            } else if (activitiGroupList.size() > 1) {
            	// 角色重复
                String errorMsg = "duplicate activiti group：id=" + groupId;
                logger.error(errorMsg);
                throw new ServiceException(errorMsg);
            } else {
            	// 添加activiti用户组数据
            	Group newGroup = identityService.newGroup(groupId);
            	newGroup.setName(role.getName());
            	identityService.saveGroup(newGroup);
            }
		}
	}
	
	/**
     * 删除activiti用户组
     * @param roleId 
     */
	private void deleteActiviti(String roleId) {
		identityService.deleteGroup(roleId);
	}
	
	 
}
