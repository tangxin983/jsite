package tangx.jsite.site.modules.sys.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标签控制器
 * @author tangx
 *
 */
@Controller
@RequestMapping(value = "sys/tag")
public class TagController {
	
	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", StringUtils.defaultString(request.getParameter("url"))); 	// 树结构数据URL
		model.addAttribute("extId", StringUtils.defaultString(request.getParameter("extId"))); // 排除的编号ID
		model.addAttribute("checked", StringUtils.defaultString(request.getParameter("checked"))); // 是否可复选
		model.addAttribute("selectIds", StringUtils.defaultString(request.getParameter("selectIds"))); // 指定默认选中的ID
		model.addAttribute("id", StringUtils.defaultString(request.getParameter("id"))); // 模块id
		return "modules/sys/tagTreeselect";
	}
	
}
