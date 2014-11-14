<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>已办</title>
</head>
<body>
	<tags:message content="${message}" />
	<!-- search form -->
	<%@ include file="/WEB-INF/views/include/workflowQuery.jsp"%>
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">已办任务</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>单据号</th>
							<th>流程名称</th>
							<th>任务名称</th>
							<th>到达时间</th>
							<th>结束时间</th>
							<th>当前节点</th>
							<th>当前处理人</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="entity">
							<tr>
								<td>
									<a href="${ctx}/oa/${entity.processDefinition.key}/detail/${entity.historicProcessInstance.businessKey}"> 
									    ${entity.historicProcessInstance.businessKey}
									</a>
								</td>
								<td>${entity.processDefinition.name}</td>
								<td>${entity.historicTaskInstance.name}</td>
								<td><fmt:formatDate value="${entity.historicTaskInstance.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${entity.historicTaskInstance.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<a target="_blank" href="${ctx}/diagram-viewer/index.html?processDefinitionId=${entity.processDefinition.id}&processInstanceId=${entity.processInstance.id}">${entity.task.name}
								</td>
								<td>${fns:getUserById(entity.task.assignee).name}</td>
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
