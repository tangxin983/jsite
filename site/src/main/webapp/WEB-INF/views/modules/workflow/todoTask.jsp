<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待办</title>
</head>
<body>
	<tags:message content="${message}" />
	<!-- search form -->
	<%@ include file="/WEB-INF/views/include/workflowQuery.jsp"%>
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">待办列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>单据号</th>
							<th>流程名称</th>
							<th>发起人</th>
							<th>发起时间</th>
							<th>当前节点</th>
							<th>到达时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="entity">
							<tr>
								<td>${entity.processInstance.businessKey}</td>
								<td>${entity.processDefinition.name}</td>
								<td>${fns:getUserById(entity.historicProcessInstance.startUserId).name}</td>
								<td><fmt:formatDate value="${entity.historicProcessInstance.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<a target="_blank" href="${ctx}/diagram-viewer/index.html?processDefinitionId=${entity.processDefinition.id}&processInstanceId=${entity.processInstance.id}">${entity.task.name}
								</td>
								<td><fmt:formatDate value="${entity.task.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<c:if test="${empty entity.task.assignee}">
										<a href="${ctx}/workflow/task/claim/${entity.task.id}" class="btn btn-primary">
											<span class="glyphicon glyphicon-info-sign"></span> 签收
										</a>
									</c:if>
									<c:if test="${not empty entity.task.assignee}">
										<a href="${ctx}/oa/${entity.processDefinition.key}/handle/${entity.processInstance.businessKey}" class="btn btn-primary"> 
										    <span class="glyphicon glyphicon-edit"></span> 办理
										</a>
									</c:if>
								</td>
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
