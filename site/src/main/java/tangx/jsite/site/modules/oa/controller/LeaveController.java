package tangx.jsite.site.modules.oa.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.activiti.engine.runtime.ProcessInstance;
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
import tangx.jsite.site.modules.oa.entity.Leave;
import tangx.jsite.site.modules.oa.service.LeaveService;
import tangx.jsite.site.utils.Constant;
import tangx.jsite.site.utils.Servlets;

/**
 * 请假Controller
 * @author tangx
 * @since 2014-05-28
 */
@Controller
@RequestMapping(value = "oa/leave")
public class LeaveController extends BaseController<Leave> {

	private LeaveService leaveService;

	@Autowired
	public void setLeaveService(LeaveService leaveService) {
		super.setService(leaveService);
		this.leaveService = leaveService;
	}
	
	/**
	 * 单据列表
	 */
	@RequestMapping
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		Page<Leave> entitys = leaveService.findLeaveInstanceByPage(searchParams, pageNumber, pageSize, false);
		model.addAttribute("page", entitys);
		// 将搜索条件编码成字符串，用于分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "s_"));
		return getListPage();
	}
    
    /**
     * 流程办理页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "handle/{id}", method = RequestMethod.GET)
	public String handle(@PathVariable("id") String id, Model model) {
    	model.addAttribute("entity", leaveService.getLeaveDetail(id));
		return "modules/oa/leaveDetail";
	}
    
    /**
     * 流程详情页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") String id, Model model) {
    	model.addAttribute("entity", leaveService.getLeaveDetail(id));
    	model.addAttribute("isView", true);
		return "modules/oa/leaveDetail";
	}
    
    /**
     * 完成任务
     * @param leave
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "completeTask", method = RequestMethod.POST)
	public String completeTask(@Valid @ModelAttribute("entity")Leave leave, RedirectAttributes redirectAttributes) {
		leaveService.completeTask(leave);
		addMessage(redirectAttributes, "办理成功");
		return "redirect:/workflow/task/todo";
	}
    
	
	/**
	 * 跳转新增页面<br>
	 * url:oa/leave/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		return super.createForm(model);
	}

	/**
	 * 新增操作<br>
	 * url:oa/leave/create
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Leave entity,
			RedirectAttributes redirectAttributes) {
		ProcessInstance processInstance = leaveService.saveLeave(entity);
		addMessage(redirectAttributes, "流程已启动，流程ID：" + processInstance.getId());
		return "redirect:/" + getControllerContext();
	}
	
	/**
	 * 跳转更新页面<br>
	 * URL:oa/leave/update/{id}
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		return super.updateForm(id, model);
	}
	
	/**
	 * 更新操作<br>
	 * URL:oa/leave/update
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity")Leave entity, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes) {
		return super.update(entity, result, model, redirectAttributes);
	}
	
	/**
	 * 删除操作<br>
	 * URL:oa/leave/delete/{id}
	 */
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}
	
	/**
	 * 批量删除操作<br>
	 * URL:oa/leave/delete
	 */
	@RequestMapping("delete")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		return super.delete(ids, redirectAttributes);
	}
	
	/**
	 * 根据id查找实体（json）<br>
	 * URL:oa/leave/get/{id}
	 */
	@RequestMapping("get/{id}")
	@ResponseBody
	public Leave get(@PathVariable("id") String id) {
		return super.get(id);
	}

}
