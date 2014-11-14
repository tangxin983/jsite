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
			<div class="text-muted bootstrap-admin-box-title">已结束流程</div>
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
							<th>启动时间</th>
							<th>结束时间</th>
							<th>结束原因</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="object">
							<c:set var="instance" value="${object[1]}" />
							<c:set var="def" value="${object[0]}" />
							<tr>
								<td>
									<a href="${ctx}/oa/${def.key}/detail/${instance.businessKey}"> 
									    ${instance.businessKey}
									</a>
								</td>
								<td>${def.name}</td>
								<td>${def.version}</td>
								<td>${fns:getUserById(instance.startUserId).name}</td>
								<td><fmt:formatDate value="${instance.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${instance.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${instance.deleteReason}</td>
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
