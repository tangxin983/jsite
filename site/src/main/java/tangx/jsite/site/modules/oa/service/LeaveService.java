package tangx.jsite.site.modules.oa.service;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tangx.jsite.site.core.persistence.entity.Page;
import tangx.jsite.site.core.web.service.BaseService;
import tangx.jsite.site.modules.oa.dao.LeaveDao;
import tangx.jsite.site.modules.oa.entity.Leave;
import tangx.jsite.site.modules.workflow.service.WorkFlowService;
import tangx.jsite.site.utils.SysUtil;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * 请假Service
 * 
 * @author tangx
 * @since 2014-05-28
 */
@Service
@Transactional
public class LeaveService extends BaseService<Leave> {

	private static final String PROCESS_DEF_KEY = "leave";

	@Autowired
	private WorkFlowService workFlowService;

	private LeaveDao leaveDao;

	@Autowired
	public void setLeaveDao(LeaveDao leaveDao) {
		super.setDao(leaveDao);
		this.leaveDao = leaveDao;
	}

	/**
	 * 启动请假流程
	 * 
	 * @param entity
	 */
	public ProcessInstance saveLeave(Leave entity) {
		// 设置流程发起人和发起时间
		entity.setApplyUser(SysUtil.getCurrentUserId());
		entity.setApplyTime(new Date());
		leaveDao.insert(entity);
		ProcessInstance processInstance = null;
		if(StringUtils.isNotBlank(entity.getProcType())){
			// 测试用流程
			processInstance = workFlowService.startWorkflow(entity,
					entity.getProcType(), null);
		}else{
			// 启动流程
			processInstance = workFlowService.startWorkflow(entity,
					PROCESS_DEF_KEY, null);
		}
		
		// 设置流程实例ID和流程状态
		entity.setProcessInstanceId(processInstance.getId());
		entity.setProcessStatus("0");
		leaveDao.update(entity);
		return processInstance;
	}

	/**
	 * 分页获取激活中的请假流程列表
	 * 
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @param isAll
	 *            true所有人的请假单；false当前用户请假单
	 * @return
	 */
	public Page<Leave> findLeaveInstanceByPage(
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			boolean isAll) {
		Table<String, String, Object> table = HashBasedTable.create();
		if (!isAll) {
			// 设置只获取当前用户的请假单
			table.put("applyUser", "=", SysUtil.getCurrentUserId());
		}
		if(searchParams.get("leaveType") != null) {
			table.put("leaveType", "=", searchParams.get("leaveType"));
		}
		if(searchParams.get("processStatus") != null) {
			table.put("processStatus", "=", searchParams.get("processStatus"));
		}
		if(searchParams.get("startTime") != null) {
			table.put("startTime", ">=", searchParams.get("startTime"));
		}
		if(searchParams.get("endTime") != null) {
			table.put("endTime", "<=", searchParams.get("endTime"));
		}
		// 根据条件获取请假列表
		Page<Leave> page = selectByPage(table, null, pageNumber, pageSize);
		if (page.getData().size() > 0) {
			for (Leave leave : page.getData()) {
				if(StringUtils.isBlank(leave.getProcessInstanceId())){
					continue;
				}
				workFlowService.setWorkFlowEntity(leave);
			}
		}
		return page;
	}

	/**
	 * 获取请假流程详情
	 * 
	 * @param id
	 *            业务ID
	 * @return
	 */
	public Leave getLeaveDetail(String id) {
		Leave leave = selectById(id);
		workFlowService.setWorkFlowEntity(leave);
		return leave;
	}

	/**
	 * 完成任务
	 * 
	 * @param leave
	 *            业务实体
	 */
	public void completeTask(Leave leave) {
		Task task = null;
		if (StringUtils.isNotBlank(leave.getComment())) {
			task = workFlowService.completeCurrentTask(
					leave.getProcessInstanceId(), leave.getComment(),
					leave.getVariable());
		} else {
			task = workFlowService.completeCurrentTask(
					leave.getProcessInstanceId(), leave.getVariable());
		}
		// 表示流程已结束
		if (task == null) {
			leave.setProcessStatus("1");
		}
		leaveDao.update(leave);
	}
}
