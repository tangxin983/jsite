package com.github.tx.jsite.modules.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.controller.BaseController;
import com.github.tx.jsite.modules.cms.entity.Comment;
import com.github.tx.jsite.modules.cms.service.CommentService;

/**
 * 评论Controller
 * @author tangx
 * @since 2014-11-06
 */
@Controller
@RequestMapping(value = "cms/comment")
public class CommentController extends BaseController<Comment> {

	private CommentService commentService;

	@Autowired
	public void setCommentService(CommentService commentService) {
		super.setService(commentService);
		this.commentService = commentService;
	}
	
	// ========== 以下为简单crud示例。注意：一旦修改url，对应生成的视图url也需手动修改 ===========
	/**
	 * 展示列表页<p>
	 * url:cms/comment
	 */
	@RequestMapping
	public String list(Model model) {
		return super.list(model);
	}
	
	/**
	 * 获取数据<p>
	 * url:cms/comment?datagrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public DataTableEntity<Comment> datagrid(HttpServletRequest request) {
		return super.datagrid(request);
	}
	
	/**
	 * 跳转新增页面<p>
	 * url:cms/comment/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		return super.createForm(model);
	}

	/**
	 * 新增操作<p>
	 * url:cms/comment/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Comment entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 跳转更新页面<p>
	 * URL:cms/comment/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新操作<p>
	 * URL:cms/comment/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Comment entity, BindingResult result, 
		Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 删除操作<p>
	 * URL:cms/comment/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}
	
	/**
	 * 批量删除操作<p>
	 * URL:cms/comment/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}
	
	/**
	 * 根据id查找实体（json）<p>
	 * URL:cms/comment/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public Comment get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
