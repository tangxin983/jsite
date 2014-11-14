package tangx.jsite.site.modules.sys.controller;

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

import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.web.controller.BaseController;
import tangx.jsite.site.modules.sys.entity.Dict;
import tangx.jsite.site.modules.sys.service.DictService;
import tangx.jsite.site.utils.Constant;
import tangx.jsite.site.utils.Servlets;

import com.google.common.collect.Maps;

/**
 * 字典Controller
 * @author tangx
 * @version 2014-06-25
 */
@Controller
@RequestMapping(value = "sys/dict")
public class DictController extends BaseController<Dict> {

	private DictService dictService;

	@Autowired
	public void setDictService(DictService dictService) {
		super.setService(dictService);
		this.dictService = dictService;
	}
	
	/**
	 * 跳转列表页（分页）<br>
	 * url:sys/dict
	 */
	@RequestMapping
	public String paginationList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		model.addAttribute("typeList", dictService.findTypeList());
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		// 按type排序
		Map<String, String> orders = Maps.newHashMap();
		orders.put("type", "asc");
		Page<Dict> entitys = service.selectByPage(searchParams, orders, pageNumber, pageSize);
		model.addAttribute("page", entitys);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "s_"));
		return getListPage();
	}
	
	/**
	 * 跳转新增页面<br>
	 * url:sys/dict/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		return super.createForm(model);
	}

	/**
	 * 新增操作<br>
	 * url:sys/dict/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Dict entity, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 跳转更新页面<br>
	 * URL:sys/dict/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新操作<br>
	 * URL:sys/dict/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Dict entity, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 删除操作<br>
	 * URL:sys/dict/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}
	
	/**
	 * 批量删除操作<br>
	 * URL:sys/dict/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}
	
	/**
	 * 根据id查找实体（json）<br>
	 * URL:sys/dict/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public Dict get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
