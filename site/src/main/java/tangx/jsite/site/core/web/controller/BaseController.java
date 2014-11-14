package tangx.jsite.site.core.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tangx.jsite.site.core.persistence.entity.BaseEntity;
import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.utils.Constant;
import tangx.jsite.site.utils.Servlets;

import com.google.common.collect.Lists;

/**
 * 提供基础的crud控制处理
 * 
 * @author tangx
 *
 * @param <T>
 */
public abstract class BaseController<T extends BaseEntity> implements ServletContextAware {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected BaseService<T> service;

	public void setService(BaseService<T> service) {
		this.service = service;
	}

	protected ServletContext servletContext;

	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	/**
	 * 供子类设置额外的查询参数
	 * 
	 * @param searchParams
	 */
	// protected void setExtraSearchParam(Map<String, Object> searchParams){
	//
	// }

	/**
	 * 通过反射取得controller的上下文（头尾的'/'会被去掉）
	 * 
	 * @return 上下文字符串
	 */
	protected String getControllerContext() {
		String context = "";
		Class<?> c = this.getClass();
		if (c != null) {
			RequestMapping mapping = c.getAnnotation(RequestMapping.class);
			String[] mappingValues = mapping.value();
			context = mappingValues[0];
			if (context.startsWith("/")) {
				context = context.substring(1);
			}
			if (context.endsWith("/")) {
				context = context.substring(0, context.length() - 1);
			}
		}
		return context;
	}

	/**
	 * 列表页<p>
	 * 默认为"modules/" + ControllerContext + "List"。比如/a/b对应modules/a/bList
	 * 
	 * @return
	 */
	protected String getListPage() {
		return "modules/" + getControllerContext() + "List";
	}

	/**
	 * 新增表单页<p>
	 * 默认为"modules/" + ControllerContext + "Form"。比如/a/b对应modules/a/bForm
	 * 
	 * @return
	 */
	protected String getCreateFormPage() {
		return "modules/" + getControllerContext() + "Form";
	}

	/**
	 * 更新表单页<p>
	 * 默认为"modules/" + ControllerContext + "Form"。比如/a/b对应modules/a/bForm
	 * 
	 * @return
	 */
	protected String getUpdateFormPage() {
		return "modules/" + getControllerContext() + "Form";
	}
	
	/**
	 * 返回列表页面（配合datatable使用）
	 * @return
	 */
	public String list() {
		return getListPage();
	}
	
	/**
	 * 获取数据(datatable物理分页)
	 * @param request
	 * @return
	 */
	public Page<T> datagrid(ServletRequest request) {
		// 获取dataTable参数
		int draw = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("draw"), "1"));
		int start = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("start"), "0"));
		int length = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("length"), Constant.PAGINATION_SIZE));
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		// clone参数，避免在selectByPage中设置的额外参数暴露在url中
		Page<T> page = service.datagrid(ObjectUtils.clone(searchParams), null, start, length, draw);
		return page;
	}
	
	/**
	 * 获取全部数据(datatable普通分页)
	 * @param request
	 * @return
	 */
	public Page<T> alldatagrid(ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		List<T> entitys = service.select(searchParams);
		Page<T> page = new Page<T>();
		page.setData(entitys);
		return page;
	}

	/**
	 * 分页展示(普通表格物理分页)
	 * 
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            每页记录数
	 * @param model
	 * @param request
	 * @return
	 */
	public String paginationList(int pageNumber, int pageSize, Model model,
			ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		// clone参数，避免在selectByPage中设置的额外参数暴露在url中
		Page<T> entitys = service.selectByPage(ObjectUtils.clone(searchParams), null, pageNumber, pageSize);
		model.addAttribute("page", entitys);
		// 将搜索条件编码成字符串，用于分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "s_"));
		return getListPage();
	}

	/**
	 * 展示所有记录(不分页的普通表格)
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	public String list(Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		List<T> entitys = service.select(searchParams);
		model.addAttribute("entitys", entitys);
		return getListPage();
	}

	/**
	 * 跳转新增页面
	 * 
	 * @param model
	 * @return 新增页面
	 */
	public String createForm(Model model) {
		model.addAttribute("action", "create");
		return getCreateFormPage();
	}

	/**
	 * 新增操作
	 * 
	 * @param entity
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	public String create(T entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return createForm(model);
		}
		service.insert(entity);
		addMessage(redirectAttributes, "添加成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 跳转更新页面
	 * 
	 * @param id
	 * @param model
	 * @return 更新页面
	 */
	public String updateForm(String id, Model model) {
		model.addAttribute("entity", service.selectById(id));
		model.addAttribute("action", "update");
		return getUpdateFormPage();
	}

	/**
	 * 更新操作
	 * 
	 * @param entity
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	public String update(T entity, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			addMessage(model, getFieldErrorMessage(result));
			return updateForm(entity.getId(), model);
		}
		service.update(entity);
		addMessage(redirectAttributes, "更新成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 删除操作
	 * 
	 * @param id
	 * @param redirectAttributes
	 * @return redirect到列表页
	 */
	public String delete(String id, RedirectAttributes redirectAttributes) {
		service.delete(id);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 批量删除操作
	 * 
	 * @param ids
	 * @param redirectAttributes
	 * @return redirect到列表页
	 */
	public String delete(List<String> ids,
			RedirectAttributes redirectAttributes) {
		service.delete(ids);
		addMessage(redirectAttributes, "删除" + ids.size() + "条记录 成功");
		return "redirect:/" + getControllerContext();
	}

	/**
	 * 根据id查找实体并返回json
	 * 
	 * @param id
	 * @return json对象
	 */
	public T get(String id) {
		return service.selectById(id);
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(RedirectAttributes redirectAttributes,
			String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	/**
	 * 添加Model消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 获取服务端参数验证错误信息
	 * 
	 * @param result
	 */
	protected String[] getFieldErrorMessage(BindingResult result) {
		List<String> message = Lists.newArrayList();
		for (FieldError error : result.getFieldErrors()) {
			message.add(error.getDefaultMessage());
		}
		message.add(0, "数据校验失败：");
		return message.toArray(new String[] {});
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果
	 * 先根据form的id从数据库查出对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 * 
	 * @param id
	 *            主键
	 * @param model
	 */
	@ModelAttribute
	public void bindingEntity(@RequestParam(value = "id", defaultValue = "-1") String id,
			Model model) {
		if (!id.equals("-1")) {
			model.addAttribute("entity", service.selectById(id));
		}
		model.addAttribute("module", getControllerContext());
		model.addAttribute("ctxModule", servletContext.getContextPath() + "/" + getControllerContext());
	}
}
