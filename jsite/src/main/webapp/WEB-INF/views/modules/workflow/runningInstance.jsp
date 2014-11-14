<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运行流程</title>
</head>
<body>
	<tags:message content="${message}" />
	<!-- search form -->
	<%@ include file="/WEB-INF/views/include/workflowQuery.jsp"%>
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">运行流程</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>单据号</th>
							<th>流程名称</th>
							<th>流程版本</th>
							<th>发起人</th>
							<th>当前节点</th>
							<th>当前处理人</th>
							<th>是否挂起</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="object">
							<c:set var="def" value="${object[0]}" />
							<c:set var="instance" value="${object[1]}" />
							<c:set var="hisInstance" value="${object[2]}" />
							<c:set var="task" value="${object[3]}" />
							<tr>
								<td>
									<a href="${ctx}/oa/${def.key}/detail/${instance.businessKey}"> 
									    ${instance.businessKey}
									</a>
								</td>
								<td>${def.name}</td>
								<td>${def.version}</td>
								<td>${fns:getUserById(hisInstance.startUserId).name}</td>
								<td>
									<a target="_blank" href="${ctx}/diagram-viewer/index.html?processDefinitionId=${instance.processDefinitionId}&processInstanceId=${instance.id}">${task.name}
								</td>
								<td>${fns:getUserById(task.assignee).name}</td>
								<td>${instance.suspended}</td>
								<td>
									<c:if test="${instance.suspended}">
										<a href="${ctx}/workflow/instance/active/${instance.processInstanceId}">激活</a>
									</c:if>
									<c:if test="${!instance.suspended}">
										<a href="${ctx}/workflow/instance/suspend/${instance.processInstanceId}">挂起</a>
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
