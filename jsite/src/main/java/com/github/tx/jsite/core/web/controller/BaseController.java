package com.github.tx.jsite.core.web.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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

import com.github.tx.jsite.core.persistence.entity.BaseEntity;
import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.service.BaseService;
import com.google.common.collect.Lists;

/**
 * 提供基础的crud控制处理
 * 
 * @author tangx
 * @since 2014年10月22日
 *
 * @param <T>
 */
public abstract class BaseController<T extends BaseEntity> implements
		ServletContextAware {

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
	 * 列表页,默认为"modules/" + ControllerContext + "List"。比如/a/b对应modules/a/bList
	 * @return
	 */
	protected String getListPage() {
		return "modules/" + getControllerContext() + "List";
	}

	/**
	 * 新增表单页,默认为"modules/" + ControllerContext + "Form"。比如/a/b对应modules/a/bForm
	 * @return
	 */
	protected String getCreateFormPage() {
		return "modules/" + getControllerContext() + "Form";
	}

	/**
	 * 更新表单页,默认为"modules/" + ControllerContext + "Form"。比如/a/b对应modules/a/bForm
	 * @return
	 */
	protected String getUpdateFormPage() {
		return "modules/" + getControllerContext() + "Form";
	}

	/**
	 * 返回列表页面（配合datatable使用）
	 * 
	 * @return
	 */
	public String list(Model model) {
		return getListPage();
	}

	/**
	 * 获取数据(datatable物理分页)
	 * 
	 * @param request
	 * @return
	 */
	public DataTableEntity<T> datagrid(HttpServletRequest request) {
		DataTableEntity<T> page = service.selectByPage(request);
		return page;
	}

	/**
	 * 获取全部数据(datatable普通分页)
	 * 
	 * @param request
	 * @return
	 */
	public DataTableEntity<T> alldatagrid(HttpServletRequest request) {
		DataTableEntity<T> entity = new DataTableEntity<T>();
		entity.setData(service.select(request));
		return entity;
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
		service.updateById(entity);
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
		service.deleteById(id);
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
	public String delete(List<String> ids, RedirectAttributes redirectAttributes) {
		service.deleteByIds(ids);
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
	public void bindingEntity(
			@RequestParam(value = "id", defaultValue = "-1") String id,
			Model model) {
		if (!id.equals("-1")) {
			model.addAttribute("entity", service.selectById(id));
		}
		model.addAttribute("module", getControllerContext());
		model.addAttribute("ctxModule", servletContext.getContextPath() + "/"
				+ getControllerContext());
	}
}
