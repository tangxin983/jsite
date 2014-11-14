package tangx.jsite.site.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.exception.ServiceException;
import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.sys.dao.RoleDao;
import tangx.jsite.site.modules.sys.dao.UserDao;
import tangx.jsite.site.modules.sys.dao.UserRoleDao;
import tangx.jsite.site.modules.sys.entity.User;
import tangx.jsite.site.modules.sys.entity.UserRole;
import tangx.jsite.site.modules.sys.security.ShiroAuthorizingRealm;
import tangx.jsite.site.utils.DigestUtil;
import tangx.jsite.site.utils.SysUtil;

import com.github.tx.common.util.CollectionUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

@Service
@Transactional
public class UserService extends BaseService<User> {
	
	@Autowired
	private IdentityService identityService;

	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private RoleDao roleDao;

	private UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		super.setDao(userDao);
		this.userDao = userDao;
	}
	
	/**
	 * 列表页分页模糊查询(覆盖BaseService)
	 */
	@Override
	public Page<User> selectByPage(Map<String, Object> searchParams, Map<String, String> orders, int pageNumber, int pageSize) {
		Page<User> p = buildPage(pageNumber, pageSize);
		Table<String, String, Object> searchTable = HashBasedTable.create();
		for(String key : searchParams.keySet()) {
			searchTable.put(key, "like", searchParams.get(key));
		}
		List<User> users = userDao.selectByComplicated(genericType, p, searchTable, orders);
		for(User user : users) {
			user.setRoles(roleDao.findRolesByUserId(user.getId()));
		}
		p.setData(userDao.selectByComplicated(genericType, p, searchTable, orders));
		return p;
	}

	/**
	 * 根据登录名查找用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User findUserByLoginName(String loginName) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("loginName", loginName);
		List<User> users = select(param);
		User user = null;
		if(users != null && users.size() > 0) {
			user = users.get(0);
			user.setRoles(roleDao.findRolesByUserId(user.getId()));
		}
		return user;
	}
	
	/**
	 * 根据id查找用户
	 * 
	 * @param id 用户id
	 * @return
	 */
	public User findUserById(String id) {
		User user = selectById(id);
		if(user != null) {
			user.setRoles(roleDao.findRolesByUserId(user.getId()));
			List<String> roleIds = CollectionUtils.extractToList(user.getRoles(), "id", true);
			user.setRoleIds(roleIds);
		}
		return user;
	}

	/**
	 * 添加用户
	 * 
	 * @param user
	 */
	public void saveUser(User user) {
		user.setSalt(DigestUtil.generateSalt());
		user.setPassword(DigestUtil.generateHash(user.getPlainPassword(),
				user.getSalt()));
		userDao.insert(user);
		saveUserRole(user);
		saveActiviti(user);
	}

	/**
	 * 更新用户（涉及到权限变更将清除权限缓存以便重新加载）
	 * 
	 * @param user
	 */
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	public void updateUser(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.setSalt(DigestUtil.generateSalt());
			user.setPassword(DigestUtil.generateHash(user.getPlainPassword(),
					user.getSalt()));
		}
		userDao.update(user);
		saveUserRole(user);
		saveActiviti(user);
	}
	
	/**
	 * 首页更新个人信息
	 * @param user
	 */
	public void updateInfo(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.setSalt(DigestUtil.generateSalt());
			user.setPassword(DigestUtil.generateHash(user.getPlainPassword(),
					user.getSalt()));
		}
		userDao.update(user);
		user.setRoles(SysUtil.getCurrentUser().getRoles());
		SysUtil.updateCurrentUser(user);
	}

	/**
	 * 删除用户及用户角色关系
	 * 
	 * @param id
	 */
	public void deleteUser(String id) {
		userDao.deleteById(genericType, id);
		Map<String, Object> para = Maps.newHashMap();
		para.put("userId", id);
		List<UserRole> userRoles = userRoleDao.selectByCondition(UserRole.class, null, para, null);
		deleteActiviti(id, userRoles);
		userRoleDao.deleteByCondition(UserRole.class, para);
	}
	
	/**
	 * 批量删除用户及用户角色关系
	 * 
	 * @param ids
	 */
	public void deleteUser(List<String> ids) {
		for(String id : ids){
			deleteUser(id);
		}
	}
	
	/**
	 * 保存用户角色对应关系
	 * 
	 * @param user
	 */
	private void saveUserRole(User user) {
		// 删除旧的用户角色对应关系
		Map<String, Object> para = Maps.newHashMap();
		para.put("userId", user.getId());
		userRoleDao.deleteByCondition(UserRole.class, para);
		// 保存新的用户角色对应关系
		if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
			for (String roleId : user.getRoleIds()) {
				UserRole userRole = new UserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(user.getId());
				userRoleDao.insertWithoutId(userRole);
			}
		}
	}
	
	/**
	 * 将用户数据同步到activiti
	 * @param user
	 */
	private void saveActiviti(User user) {
		if (user != null) {
			String userId = user.getId();
			List<org.activiti.engine.identity.User> activitiUserList = identityService
					.createUserQuery().userId(userId).list();
			if (activitiUserList.size() == 1) {
				// 更新Activiti用户数据
				cloneAndSaveActivitiUser(user, activitiUserList.get(0));
				saveActivitiMembership(user);
            } else if (activitiUserList.size() > 1) {
            	// 用户重复
                String errorMsg = "duplicate activiti user：id=" + userId;
                logger.error(errorMsg);
                throw new ServiceException(errorMsg);
            } else {
            	// 添加Activiti用户数据
            	org.activiti.engine.identity.User newUser = identityService.newUser(userId);
            	cloneAndSaveActivitiUser(user, newUser);
            	saveActivitiMembership(user);
            }
		}
	}
	
	/**
	 * 拷贝系统用户属性到activiti用户并保存
	 * @param user 系统用户
	 * @param activitiUser activiti用户
	 */
	private void cloneAndSaveActivitiUser(User user, org.activiti.engine.identity.User activitiUser) {
        activitiUser.setFirstName(user.getName());
        activitiUser.setLastName(StringUtils.EMPTY);
        activitiUser.setPassword(StringUtils.EMPTY);
        activitiUser.setEmail(user.getEmail());
        identityService.saveUser(activitiUser);
    }
	
	/**
	 * 保存activiti用户与组的关系
	 * @param user 系统用户
	 */
    private void saveActivitiMembership(User user) {
    	// 删除旧的membership
        List<Group> activitiGroups = identityService.createGroupQuery().groupMember(user.getId()).list();
        for (Group group : activitiGroups) {
            identityService.deleteMembership(user.getId(), group.getId());
        }
        // 添加新的membership
    	if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
    		for (String roleId : user.getRoleIds()) {
    			identityService.createMembership(user.getId(), roleId);
    		}
    	}
    }
    
   /**
    * 删除activiti用户、删除activiti用户与组的关系
    * @param userId
    * @param userRoles
    */
	private void deleteActiviti(String userId, List<UserRole> userRoles) {
		if (!userRoles.isEmpty()) {
    		for (UserRole userRole : userRoles) {
    			identityService.deleteMembership(userId, userRole.getRoleId());
    		}
    	}
		identityService.deleteUser(userId);
	}

}
