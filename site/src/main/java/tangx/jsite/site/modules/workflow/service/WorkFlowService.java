package tangx.jsite.site.modules.workflow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.exception.ServiceException;
import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.persistence.entity.WorkFlowEntity;
import tangx.jsite.site.utils.SysUtil;

import com.github.tx.common.util.JodaTimeUtil;
import com.google.common.collect.Lists;

/**
 * 工作流Service
 * 
 * @author tangx
 * @since 2014-06-12
 */
@Service
@Transactional
public class WorkFlowService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ManagementService managementService;

	/**
	 * 分页获取流程定义列表
	 * 
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	public Page<Object[]> getPaginationProcessDefinition(int pageNumber,
			int pageSize) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService
				.createProcessDefinitionQuery().orderByDeploymentId().desc();
		// 设置page对象
		Page<Object[]> page = new Page<Object[]>();
		page.setCurrentPage(pageNumber);
		page.setSize(pageSize);
		page.setRecordsTotal((int) processDefinitionQuery.count());
		// 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		List<Object[]> objects = Lists.newArrayList();
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery
				.listPage(page.getCurrentResult(), pageSize);
		for (ProcessDefinition processDefinition : processDefinitionList) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery()
					.deploymentId(deploymentId).singleResult();
			objects.add(new Object[] { processDefinition, deployment });
		}
		page.setData(objects);
		return page;
	}

	/**
	 * 分页获取正在运行的流程实例
	 * 
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	public Page<Object[]> getPaginationRunningInstance(
			Map<String, Object> param, int pageNumber, int pageSize) {
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("processDefinitionId")))) {
			query = query.processDefinitionId(ObjectUtils.toString(param.get("processDefinitionId")));
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("businessKey")))) {
			query = query.processInstanceBusinessKey(ObjectUtils.toString(param.get("businessKey")));
		}
		query = query.orderByProcessInstanceId().desc();
		// 设置page对象
		Page<Object[]> page = new Page<Object[]>();
		page.setCurrentPage(pageNumber);
		page.setSize(pageSize);
		page.setRecordsTotal((int) query.count());
		// 保存ProcessInstance,ProcessDefinition,Task,HistoricProcessInstance(用于获取startUserId)
		List<Object[]> objects = Lists.newArrayList();
		List<ProcessInstance> instances = query.listPage(
				page.getCurrentResult(), pageSize);
		for (ProcessInstance instance : instances) {
			Task task = getCurrentTask(instance.getId());
			ProcessDefinition processDefinition = getProcessDefinition(instance
					.getProcessDefinitionId());
			HistoricProcessInstance hisInstance = getHistoricProcessInstance(instance
					.getId());
			objects.add(new Object[] { processDefinition, instance,
					hisInstance, task });
		}
		page.setData(objects);
		return page;
	}

	/**
	 * 分页获取已结束的流程实例
	 * 
	 * @param pageNumber
	 *            当前页
	 * @param pageSize
	 *            每页数量
	 * @return
	 */
	public Page<Object[]> getPaginationFinishedInstance(
			Map<String, Object> param, int pageNumber, int pageSize) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished();
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("processDefinitionId")))) {
			query = query.processDefinitionId(ObjectUtils.toString(param.get("processDefinitionId")));
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("businessKey")))) {
			query = query.processInstanceBusinessKey(ObjectUtils.toString(param.get("businessKey")));
		}
		query = query.orderByProcessInstanceEndTime().desc();
		// 设置page对象
		Page<Object[]> page = new Page<Object[]>();
		page.setCurrentPage(pageNumber);
		page.setSize(pageSize);
		page.setRecordsTotal((int) query.count());
		// 保存HistoricProcessInstance,ProcessDefinition
		List<Object[]> objects = Lists.newArrayList();
		List<HistoricProcessInstance> instances = query.listPage(
				page.getCurrentResult(), pageSize);
		for (HistoricProcessInstance instance : instances) {
			ProcessDefinition processDefinition = getProcessDefinition(instance
					.getProcessDefinitionId());
			objects.add(new Object[] { processDefinition, instance });
		}
		page.setData(objects);
		return page;
	}

	/**
	 * 分页获取当前用户的待办任务
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<WorkFlowEntity> getPaginationTodoTask(
			Map<String, Object> param, int pageNumber, int pageSize) {
		TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(SysUtil.getCurrentUserId()).active();
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("processDefinitionId")))) {
			query = query.processDefinitionId(ObjectUtils.toString(param.get("processDefinitionId")));
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("businessKey")))) {
			query = query.processInstanceBusinessKey(ObjectUtils.toString(param.get("businessKey")));
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("taskCreateTime1")))) {
			query = query.taskCreatedAfter(JodaTimeUtil.convertFromString(ObjectUtils.toString(param.get("taskCreateTime1"))));
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("taskCreateTime2")))) {
			query = query.taskCreatedBefore(JodaTimeUtil.convertFromString(ObjectUtils.toString(param.get("taskCreateTime2"))));
		}
		query = query.orderByTaskCreateTime().desc();
		// 设置page对象
		Page<WorkFlowEntity> page = new Page<WorkFlowEntity>();
		page.setCurrentPage(pageNumber);
		page.setSize(pageSize);
		page.setRecordsTotal((int) query.count());
		List<WorkFlowEntity> result = Lists.newArrayList();
		List<Task> tasks = query.listPage(page.getCurrentResult(), pageSize);
		for (Task task : tasks) {
			WorkFlowEntity entity = new WorkFlowEntity();
			entity.setTask(task);
			entity.setProcessInstanceId(task.getProcessInstanceId());
			setWorkFlowEntity(entity);
			result.add(entity);
		}
		page.setData(result);
		return page;
	}

	/**
	 * 分页获取当前用户的已办任务
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<WorkFlowEntity> getPaginationDoneTask(
			Map<String, Object> param, int pageNumber, int pageSize) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().finished()
				.taskAssignee(SysUtil.getCurrentUserId());
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("processDefinitionId")))) {
			query = query.processDefinitionId(ObjectUtils.toString(param.get("processDefinitionId")));
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(param.get("businessKey")))) {
			query = query.processInstanceBusinessKey(ObjectUtils.toString(param.get("businessKey")));
		}
		query = query.orderByHistoricTaskInstanceEndTime().desc();
		// 设置page对象
		Page<WorkFlowEntity> page = new Page<WorkFlowEntity>();
		page.setCurrentPage(pageNumber);
		page.setSize(pageSize);
		page.setRecordsTotal((int) query.count());
		List<WorkFlowEntity> result = Lists.newArrayList();
		List<HistoricTaskInstance> tasks = query.listPage(
				page.getCurrentResult(), pageSize);
		for (HistoricTaskInstance historicTaskInstance : tasks) {
			WorkFlowEntity entity = new WorkFlowEntity();
			entity.setHistoricTaskInstance(historicTaskInstance);
			entity.setProcessInstanceId(historicTaskInstance
					.getProcessInstanceId());
			setWorkFlowEntity(entity);
			result.add(entity);
		}
		page.setData(result);
		return page;
	}

	/**
	 * 获取某个业务流程的历史流程实例
	 * 
	 * @param processDefinitionKey
	 *            流程定义key
	 * @return
	 */
	public List<HistoricProcessInstance> getHistoricInstanceList(
			String processDefinitionKey) {
		HistoricProcessInstanceQuery query = historyService
				.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.orderByProcessInstanceId().desc();
		return query.list();
	}

	/**
	 * 获取某个业务流程的历史流程实例
	 * 
	 * @param processDefinitionKey
	 *            流程定义key
	 * @param userId
	 *            发起人ID
	 * @return
	 */
	public List<HistoricProcessInstance> getHistoricInstanceList(
			String processDefinitionKey, String userId) {
		HistoricProcessInstanceQuery query = historyService
				.createHistoricProcessInstanceQuery().startedBy(userId)
				.processDefinitionKey(processDefinitionKey)
				.orderByProcessInstanceId().desc();
		return query.list();
	}

	/**
	 * 获取某个业务流程的流程实例
	 * 
	 * @param processDefinitionKey
	 *            流程定义key
	 * @return
	 */
	public List<ProcessInstance> getInstanceList(String processDefinitionKey) {
		ProcessInstanceQuery processInstanceQuery = runtimeService
				.createProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey)
				.orderByProcessInstanceId().desc();
		return processInstanceQuery.list();
	}

	/**
	 * 获取某个业务流程的流程实例
	 * 
	 * @param processDefinitionKey
	 *            流程定义key
	 * @param isActive
	 *            true代表只获取激活的实例；false代表只获取挂起的实例
	 * @return
	 */
	public List<ProcessInstance> getInstanceList(String processDefinitionKey,
			boolean isActive) {
		ProcessInstanceQuery processInstanceQuery = null;
		if (isActive) {
			processInstanceQuery = runtimeService.createProcessInstanceQuery()
					.processDefinitionKey(processDefinitionKey).active()
					.orderByProcessInstanceId().desc();
		} else {
			processInstanceQuery = runtimeService.createProcessInstanceQuery()
					.processDefinitionKey(processDefinitionKey).suspended()
					.orderByProcessInstanceId().desc();
		}
		return processInstanceQuery.list();
	}

	/**
	 * 启动流程实例
	 * 
	 * @param entity
	 *            业务实体（需继承WorkFlowEntity）
	 * @param processDefinitionKey
	 *            业务流程key
	 * @param variables
	 *            流程变量（可为null）
	 * @return
	 */
	public ProcessInstance startWorkflow(WorkFlowEntity entity,
			String processDefinitionKey, Map<String, Object> variables) {
		String businessKey = ObjectUtils.toString(entity.getId());
		ProcessInstance processInstance = null;
		try {
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());
			processInstance = runtimeService.startProcessInstanceByKey(
					processDefinitionKey, businessKey, variables);
			logger.debug(
					"start process of {key={}, bkey={}, pid={}, variables={}}",
					new Object[] { processDefinitionKey, businessKey,
							processInstance.getId(), variables });
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		return processInstance;
	}

	/**
	 * 设置流程实体的流程相关属性
	 * 
	 * @param entity
	 */
	public void setWorkFlowEntity(WorkFlowEntity entity) {
		String processInstanceId = entity.getProcessInstanceId();
		if (StringUtils.isBlank(processInstanceId)) {
			throw new ServiceException("processInstanceId is empty");
		}
		ProcessInstance processInstance = getProcessInstance(processInstanceId);
		// 流程实例和流程定义
		if (processInstance != null) {
			entity.setProcessInstance(processInstance);
			entity.setHistoricProcessInstance(getHistoricProcessInstance(processInstanceId));
			entity.setProcessDefinition(getProcessDefinition(processInstance
					.getProcessDefinitionId()));
		} else {
			entity.setHistoricProcessInstance(getHistoricProcessInstance(processInstanceId));
			entity.setProcessDefinition(getProcessDefinition(entity
					.getHistoricProcessInstance().getProcessDefinitionId()));
		}
		// 当前任务
		if (entity.getTask() == null) {
			entity.setTask(getCurrentTask(processInstanceId));
		}
		// 任务批注
		entity.setComments(taskService
				.getProcessInstanceComments(processInstanceId));
		// 历史任务（包括当前任务）
		List<HistoricTaskInstance> historicTaskInstances = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByHistoricTaskInstanceStartTime().asc().list();
		entity.setHistoricTaskInstances(historicTaskInstances);
	}

	/**
	 * 完成当前任务并返回下一任务对象
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @param comment
	 *            批注
	 * @param map
	 *            流程变量kv
	 * @return
	 */
	public Task completeCurrentTask(String processInstanceId, String comment,
			Map<String, Object> map) {
		Task task = getCurrentTask(processInstanceId);
		// 添加批注
		taskService.addComment(task.getId(), processInstanceId, comment);
		// 完成任务
		taskService.complete(task.getId(), map);
		// 查询下一任务
		task = getCurrentTask(processInstanceId);
		return task;
	}

	/**
	 * 完成当前任务并返回下一任务对象
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @param map
	 *            流程变量kv
	 * @return
	 */
	public Task completeCurrentTask(String processInstanceId,
			Map<String, Object> map) {
		Task task = getCurrentTask(processInstanceId);
		// 完成任务
		taskService.complete(task.getId(), map);
		// 查询下一任务
		task = getCurrentTask(processInstanceId);
		return task;
	}

	/**
	 * 完成当前任务并返回下一任务对象
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	public Task completeCurrentTask(String processInstanceId) {
		Task task = getCurrentTask(processInstanceId);
		// 完成任务
		taskService.complete(task.getId());
		// 查询下一任务
		task = getCurrentTask(processInstanceId);
		return task;
	}

	/**
	 * 获得流程实例当前任务名
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	public String getCurrentTaskName(String processInstanceId) {
		return taskService.createTaskQuery()
				.processInstanceId(processInstanceId).singleResult().getName();
	}

	/**
	 * 获得流程实例
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	public ProcessInstance getProcessInstance(String processInstanceId) {
		return runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 获得历史流程实例
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	public HistoricProcessInstance getHistoricProcessInstance(
			String processInstanceId) {
		return historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 获得流程定义
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @return
	 */
	public ProcessDefinition getProcessDefinition(String processDefinitionId) {
		return repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
	}
	
	/**
	 * 获得某个流程定义下的节点列表
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @return
	 */
	public List<ActivityImpl> getActivitys(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) processDefinition;
		return pde.getActivities();
	}

	/**
	 * 获得流程定义列表
	 * 
	 * @return
	 */
	public List<ProcessDefinition> getProcessDefinition() {
		return repositoryService.createProcessDefinitionQuery().list();
	}

	/**
	 * 获得当前任务
	 * 
	 * @param processInstanceId
	 *            流程实例ID
	 * @return
	 */
	public Task getCurrentTask(String processInstanceId) {
		return taskService.createTaskQuery()
				.processInstanceId(processInstanceId).singleResult();
	}

}
