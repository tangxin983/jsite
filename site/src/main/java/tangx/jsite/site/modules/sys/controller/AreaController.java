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
import tangx.jsite.site.modules.sys.entity.Area;
import tangx.jsite.site.modules.sys.service.AreaService;

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
	 * 跳转列表页<br>
	 * url:sys/area
	 */
	@RequestMapping
	public String view(Model model) {
		List<Area> sourcelist = areaService.findAreaOrderByCode();
		List<Area> list = Lists.newArrayList();
		sortAreaList(list, sourcelist, "1");
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
	private void sortAreaList(List<Area> list, List<Area> sourcelist,
			String parentId) {
		for (Area e : sourcelist) {
			if (e.getParentId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (Area child : sourcelist) {
					if (child.getParentId().equals(e.getId())) {
						sortAreaList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	/**
	 * 跳转新增页面<br>
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
	 * 新增操作<br>
	 * url:sys/area/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Area entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return createForm(entity, model);
		}
		areaService.saveArea(entity);
		addMessage(redirectAttributes, "保存区域'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 跳转更新页面<br>
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
	 * 更新操作<br>
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
	 * 删除操作<br>
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
		List<Area> list = areaService.findAreaOrderByCode();
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
