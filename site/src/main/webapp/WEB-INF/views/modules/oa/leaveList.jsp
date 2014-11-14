<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假管理</title>
	<!-- 这里引入额外的css和js 
	<link rel="stylesheet" type="text/css" href="" />
	<script type="text/javascript" src=""></script>
	-->
	<script type="text/javascript">
		function multiDel(){
			$("[name='ids']").each(function(){
				if($(this).is(":checked")){
					return confirmx_func('确定要删除选中的记录吗?', function(){$("#viewForm").submit();})
				}
			});
		}
	</script>
</head>
<body>
	<tags:message content="${message}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<div class="form-group">
				<input name="s_startTime" value="${param.s_startTime}" class="form-control Wdate" placeholder="开始时间" 
					readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			</div>
			<div class="form-group">
				<input name="s_endTime" value="${param.s_endTime}" class="form-control Wdate" placeholder="结束时间" 
					readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			</div>
			<div class="form-group">
				<select name="s_processStatus" class="form-control">
					<option value="" ${param.s_processStatus eq "" ? "selected" : ""}>全部</option>
					<option value="0" ${param.s_processStatus eq "0" ? "selected" : ""}>运行中</option>
					<option value="1" ${param.s_processStatus eq "1" ? "selected" : ""}>已完成</option>				 
				</select>
			</div>
			<div class="form-group">
				<select name="s_leaveType" class="form-control">
					<option value="" ${param.s_leaveType eq "" ? "selected" : ""}>全部</option>
					<c:forEach items="${fns:getDictList('oa_leave_type')}" var="type">
						<option value="${type.value}" ${param.s_leaveType eq type.value ? "selected" : ""}>${type.label}</option>
					</c:forEach>
				</select>
			</div>
			<button type="submit" class="btn btn-primary">
				<span class="glyphicon glyphicon-search"></span> 查询
			</button>
			<a href="${ctxModule}/create" class="btn btn-primary"> 
				<span class="glyphicon glyphicon-plus"></span> 添加请假
			</a>
		</form>
	</nav>
	
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">请假列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${ctxModule}/delete" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>单据号</th>
							<th>假种</th>
							<th>申请时间</th>
							<th>请假开始</th>
							<th>请假结束</th>
							<th>当前节点</th>
							<th>当前处理人</th>
							<th>流程状态</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="leave">
							<tr>
								<td>
									<a href="${ctxModule}/detail/${leave.id}"> 
									    ${leave.id}
									</a>
								</td>
								<td>${fns:getDictLabel('oa_leave_type',leave.leaveType,'')}</td>
								<td><fmt:formatDate value="${leave.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<c:if test="${not empty leave.processInstance}">
									<td>
										<a target="_blank" href="${ctx}/diagram-viewer/index.html?processDefinitionId=${leave.processInstance.processDefinitionId}&processInstanceId=${leave.processInstance.id}">${leave.task.name}
									</td>
									<td>${fns:getUserById(leave.task.assignee).name}</td>
								</c:if>
								<c:if test="${not empty leave.historicProcessInstance.endTime}">
									<td></td>
									<td></td>
								</c:if>
								<td>${leave.processStatus eq "1" ? "已完成" : "运行中"}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<tags:pagination page="${page}" />
		</div>
	</div>
</body>
</html>
