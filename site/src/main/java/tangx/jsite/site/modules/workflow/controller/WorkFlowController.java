package tangx.jsite.site.modules.workflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tangx.jsite.site.core.exception.ServiceException;
import tangx.jsite.site.modules.workflow.service.WorkFlowService;
import tangx.jsite.site.utils.Constant;
import tangx.jsite.site.utils.Servlets;
import tangx.jsite.site.utils.SysUtil;

/**
 * 工作流Controller
 * 
 * @author tangx
 * @since 2014-06-12
 */
@Controller
@RequestMapping(value = "workflow")
public class WorkFlowController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	/**
	 * 流程定义列表
	 * 
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            每页记录数
	 * @param model
	 * @return
	 */
	@RequestMapping("process/list")
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model) {
		model.addAttribute("page", workFlowService
				.getPaginationProcessDefinition(pageNumber, pageSize));
		return "modules/workflow/processList";
	}

	/**
	 * 跳转部署页面
	 * 
	 * @return
	 */
	@RequestMapping("process/deployForm")
	public String deployForm() {
		return "modules/workflow/deploy";
	}

	/**
	 * 部署流程
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping("process/deploy")
	public String deploy(
			@RequestParam(value = "file", required = false) MultipartFile file,
			RedirectAttributes redirectAttributes) {
		String fileName = file.getOriginalFilename();
		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment()
						.addZipInputStream(zip).deploy();
			} else {
				deployment = repositoryService.createDeployment()
						.addInputStream(fileName, fileInputStream).deploy();
			}
			List<ProcessDefinition> list = repositoryService
					.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).list();
			if (list.isEmpty()) {
				logger.error("deploy process error");
				redirectAttributes.addFlashAttribute("message", "部署失败");
			} else {
				redirectAttributes.addFlashAttribute("message", "部署成功");
			}
		} catch (Exception e) {
			logger.error("deploy process error:" + e.getMessage());
			redirectAttributes.addFlashAttribute("message",
					"部署失败:" + e.getMessage());
		}
		return "redirect:/workflow/process/list";
	}

	/**
	 * 读取部署资源(xml和png图片)
	 * 
	 * @param processDefinitionId
	 *            流程定义
	 * @param resourceType
	 *            资源类型(xml|image)
	 * @throws Exception
	 */
	@RequestMapping(value = "resource/read")
	public void readResource(
			@RequestParam("processId") String processDefinitionId,
			@RequestParam("type") String resourceType,
			HttpServletResponse response) {
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(), resourceName);
		byte[] b = new byte[1024];
		int len = -1;
		try {
			while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 挂起流程定义（挂起后不能再发起此流程定义的实例；所有流程实例也将挂起无法继续执行）
	 */
	@RequestMapping("process/suspend/{processId}")
	public String suspendProcess(
			@PathVariable("processId") String processDefinitionId,
			RedirectAttributes redirectAttributes) {
		repositoryService.suspendProcessDefinitionById(processDefinitionId,
				true, null);
		redirectAttributes.addFlashAttribute("message", "已挂起ID为["
				+ processDefinitionId + "]的流程定义。");
		return "redirect:/workflow/process/list";
	}

	/**
	 * 激活流程定义（同时激活所有流程实例）
	 */
	@RequestMapping("process/active/{processId}")
	public String activeProcess(
			@PathVariable("processId") String processDefinitionId,
			RedirectAttributes redirectAttributes) {
		repositoryService.activateProcessDefinitionById(processDefinitionId,
				true, null);
		redirectAttributes.addFlashAttribute("message", "已激活ID为["
				+ processDefinitionId + "]的流程定义。");
		return "redirect:/workflow/process/list";
	}

	/**
	 * 删除部署的流程，级联删除流程实例与历史数据
	 * 
	 * @param deploymentId
	 *            流程部署ID
	 */
	@RequestMapping("process/delete/{deploymentId}")
	public String delete(@PathVariable("deploymentId") String deploymentId,
			RedirectAttributes redirectAttributes) {
		repositoryService.deleteDeployment(deploymentId, true);
		redirectAttributes.addFlashAttribute("message", "已删除部署ID为"
				+ deploymentId + "的流程。");
		return "redirect:/workflow/process/list";
	}

	/**
	 * 运行中的流程实例列表
	 * 
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            每页记录数
	 * @param model
	 * @return
	 */
	@RequestMapping("instance/running")
	public String running(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		model.addAttribute("processDefinitionList", workFlowService.getProcessDefinition());
		model.addAttribute("page", workFlowService
				.getPaginationRunningInstance(searchParams, pageNumber, pageSize));
		return "modules/workflow/runningInstance";
	}

	/**
	 * 已结束的流程实例列表
	 * 
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            每页记录数
	 * @param model
	 * @return
	 */
	@RequestMapping("instance/finished")
	public String finished(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		model.addAttribute("processDefinitionList", workFlowService.getProcessDefinition());
		model.addAttribute("page", workFlowService
				.getPaginationFinishedInstance(searchParams, pageNumber, pageSize));
		return "modules/workflow/finishedInstance";
	}

	/**
	 * 激活流程实例
	 * 
	 * @param processInstanceId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("instance/active/{processInstanceId}")
	public String activeInstance(
			@PathVariable("processInstanceId") String processInstanceId,
			RedirectAttributes redirectAttributes) {
		runtimeService.activateProcessInstanceById(processInstanceId);
		redirectAttributes.addFlashAttribute("message", "已激活ID为["
				+ processInstanceId + "]的流程实例。");
		return "redirect:/workflow/instance/running";
	}

	/**
	 * 挂起流程实例
	 * 
	 * @param processInstanceId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("instance/suspend/{processInstanceId}")
	public String suspendInstance(
			@PathVariable("processInstanceId") String processInstanceId,
			RedirectAttributes redirectAttributes) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		redirectAttributes.addFlashAttribute("message", "已挂起ID为["
				+ processInstanceId + "]的流程实例。");
		return "redirect:/workflow/instance/running";
	}

	/**
	 * 待办任务列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("task/todo")
	public String todo(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		model.addAttribute("processDefinitionList", workFlowService.getProcessDefinition());
		model.addAttribute("page",
				workFlowService.getPaginationTodoTask(searchParams, pageNumber, pageSize));
		return "modules/workflow/todoTask";
	}

	/**
	 * 已办任务列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("task/done")
	public String done(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "size", defaultValue = Constant.PAGINATION_SIZE) int pageSize,
			Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "s_");
		model.addAttribute("processDefinitionList", workFlowService.getProcessDefinition());
		model.addAttribute("page",
				workFlowService.getPaginationDoneTask(searchParams, pageNumber, pageSize));
		return "modules/workflow/doneTask";
	}

	/**
	 * 签收任务
	 * 
	 * @param taskId
	 *            任务ID
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("task/claim/{id}")
	public String claim(@PathVariable("id") String taskId,
			RedirectAttributes redirectAttributes) {
		taskService.claim(taskId, SysUtil.getCurrentUserId());
		redirectAttributes.addFlashAttribute("message", "任务已签收");
		return "redirect:/workflow/task/todo";
	}

}
