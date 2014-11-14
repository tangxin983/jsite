package tangx.jsite.site.modules.cms.controller;

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
import tangx.jsite.site.modules.cms.entity.Category;
import tangx.jsite.site.modules.cms.service.CategoryService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * cms栏目Controller
 * 
 * @author tangx
 * @since 2014-09-10
 */
@Controller
@RequestMapping(value = "cms/category")
public class CategoryController extends BaseController<Category> {

	private CategoryService categoryService;

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		super.setService(categoryService);
		this.categoryService = categoryService;
	}

	/**
	 * 跳转列表页
	 * <p>
	 * url:cms/category
	 */
	@RequestMapping
	public String view(Model model) {
		model.addAttribute("entitys", categoryService.getCategoryForTreeTable(null));
		return getListPage();
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
		List<Category> list = categoryService.findCategoryBySort(null);
		for (Category e : list) {
			// 排除extId及其子菜单
			if (extId == null
					|| (extId != null && !extId.equals(e.getId()) && e
							.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				map.put("type", e.getModule());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 跳转新增页面
	 * <p>
	 * url:cms/category/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Category category, Model model) {
		// 如果没有传入父栏目id，则默认父栏目是顶级栏目
		if (category == null) {
			category = new Category();
		}
		if (StringUtils.isBlank(category.getParentId())) {
			category.setParentId("1");
		}
		// 设置父栏目名称
		Category parent = categoryService.selectById(category.getParentId());
		if (parent != null) {
			category.setParentName(parent.getName());
		}
		model.addAttribute("entity", category);
		model.addAttribute("action", "create");
		return getCreateFormPage();
	}

	/**
	 * 新增操作
	 * <p>
	 * url:cms/category/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Category entity, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}

	/**
	 * 跳转更新页面
	 * <p>
	 * URL:cms/category/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		Category category = categoryService.selectById(id);
		if (category != null) {
			// 设置父栏目名称
			Category parent = categoryService
					.selectById(category.getParentId());
			if (parent != null) {
				category.setParentName(parent.getName());
			}
		}
		model.addAttribute("entity", category);
		model.addAttribute("action", "update");
		return getUpdateFormPage();
	}

	/**
	 * 更新操作
	 * <p>
	 * URL:cms/category/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") Category entity,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}

	/**
	 * 删除操作
	 * <p>
	 * URL:cms/category/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id,
			RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}

	/**
	 * 批量删除操作
	 * <p>
	 * URL:cms/category/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids") List<String> ids,
			RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}

	/**
	 * 根据id查找实体（json）
	 * <p>
	 * URL:cms/category/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public Category get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
