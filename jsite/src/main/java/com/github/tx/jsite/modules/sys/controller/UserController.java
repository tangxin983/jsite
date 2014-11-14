package com.github.tx.jsite.modules.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.controller.BaseController;
import com.github.tx.jsite.modules.sys.entity.User;
import com.github.tx.jsite.modules.sys.service.RoleService;
import com.github.tx.jsite.modules.sys.service.UserService;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "sys/user")
public class UserController extends BaseController<User> {
	
	@Autowired
	private RoleService roleService;

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		super.setService(userService);
		this.userService = userService;
	}
	
	/**
	 * 展示列表页<p>
	 * url:sys/user
	 */
	@RequestMapping
	public String list(Model model) {
		return super.list(model);
	}
	
	/**
	 * 获取数据<p>
	 * url:sys/user?datagrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public DataTableEntity<User> datagrid(HttpServletRequest request) {
		return super.datagrid(request);
	}
	
	/**
	 * 跳转到用户新增页面
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("roles", roleService.select());
		return super.createForm(model);
	}
	
	/**
	 * 新增用户
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid User entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 跳转到用户更新页面
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("roles", roleService.select());
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新用户
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")User entity, BindingResult result, 
		Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 首页更新个人信息
	 */
	@RequestMapping(value = "updateInfo", method = RequestMethod.POST)
	public String updateInfo(@Valid @ModelAttribute("entity")User entity, RedirectAttributes redirectAttributes) {
		userService.updateInfo(entity);
		addMessage(redirectAttributes, "更新个人信息成功");
		return "redirect:/index";
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		if(!User.isAdmin(id)){
			userService.deleteById(id);
			addMessage(redirectAttributes, "删除成功");
		}else{
			addMessage(redirectAttributes, "无法删除管理员账号");
		}
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 批量删除用户
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		if(!User.isAdmin(ids)){
			userService.deleteByIds(ids);
			addMessage(redirectAttributes, "删除" + ids.size() + "条记录 成功");
		}else{
			addMessage(redirectAttributes, "无法删除管理员账号");
		}
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 判断登录名是否已经存在
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkName")
	@ResponseBody
	public Map<String, String> checkName(@RequestParam("oldName") String oldName,
			@RequestParam("loginName") String name) {
		Map<String, String> msg = Maps.newHashMap();
		if (name != null && name.equals(oldName)) {
			msg.put("success", "true");
		} else if (name != null && userService.findUserByLoginName(name) == null) {
			msg.put("success", "true");
		} else {
			msg.put("success", "false");
			msg.put("msg", "登录名已存在");
		}
		return msg;
	}
}
