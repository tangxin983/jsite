package tangx.jsite.site.modules.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tangx.jsite.site.core.web.controller.BaseController;
import tangx.jsite.site.modules.sys.entity.Role;
import tangx.jsite.site.modules.sys.service.RoleService;
import tangx.jsite.site.utils.Constant;

import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "sys/role")
public class RoleController extends BaseController<Role> {

	private RoleService roleService;

	@Autowired
	public void setRoleService(RoleService roleService) {
		super.setService(roleService);
		this.roleService = roleService;
	}
	
	/**
	 * 跳转角色列表页（分页）
	 */
	@RequestMapping
	public String paginationList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		return super.paginationList(pageNumber, pageSize, model, request);
	}

	/**
	 * 跳转角色新增页面
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
//		model.addAttribute("menuList", menuService.findAllMenuBySort(null));
		return super.createForm(model);
	}

	/**
	 * 新增角色
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Role entity,
			RedirectAttributes redirectAttributes) {
		roleService.saveRole(entity);
		addMessage(redirectAttributes, "添加角色" + entity.getName() + "成功");
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 跳转角色更新页面
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
//		model.addAttribute("menuList", menuService.findAllMenuBySort(null));
		Role role = service.selectById(id);
		role.setMenuIds(roleService.findRoleMenu(id));
		model.addAttribute("entity", role);
		model.addAttribute("action", "update");
		return getUpdateFormPage();
	}
	
	/**
	 * 更新角色
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Role entity, RedirectAttributes redirectAttributes) {
		roleService.updateRole(entity);
		addMessage(redirectAttributes, "更新角色" + entity.getName() + "成功");
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 删除角色
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		if(roleService.isDelete(id)){
			roleService.deleteRole(id);
			addMessage(redirectAttributes, "删除成功");
		}else{
			addMessage(redirectAttributes, "无法删除已关联用户的角色");
		}
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 批量删除角色
	 */
	@RequestMapping("delete")
	public String multiDel(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		if(roleService.isDelete(ids)){
			roleService.deleteRole(ids);
			addMessage(redirectAttributes, "删除" + ids.size() + "条记录 成功");
		}else{
			addMessage(redirectAttributes, "无法删除已关联用户的角色");
		}
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 检查角色名称是否已经存在
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkName")
	@ResponseBody
	public Map<String, String> checkName(@RequestParam("oldName") String oldName,
			@RequestParam("name") String name) {
		Map<String, String> msg = Maps.newHashMap();
		if (name != null && name.equals(oldName)) {
			msg.put("success", "true");
		} else if (name != null && roleService.countRoleByName(name) == 0) {
			msg.put("success", "true");
		} else {
			msg.put("success", "false");
			msg.put("msg", "角色名称已存在");
		}
		return msg;
	}
	
	/**
	 * 检查角色英文名是否已经存在
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "checkEnName")
	@ResponseBody
	public Map<String, String> checkEnName(@RequestParam("oldName") String oldName,
			@RequestParam("enName") String name) {
		Map<String, String> msg = Maps.newHashMap();
		if (name != null && name.equals(oldName)) {
			msg.put("success", "true");
		} else if (name != null && roleService.countRoleByEnName(name) == 0) {
			msg.put("success", "true");
		} else {
			msg.put("success", "false");
			msg.put("msg", "角色英文名已存在");
		}
		return msg;
	}

}
