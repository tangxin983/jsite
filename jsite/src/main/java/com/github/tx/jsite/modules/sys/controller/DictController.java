package com.github.tx.jsite.modules.sys.controller;

import java.util.List;

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
import com.github.tx.jsite.modules.sys.entity.Dict;
import com.github.tx.jsite.modules.sys.service.DictService;

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
	 * 展示列表页<p>
	 * url:sys/dict
	 */
	@RequestMapping
	public String list(Model model) {
		model.addAttribute("typeList", dictService.findTypeList());
		return super.list(model);
	}
	
	/**
	 * 获取数据<p>
	 * url:sys/dict?datagrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public DataTableEntity<Dict> datagrid(HttpServletRequest request) {
		return super.datagrid(request);
	}
	
	/**
	 * 跳转新增页面<p>
	 * url:sys/dict/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		return super.createForm(model);
	}

	/**
	 * 新增操作<p>
	 * url:sys/dict/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Dict entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 跳转更新页面<p>
	 * URL:sys/dict/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新操作<p>
	 * URL:sys/dict/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Dict entity, BindingResult result, 
		Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 删除操作<p>
	 * URL:sys/dict/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}
	
	/**
	 * 批量删除操作<p>
	 * URL:sys/dict/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}
	
	/**
	 * 根据id查找实体（json）<p>
	 * URL:sys/dict/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public Dict get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
