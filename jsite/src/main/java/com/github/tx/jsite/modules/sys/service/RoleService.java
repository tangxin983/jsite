package com.github.tx.jsite.modules.sys.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.jsite.core.exception.ServiceException;
import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.sys.dao.RoleDao;
import com.github.tx.jsite.modules.sys.dao.RoleMenuDao;
import com.github.tx.jsite.modules.sys.dao.UserRoleDao;
import com.github.tx.jsite.modules.sys.entity.Role;
import com.github.tx.jsite.modules.sys.entity.RoleMenu;
import com.github.tx.jsite.modules.sys.security.ShiroAuthorizingRealm;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.criteria.UpdateCondition;

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
	
	@Override
	public DataTableEntity<Role> selectByPage(HttpServletRequest request) {
		return super.selectByPage(request);
	}
	
	@Override
	public Role selectById(String id) {
		return roleDao.findRoleByPrimaryKey(id);
	}

	/**
	 * 根据角色名称查询记录数
	 * 
	 * @param roleName
	 * @return
	 */
	public long countRoleByName(String roleName) {
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().eq("name", roleName));
		return roleDao.countByCondition(condition);
	}
	
	/**
	 * 根据角色英文名查询记录数
	 * 
	 * @param enName
	 * @return
	 */
	public long countRoleByEnName(String enName) {
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().eq("en_name", enName));
		return roleDao.countByCondition(condition);
	}

	/**
	 * 查询角色对应的菜单id列表
	 * 
	 * @param roleName
	 * @return
	 */
	public List<String> findRoleMenu(String id) {
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().eq("role_id", id));
		List<RoleMenu> roleMenus = roleMenuDao.selectByCondition(condition);
		return CollectionUtils.extractToList(roleMenus, "menuId", true);
	}

	/**
	 * 保存角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param entity
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	@Override
	public int insert(Role entity) {
		int rows = roleDao.insertWithUuid(entity);
		saveRoleMenu(entity);
		saveActiviti(entity);
		return rows;
	}

	/**
	 * 更新角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param entity
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	@Override
	public int updateById(Role entity) {
		int rows = dao.updateByPrimaryKey(entity);
		saveRoleMenu(entity);
		saveActiviti(entity);
		return rows;
	}

	/**
	 * 删除角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param id
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	@Override
	public int deleteById(String id) {
		// 删除角色与菜单对应关系
		UpdateCondition condition = new UpdateCondition();
		condition.or(new Criteria().eq("role_id", id));
		roleMenuDao.deleteByCondition(condition);
		deleteActiviti(id);
		return dao.deleteByPrimaryKey(id);
	}
	
	/**
	 * 批量删除角色（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param ids
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	@Override
	public void deleteByIds(List<String> ids) {
		for(String id : ids){
			deleteById(id);
		}
	}
	

	/**
	 * 判断是否能够删除角色（已关联用户则不能删除）
	 * @param id
	 * @return 可以删除true不能删除false
	 */
	public boolean isDelete(String id) {
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().eq("role_id", id));
		if(userRoleDao.countByCondition(condition) > 0){
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
		UpdateCondition condition = new UpdateCondition();
		condition.or(new Criteria().eq("role_id", entity.getId()));
		roleMenuDao.deleteByCondition(condition);
		// 保存新的角色菜单对应关系
		if (entity.getMenuIds() != null && !entity.getMenuIds().isEmpty()) {
			for (String menuId : entity.getMenuIds()) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(entity.getId());
				roleMenu.setMenuId(menuId);
				roleMenuDao.insert(roleMenu);
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
