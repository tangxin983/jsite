package tangx.jsite.site.modules.sys.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import tangx.jsite.site.core.web.controller.BaseController;
import tangx.jsite.site.modules.sys.entity.Menu;
import tangx.jsite.site.modules.sys.service.MenuService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "sys/menu")
public class MenuController extends BaseController<Menu> {

	private MenuService menuService;

	@Autowired
	public void setMenuService(MenuService menuService) {
		super.setService(menuService);
		this.menuService = menuService;
	}

	/**
	 * 菜单列表页
	 * @param model
	 * @return
	 */
	@RequestMapping
	public String view(Model model) {
		List<Menu> sourcelist = menuService.findAllMenuBySort(null);
		List<Menu> list = Lists.newArrayList();
		sortMenuList(list, sourcelist, "1");
		model.addAttribute("entitys", list);
		return getListPage();
	}

	/**
	 * 对原始list进行整理以适合treetable要求
	 * 
	 * @param list
	 * @param sourcelist
	 * @param parentId
	 */
	private void sortMenuList(List<Menu> list, List<Menu> sourcelist,
			String parentId) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Menu e = sourcelist.get(i);
			if (e.getParentId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j = 0; j < sourcelist.size(); j++) {
					Menu child = sourcelist.get(j);
					if (child.getParentId().equals(e.getId())) {
						sortMenuList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	/**
	 * 新增菜单页面
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Menu menu, Model model) {
		// 如果没有传入父菜单id，则默认父菜单是顶级菜单
		if (menu == null) {
			menu = new Menu();
		}
		if (StringUtils.isBlank(menu.getParentId())) {
			menu.setParentId("1");
		}
		// 设置父菜单名称
		Menu parent = menuService.selectById(menu.getParentId());
		if (parent != null) {
			menu.setParentName(parent.getName());
		}
		model.addAttribute("entity", menu);
		model.addAttribute("action", "create");
		return getCreateFormPage();
	}

	/**
	 * 新增菜单
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@Override
	public String create(@Valid Menu entity, BindingResult result, Model model, 
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return createForm(entity, model);
		}
		menuService.saveMenu(entity);
		addMessage(redirectAttributes, "保存菜单'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 更新菜单页面
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		Menu menu = menuService.selectById(id);
		if (menu != null) {
			Menu parent = menuService.selectById(menu.getParentId());
			if (parent != null) {
				menu.setParentName(parent.getName());
			}
		}
		model.addAttribute("entity", menu);
		model.addAttribute("action", "update");
		return getUpdateFormPage();
	}

	/**
	 * 更新菜单
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@Override
	public String update(@Valid @ModelAttribute("entity") Menu entity, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return updateForm(entity.getId(), model);
		}
		menuService.updateMenu(entity);
		addMessage(redirectAttributes, "更新菜单'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 删除菜单
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id,
			RedirectAttributes redirectAttributes) {
		menuService.deleteMenu(id);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 获取树形菜单数据
	 * @param extId
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(
			@RequestParam(required = false) String extId) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = menuService.findAllMenuBySort(null);
		for (Menu e : list) {
			// 排除extId及其子菜单
			if (extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 检查菜单链接是否已经存在
	 * @param oldHref
	 * @param href
	 * @return
	 */
	@RequestMapping(value = "checkHref")
	@ResponseBody
	public Map<String, String> checkHref(@RequestParam("old") String oldHref,
			@RequestParam("href") String href) {
		Map<String, String> msg = Maps.newHashMap();
		if (href != null && href.equals(oldHref)) {
			msg.put("success", "true");
		} else if (href != null && menuService.countByHref(href) == 0) {
			msg.put("success", "true");
		} else {
			msg.put("success", "false");
			msg.put("msg", "此链接已存在");
		}
		return msg;
	}

}
