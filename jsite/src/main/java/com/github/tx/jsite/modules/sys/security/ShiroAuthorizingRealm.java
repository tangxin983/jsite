package com.github.tx.jsite.modules.sys.security;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.tx.common.util.CollectionUtils;
import com.github.tx.common.util.Encodes;
import com.github.tx.jsite.core.exception.ServiceException;
import com.github.tx.jsite.core.persistence.entity.ShiroEntity;
import com.github.tx.jsite.modules.sys.entity.Menu;
import com.github.tx.jsite.modules.sys.entity.User;
import com.github.tx.jsite.modules.sys.service.MenuService;
import com.github.tx.jsite.modules.sys.service.UserService;
import com.github.tx.jsite.utils.SysUtil;

/**
 * 系统安全认证实现类
 * @author tangx
 *
 */
@Service
public class ShiroAuthorizingRealm extends AuthorizingRealm {
	
	private static Logger logger = LoggerFactory.getLogger(ShiroAuthorizingRealm.class);
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final String CACHE_NAME = "shiroAuthorizationCache";
	
	@Value("${shiro.defaultFilterChain}")
	private String defaultFilterChainDefinitions;// 配置文件中默认的filterChain
	
	@Value("${shiro.defaultPermission}")
	private String defaultPermission;// 配置文件中默认的权限,如果存在多个值，使用逗号分割

	@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuService menuService;
	
	
	/**
	 * 1、设定密码校验的Hash算法与迭代次数<br>
	 * 2、设定缓存参数名（与ehcache.xml中的一致）
	 */
	@PostConstruct
	public void initShiroConfig() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
		matcher.setHashIterations(HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
		setAuthorizationCacheName(CACHE_NAME);
	}
	
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		if (username == null) {
			throw new AccountException("用户名不能为空");
		}
		User user = userService.findUserByLoginName(username);
		if (user != null) {
			byte[] salt = Encodes.decodeHex(user.getSalt());
			return new SimpleAuthenticationInfo(new ShiroEntity(user),
					user.getPassword(), ByteSource.Util.bytes(salt), getName());
		} else {
			throw new UnknownAccountException("用户不存在");
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroEntity shiroEntity = (ShiroEntity) principals.getPrimaryPrincipal();
		List<Menu> resources = menuService.getResourceByUserId(shiroEntity.getUser().getId().toString());//总资源
		List<Menu> menus = menuService.getSidebarMenus(resources);// 侧边栏
		List<Menu> navs = menuService.getNavs(resources);// 可点击菜单
		List<Menu> perms = menuService.getPerms(resources);// 权限
		shiroEntity.setPerms(perms);
		shiroEntity.setMenus(menus);
		shiroEntity.setNavs(navs);
		shiroEntity.setResources(resources);
		// 添加permission
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		addPermissions(info, perms);
		// 将shiroEntity放入session
		SysUtil.setAttribute("shiroEntity", shiroEntity);
		return info;
	}

	private void addPermissions(SimpleAuthorizationInfo info, List<Menu> authorizationInfo) {
		// 解析当前用户资源中的permissions
		List<String> permissions = CollectionUtils.extractToList(authorizationInfo, "permission", true);
		//添加默认的permissions到permissions
        if (StringUtils.isNotBlank(defaultPermission)) {
        	String[] perms = StringUtils.split(defaultPermission, ",");
        	CollectionUtils.addAll(permissions, perms);
        }
		// 将当前用户拥有的permissions设置到SimpleAuthorizationInfo中
		if (permissions != null && !permissions.isEmpty()) {
			logger.debug("perms:{}", permissions.toString());
			info.addStringPermissions(permissions);
		}
	}

	/**
	 * 服务启动时加载数据库中权限配置（permission）
	 * @return
	 */
	public String loadFilterChainDefinitions() {
		StringBuffer sb = new StringBuffer("");
		// 加载数据库配置
		List<Menu> menuList = menuService.findAllMenuBySort(null);
		for (Menu menu : menuList) {
			if (StringUtils.isNotBlank(menu.getHref()) && StringUtils.isNotBlank(menu.getPermission())) {
				String uri = menu.getHref().startsWith("/") ? menu.getHref() : "/" + menu.getHref();
				sb.append(uri + "=" + MessageFormat.format("perms[\"{0}\"]", menu.getPermission()) + "\r\n");
			}
		}
		// 加载默认filter
		for (String def : StringUtils.split(defaultFilterChainDefinitions, ",")) {
			sb.append(def.trim() + "\r\n");
		}
		return sb.toString();
	}
	
	/**
	 * 重新加载数据库权限
	 */
	public synchronized void reloadShiroFilterChains() {
		try {
			AbstractShiroFilter shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
			PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
			DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
			//清空老的权限控制
			manager.getFilterChains().clear();
			shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
			//加载新的权限控制
		    shiroFilterFactoryBean.setFilterChainDefinitions(loadFilterChainDefinitions());
			for(Map.Entry<String, String> entry : shiroFilterFactoryBean.getFilterChainDefinitionMap().entrySet()) {
	            manager.createChain(entry.getKey(), entry.getValue().trim());
	        }
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 清除缓存并重新加载数据库权限
	 */
	public void clearCacheAndReloadFilterChain() {
		getAuthorizationCache().clear();
		reloadShiroFilterChains();
	}
	
	/**
	 * 清除缓存
	 */
	public void clearCache() {
		getAuthorizationCache().clear();
	}
}
