package tangx.jsite.site.modules.workflow.service;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.modules.sys.dao.RoleDao;
import tangx.jsite.site.modules.sys.dao.UserDao;
import tangx.jsite.site.modules.sys.dao.UserRoleDao;
import tangx.jsite.site.modules.sys.entity.Role;
import tangx.jsite.site.modules.sys.entity.User;
import tangx.jsite.site.modules.sys.entity.UserRole;
import tangx.jsite.site.modules.workflow.dao.ActIdentifyDao;

/**
 * 用于操作activiti的服务
 * @author tangx
 *
 */
@Service
@Transactional
public class ActivitiService {

	@Autowired
	private ActIdentifyDao actIdentifyDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private IdentityService identityService;

	/**
	 * 清空activiti用户数据并同步本系统用户数据到activiti
	 */
	public void syncActiviti() {
		deleteAllActivitiIdentifyData();
		syncRoleToActiviti();
		syncUserWithRoleToActiviti();
	}

	/**
	 * 复制用户以及关系数据
	 */
	private void syncUserWithRoleToActiviti() {
		// 复制角色
		List<User> allUser = userDao.select(User.class);
		for (User user : allUser) {
			saveActivitiUser(user);
		}
		// 复制角色和用户的关系
		List<UserRole> urList = userRoleDao.select(UserRole.class);
		for (UserRole ur : urList) {
			identityService.createMembership(ur.getUserId(), ur.getRoleId());
		}
	}
	
	/**
	 * 添加一个用户到activiti
	 * @param user
	 */
	private void saveActivitiUser(User user) {
		org.activiti.engine.identity.User newUser = identityService.newUser(user.getId());
		newUser.setFirstName(user.getName());
		newUser.setLastName(StringUtils.EMPTY);
		newUser.setPassword(StringUtils.EMPTY);
		newUser.setEmail(user.getEmail());
        identityService.saveUser(newUser);
    }

	/**
	 * 清空activiti用户、组、用户组关系数据
	 */
	private void deleteAllActivitiIdentifyData() {
		actIdentifyDao.deleteAllMemerShip();
		actIdentifyDao.deleteAllGroup();
		actIdentifyDao.deleteAllUser();
	}

	/**
	 * 同步所有角色数据到activiti
	 */
	private void syncRoleToActiviti() {
		List<Role> allRole = roleDao.select(Role.class);
		for (Role role : allRole) {
			Group group = identityService.newGroup(role.getId());
			group.setName(role.getName());
			identityService.saveGroup(group);
		}
	}
}
