package tangx.jsite.site.modules.sys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tangx.jsite.site.modules.sys.entity.Menu;
import tangx.jsite.site.modules.sys.security.CaptchaAuthenticationFilter;
import tangx.jsite.site.utils.SysUtil;

import com.github.tx.common.util.CaptchaUtils;

/**
 * 登录管理
 * 
 */
@Controller
public class LoginController {

	/**
	 * 判断是否登录，如果未登录则跳转到登录页否则跳转到index页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login() {
		if (SysUtil.isAuthenticated() || SysUtil.isRemembered()) {
			return "redirect:/index";
		} else {
			return "modules/sys/login";
		}
	}

	/**
	 * shiro登录失败时会post到这个方法，为了用户友好性考虑需要把之前输入的用户名重新展示出来
	 * 
	 * @param userName
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginFail(@RequestParam("username") String userName,
			Model model) {
		if (SysUtil.isAuthenticated() || SysUtil.isRemembered()) {
			return "redirect:/index";
		}
		model.addAttribute("username", userName);
		return "modules/sys/login";
	}

	/**
	 * 生成验证码
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getCaptcha", method = RequestMethod.GET, produces = MediaType.IMAGE_GIF_VALUE)
	@ResponseBody
	public byte[] getCaptcha() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String captcha = CaptchaUtils.getGifCaptcha(80, 32, 4, outputStream, 1000).toLowerCase();
		SysUtil.setAttribute(CaptchaAuthenticationFilter.DEFAULT_CAPTCHA_PARAM, captcha);
		return outputStream.toByteArray();
	}
	
	/**
	 * 生成面包屑导航
	 * @param id 被选中的菜单id
	 * @return
	 */
	@RequestMapping("/breadcrumb")
    @ResponseBody
    public String breadcrumb(@RequestParam("id") String id){
		StringBuilder sb = new StringBuilder();
		List<Menu> menus = SysUtil.getNavs();
		Menu selected = null;
		if (menus != null) {
			for (Menu menu : menus) {
				if (menu.getId().equals(id)) {
					selected = menu;
				}
			}
			if (selected != null) {
				sb.append("<li><a href=\"#\">");
				Menu parent = selected.getParent();
				sb.append(parent.getName());
				sb.append("</a></li>");
				sb.append("<li class=\"active\">");
				sb.append(selected.getName());
				sb.append("</li>");
			}
		}
		return sb.toString();
    }

	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "modules/sys/index";
	}

}
