<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程列表</title>
</head>
<body>
	<tags:message content="${message}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<a href="${ctx}/workflow/process/deployForm" class="btn btn-primary"> 
				<span class="glyphicon glyphicon-plus"></span> 部署流程
			</a>
		</form>
	</nav>
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">流程列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${ctxModule}/delete" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>键值</th>
							<th>版本</th>
							<th>XML</th>
							<th>图片</th>
							<th>部署时间</th>
							<th>是否挂起</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="object">
							<c:set var="process" value="${object[0]}" />
							<c:set var="deployment" value="${object[1]}" />
							<tr>
								<td>${process.name}</td>
								<td>${process.key}</td>
								<td>${process.version}</td>
								<td><a target="_blank" href='${ctx}/workflow/resource/read?processId=${process.id}&type=xml'>${process.resourceName}</a></td>
								<td><a target="_blank" href='${ctx}/workflow/resource/read?processId=${process.id}&type=image'>${process.diagramResourceName}</a></td>
								<td><fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${process.suspended} |
									<c:if test="${process.suspended}">
										<a href="${ctx}/workflow/process/active/${process.id}">激活</a>
									</c:if>
									<c:if test="${!process.suspended}">
										<a href="${ctx}/workflow/process/suspend/${process.id}">挂起</a>
									</c:if>
								</td>
								<td>
			                        <a href='${ctx}/workflow/process/delete/${process.deploymentId}'>删除</a>
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
