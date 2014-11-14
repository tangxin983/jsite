package ${packageName}.${moduleName}.controller${subModuleName};

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
import com.github.tx.jsite.core.web.controller.BaseController;
import com.github.tx.jsite.core.persistence.entity.PageEntity;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.service${subModuleName}.${ClassName}Service;

/**
 * ${functionName}Controller
 * @author ${classAuthor}
 * @since ${classVersion}
 */
@Controller
@RequestMapping(value = "${urlPrefix}")
public class ${ClassName}Controller extends BaseController<${ClassName}> {

	private ${ClassName}Service ${className}Service;

	@Autowired
	public void set${ClassName}Service(${ClassName}Service ${className}Service) {
		super.setService(${className}Service);
		this.${className}Service = ${className}Service;
	}
	
	// ========== 以下为简单crud示例。注意：一旦修改url，对应生成的视图url也需手动修改 ===========
	/**
	 * 展示列表页<p>
	 * url:${urlPrefix}
	 */
	@RequestMapping
	public String list(Model model) {
		return super.list(model);
	}
	
	/**
	 * 获取数据<p>
	 * url:${urlPrefix}?datagrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public PageEntity<${ClassName}> datagrid(HttpServletRequest request) {
		<#if isPagination>
		return super.datagrid(request);
		<#else>
		return super.alldatagrid(request);
		</#if>
	}
	
	/**
	 * 跳转新增页面<p>
	 * url:${urlPrefix}/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		return super.createForm(model);
	}

	/**
	 * 新增操作<p>
	 * url:${urlPrefix}/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid ${ClassName} entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		return super.create(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 跳转更新页面<p>
	 * URL:${urlPrefix}/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新操作<p>
	 * URL:${urlPrefix}/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")${ClassName} entity, BindingResult result, 
		Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 删除操作<p>
	 * URL:${urlPrefix}/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}
	
	/**
	 * 批量删除操作<p>
	 * URL:${urlPrefix}/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}
	
	/**
	 * 根据id查找实体（json）<p>
	 * URL:${urlPrefix}/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public ${ClassName} get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
