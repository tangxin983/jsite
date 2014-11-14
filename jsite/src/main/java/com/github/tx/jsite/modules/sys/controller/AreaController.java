package com.github.tx.jsite.modules.sys.controller;

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

import com.github.tx.jsite.core.web.controller.BaseController;
import com.github.tx.jsite.modules.sys.entity.Area;
import com.github.tx.jsite.modules.sys.service.AreaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 区域Controller
 * 
 * @author tangx
 * @version 2014-05-12
 */
@Controller
@RequestMapping(value = "sys/area")
public class AreaController extends BaseController<Area> {

	private AreaService areaService;

	@Autowired
	public void setAreaService(AreaService areaService) {
		super.setService(areaService);
		this.areaService = areaService;
	}

	/**
	 * 展示列表页<p>
	 * url:sys/area
	 */
	@RequestMapping
	public String list(Model model) {
		model.addAttribute("entitys", areaService.getTreeList());
		return getListPage();
	}

	/**
	 * 跳转新增页面<p>
	 * url:sys/area/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Area area, Model model) {
		// 如果没有传入父菜单id，则默认父菜单是顶级菜单
		if (area == null) {
			area = new Area();
		}
		if (StringUtils.isBlank(area.getParentId())) {
			area.setParentId("1");
		}
		// 设置父菜单名称
		Area parent = areaService.selectById(area.getParentId());
		if (parent != null) {
			area.setParentName(parent.getName());
		}
		model.addAttribute("entity", area);
		model.addAttribute("action", "create");
		return getCreateFormPage();
	}

	/**
	 * 新增操作<p>
	 * url:sys/area/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Area entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return createForm(entity, model);
		}
		areaService.insert(entity);
		addMessage(redirectAttributes, "保存区域'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 跳转更新页面<p>
	 * URL:sys/area/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		Area area = areaService.selectById(id);
		if (area != null) {
			Area parent = areaService.selectById(area.getParentId());
			if (parent != null) {
				area.setParentName(parent.getName());
			}
		}
		model.addAttribute("entity", area);
		model.addAttribute("action", "update");
		return getUpdateFormPage();
	}

	/**
	 * 更新操作<p>
	 * URL:sys/area/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") Area entity,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return updateForm(entity.getId(), model);
		}
		areaService.updateArea(entity);
		addMessage(redirectAttributes, "更新区域'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 删除操作<p>
	 * URL:sys/area/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id,
			RedirectAttributes redirectAttributes) {
		areaService.deleteArea(id);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 获取树形菜单数据
	 * 
	 * @param extId
	 * @return
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(
			@RequestParam(required = false) String extId) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.getTreeList();
		for (Area e : list) {
			// 排除extId及其子菜单
			if (extId == null
					|| (extId != null && !extId.equals(e.getId()) && e
							.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

}
