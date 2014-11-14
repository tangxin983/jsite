package com.github.tx.jsite.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.jsite.core.exception.ServiceException;
import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.sys.dao.RoleDao;
import com.github.tx.jsite.modules.sys.dao.UserDao;
import com.github.tx.jsite.modules.sys.dao.UserRoleDao;
import com.github.tx.jsite.modules.sys.entity.Role;
import com.github.tx.jsite.modules.sys.entity.User;
import com.github.tx.jsite.modules.sys.entity.UserRole;
import com.github.tx.jsite.modules.sys.security.ShiroAuthorizingRealm;
import com.github.tx.jsite.utils.DigestUtil;
import com.github.tx.jsite.utils.SysUtil;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.criteria.UpdateCondition;

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
	
	@Override
	public DataTableEntity<User> selectByPage(HttpServletRequest request) {
		startPagination(request);
		QueryCondition condition = getQueryCondition(request);
		List<User> data = userDao.findUserList(condition.transform(User.class));
		for(User user : data){
			List<String> roleNames = new ArrayList<String>();
			for(Role role : user.getRoles()){
				roleNames.add(role.getName());
			}
			user.setRoleNames(roleNames);
		}
		return buildDataTableEntity(request, data);
	}

	/**
	 * 根据登录名查找用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User findUserByLoginName(String loginName) {
		QueryCondition condition = new QueryCondition();
		Criteria criteria = new Criteria();
		criteria.eq("loginName", loginName);
		condition.or(criteria);
		return userDao.findUserByLoginName(condition.transform(User.class));
	}
	
	/**
	 * 根据id查找用户
	 */
	@Override
	public User selectById(String id) {
		User user = userDao.findUserByPrimaryKey(id);
		if(user != null) {
			List<String> roleIds = CollectionUtils.extractToList(user.getRoles(), "id", true);
			user.setRoleIds(roleIds);
		}
		return user;
	}
	
	
	/**
	 * 添加用户
	 */
	@Override
	public int insert(User user) {
		user.setSalt(DigestUtil.generateSalt());
		user.setPassword(DigestUtil.generateHash(user.getPlainPassword(), user.getSalt()));
		int rows = userDao.insertWithUuid(user);
		saveUserRole(user);
		saveActiviti(user);
		return rows;
	}
	
	/**
	 * 更新用户（涉及到权限变更将清除权限缓存以便重新加载）
	 */
	@Override
	@CacheEvict(value = ShiroAuthorizingRealm.CACHE_NAME, allEntries = true)
	public int updateById(User user) {
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			user.setSalt(DigestUtil.generateSalt());
			user.setPassword(DigestUtil.generateHash(user.getPlainPassword(),
					user.getSalt()));
		}
		int rows = userDao.updateByPrimaryKey(user);
		saveUserRole(user);
		saveActiviti(user);
		return rows;
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
		userDao.updateByPrimaryKey(user);
		user.setRoles(SysUtil.getCurrentUser().getRoles());
		SysUtil.updateCurrentUser(user);
	}

	/**
	 * 删除用户及用户角色关系
	 */
	@Override
	public int deleteById(String id) {
		int rows = userDao.deleteByPrimaryKey(id);
		QueryCondition condition = new QueryCondition();
		condition.or(Criteria.newCriteria().eq("user_id", id));
		List<UserRole> userRoles = userRoleDao.selectByCondition(condition);
		deleteActiviti(id, userRoles);
		UpdateCondition updateCondition = new UpdateCondition();
		updateCondition.or(Criteria.newCriteria().eq("user_id", id));
		userRoleDao.deleteByCondition(updateCondition);
		return rows;
	}
	
	/**
	 * 批量删除用户及用户角色关系
	 */
	@Override
	public void deleteByIds(List<String> ids) {
		super.deleteByIds(ids);
	}
	
	/**
	 * 保存用户角色对应关系
	 * 
	 * @param user
	 */
	private void saveUserRole(User user) {
		// 删除旧的用户角色对应关系
		UpdateCondition condition = new UpdateCondition();
		condition.or(Criteria.newCriteria().eq("user_id", user.getId()));
		userRoleDao.deleteByCondition(condition);
		// 保存新的用户角色对应关系
		if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
			for (String roleId : user.getRoleIds()) {
				UserRole userRole = new UserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(user.getId());
				userRoleDao.insert(userRole);
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
