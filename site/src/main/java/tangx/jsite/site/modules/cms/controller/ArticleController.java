package tangx.jsite.site.modules.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import tangx.jsite.site.modules.cms.entity.Article;
import tangx.jsite.site.modules.cms.entity.Category;
import tangx.jsite.site.modules.cms.service.ArticleService;
import tangx.jsite.site.modules.cms.service.CategoryService;
import tangx.jsite.site.utils.Constant;

/**
 * 文章Controller
 * @author tangx
 * @since 2014-09-22
 */
@Controller
@RequestMapping(value = "cms/article")
public class ArticleController extends BaseController<Article> {

	private ArticleService articleService;

	@Autowired
	public void setArticleService(ArticleService articleService) {
		super.setService(articleService);
		this.articleService = articleService;
	}
	
	@Autowired
	private CategoryService categoryService;
	
	// ========== 以下为简单crud示例。注意：一旦修改url，对应生成的视图url也需手动修改 ===========
	/**
	 * 跳转列表页（分页）<p>
	 * url:cms/article
	 */
	@RequestMapping
	public String paginationList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		String categoryId = StringUtils.defaultString(request.getParameter("s_categoryId"));
		Category category = new Category();
		if(categoryId.equals("")) {
			category.setId("1");
			category.setName("所有栏目");
		}else{
			category = categoryService.selectById(categoryId);
		}
		model.addAttribute("category", category);
		return super.paginationList(pageNumber, pageSize, model, request);
	}
	
	/**
	 * 跳转新增页面<p>
	 * url:cms/article/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		return super.createForm(model);
	}

	/**
	 * 新增操作<p>
	 * url:cms/article/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Article entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 跳转更新页面<p>
	 * URL:cms/article/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新操作<p>
	 * URL:cms/article/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Article entity, BindingResult result, 
		Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 删除操作<p>
	 * URL:cms/article/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}
	
	/**
	 * 批量删除操作<p>
	 * URL:cms/article/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}
	
	/**
	 * 根据id查找实体（json）<p>
	 * URL:cms/article/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public Article get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
