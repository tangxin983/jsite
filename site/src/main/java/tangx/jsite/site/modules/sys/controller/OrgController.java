package tangx.jsite.site.modules.sys.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import tangx.jsite.site.modules.sys.entity.Area;
import tangx.jsite.site.modules.sys.entity.Org;
import tangx.jsite.site.modules.sys.service.AreaService;
import tangx.jsite.site.modules.sys.service.OrgService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 机构Controller
 * @author tangx
 * @version 2014-05-15
 */
@Controller
@RequestMapping(value = "sys/org")
public class OrgController extends BaseController<Org> {

	private OrgService orgService;
	
	@Autowired
	private AreaService areaService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		super.setService(orgService);
		this.orgService = orgService;
	}
	
	/**
	 * 跳转列表页<br>
	 * url:sys/org
	 */
	@RequestMapping
	public String view(Model model) {
		List<Org> sourcelist = orgService.findOrgOrderByCode();
		List<Org> list = Lists.newArrayList();
		sortList(list, sourcelist, "1");
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
	private void sortList(List<Org> list, List<Org> sourcelist,
			String parentId) {
		for (Org e : sourcelist) {
			if (e.getParentId().equals(parentId)) {
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (Org child : sourcelist) {
					if (child.getParentId().equals(e.getId())) {
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 跳转新增页面<br>
	 * url:sys/org/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Org org, Model model) {
		// 如果没有传入父id，则默认上级机构是总公司，默认归属区域是中国
		if (org == null) {
			org = new Org();
		}
		if (StringUtils.isBlank(org.getParentId())) {
			org.setParentId("1");
			org.setAreaId("1");
		}
		// 设置父机构名称
		Org parent = orgService.selectById(org.getParentId());
		if (parent != null) {
			org.setParentName(parent.getName());
		}
		// 设置归属区域名称
		Area area = areaService.selectById(org.getAreaId());
		if(area != null){
			org.setAreaName(area.getName());
		}
		model.addAttribute("entity", org);
		model.addAttribute("action", "create");
		return getCreateFormPage();
	}

	/**
	 * 新增操作<br>
	 * url:sys/org/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Org entity,
			RedirectAttributes redirectAttributes) {
		orgService.saveOrg(entity);
		addMessage(redirectAttributes, "保存机构'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 跳转更新页面<br>
	 * URL:sys/org/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		Org org = orgService.selectById(id);
		if (org != null) {
			// 设置父机构名称
			Org parent = orgService.selectById(org.getParentId());
			if (parent != null) {
				org.setParentName(parent.getName());
			}
			// 设置归属区域名称
			Area area = areaService.selectById(org.getAreaId());
			if(area != null){
				org.setAreaName(area.getName());
			}
		}
		model.addAttribute("entity", org);
		model.addAttribute("action", "update");
		return getUpdateFormPage();
	}
	
	/**
	 * 更新操作<br>
	 * URL:sys/org/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Org entity, RedirectAttributes redirectAttributes) {
		orgService.updateOrg(entity);
		addMessage(redirectAttributes, "更新机构'" + entity.getName() + "'成功");
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 删除操作<br>
	 * URL:sys/org/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		orgService.deleteOrg(id);
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
		List<Org> list = orgService.findOrgOrderByCode();
		for (Org e : list) {
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

}
