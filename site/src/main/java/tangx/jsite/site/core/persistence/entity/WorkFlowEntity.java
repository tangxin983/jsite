package tangx.jsite.site.core.persistence.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;

import com.google.common.collect.Maps;

/**
 * 工作流业务实体需继承此类
 * 
 * @author tangx
 * 
 */
@SuppressWarnings("serial")
public class WorkFlowEntity extends BaseEntity {

	// 流程发起人
	@Column(name = "apply_user")
	protected String applyUser;

	// 流程发起时间
	@Column(name = "apply_time")
	protected Date applyTime;

	// 流程实例ID
	@Column(name = "process_instance_id")
	protected String processInstanceId;

	// 当前节点名
	@Column(name = "process_status")
	protected String processStatus;

	// 任务
	protected Task task;

	// 历史任务
	protected HistoricTaskInstance historicTaskInstance;

	// 运行中的流程实例
	protected ProcessInstance processInstance;

	// 历史流程实例
	protected HistoricProcessInstance historicProcessInstance;

	// 历史任务列表
	protected List<HistoricTaskInstance> historicTaskInstances;

	// 流程定义
	protected ProcessDefinition processDefinition;

	// 任务批注
	protected List<Comment> comments;

	// 存储流程变量
	protected Map<String, Object> variable = Maps.newHashMap();

	// 批注
	protected String comment;

	public HistoricTaskInstance getHistoricTaskInstance() {
		return historicTaskInstance;
	}

	public void setHistoricTaskInstance(
			HistoricTaskInstance historicTaskInstance) {
		this.historicTaskInstance = historicTaskInstance;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<String, Object> getVariable() {
		// 处理流程变量
		for (String key : variable.keySet()) {
			if (ObjectUtils.toString(variable.get(key))
					.equalsIgnoreCase("true")) {
				variable.put(key, true);
			}
			if (ObjectUtils.toString(variable.get(key)).equalsIgnoreCase(
					"false")) {
				variable.put(key, false);
			}
		}
		return variable;
	}

	public void setVariable(Map<String, Object> variable) {
		this.variable = variable;
	}

	public Map<String, Object> getCommentMap() {
		Map<String, Object> map = Maps.newHashMap();
		if (comments != null && comments.size() > 0) {
			for (Comment comment : comments) {
				map.put(comment.getTaskId(), comment.getFullMessage());
			}
		}
		return map;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(
			HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public List<HistoricTaskInstance> getHistoricTaskInstances() {
		return historicTaskInstances;
	}

	public void setHistoricTaskInstances(
			List<HistoricTaskInstance> historicTaskInstances) {
		this.historicTaskInstances = historicTaskInstances;
	}

}
