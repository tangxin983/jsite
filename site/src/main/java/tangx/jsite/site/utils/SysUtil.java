package tangx.jsite.site.utils;

import java.util.List;

import org.apache.shiro.SecurityUtils;

import tangx.jsite.site.core.persistence.entity.ShiroEntity;
import tangx.jsite.site.modules.sys.entity.Menu;
import tangx.jsite.site.modules.sys.entity.User;

public class SysUtil {
	
	/**
	 * 获取当前用户对象.
	 */
	public static User getCurrentUser() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.getUser(): null;
	}

	/**
	 * 获取当前用户Id.
	 */
	public static String getCurrentUserId() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.getUser().getId().toString() : null;
	}

	/**
	 * 获取当前用户姓名.
	 */
	public static String getCurrentUserName() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.toString() : null;
	}

	/**
	 * 更新当前用户对象.
	 */
	public static void updateCurrentUser(User user) {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		if (entity != null) {
			entity.setUser(user);
		}
	}

	/**
	 * 将对象放入shiro session中
	 * 
	 * @param attrName
	 *            属性名
	 * @param obj
	 *            对象
	 */
	public static void setAttribute(String attrName, Object obj) {
		SecurityUtils.getSubject().getSession().setAttribute(attrName, obj);
	}

	/**
	 * 取出shiro session中的对象
	 * 
	 * @param attrName
	 *            属性名
	 * @return
	 */
	public static Object getAttribute(String attrName) {
		return SecurityUtils.getSubject().getSession().getAttribute(attrName);
	}

	/**
	 * 判断当前会话是否处于登录状态
	 * 
	 * @return
	 */
	public static boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}

	/**
	 * 判断之前是否有会话被记住
	 * 
	 * @return
	 */
	public static boolean isRemembered() {
		return SecurityUtils.getSubject().isRemembered();
	}

	/**
	 * 判断当前会话是否过期
	 * 
	 * @return 过期返回true
	 */
	public static boolean isSessionTimeOut() {
		return SecurityUtils.getSubject().getSession(false) == null;
	}

	/**
	 * 取出当前用户侧边栏
	 */
	public static List<Menu> getMenus() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.getMenus() : null;
	}

	/**
	 * 取出当前用户权限
	 */
	public static List<Menu> getPerms() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.getPerms() : null;
	}
	
	/**
	 * 取出当前用户可见菜单
	 */
	public static List<Menu> getNavs() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.getNavs() : null;
	}
	
	/**
	 * 取出当前用户所有资源
	 */
	public static List<Menu> getResources() {
		ShiroEntity entity = (ShiroEntity) SecurityUtils.getSubject().getPrincipal();
		return entity != null ? entity.getResources() : null;
	}
}
