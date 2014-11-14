<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<%@include file="/WEB-INF/views/include/treetable.jsp"%>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#treeTable").treeTable({
			expandLevel : 3
		});
	});
	</script>
</head>
<body>
	<tags:message content="${message}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<shiro:hasPermission name="sys:org:create">
			<a href="${ctxModule}/create" class="btn btn-primary"> 
				<span class="glyphicon glyphicon-plus"></span> 添加机构
			</a>
			</shiro:hasPermission>
		</form>
	</nav>
	
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">机构列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${ctxModule}/delete" valid="false" method="post">
				<table id="treeTable" class="table table-striped table-hover">
					<thead>
						<tr>
							<th>机构名称</th>
							<th>归属区域</th>
							<th>机构编码</th>
							<th>机构类型</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${entitys}" var="org">
							<tr id="${org.id}" pId="${org.parentId ne '1' ? org.parentId : '0'}">
								<td>
			    					<a href="${ctxModule}/update/${org.id}" title="修改">
										${org.name}
									</a>
								</td>
								<td>${org.areaId}</td>
								<td>${org.code}</td>
								<td>${org.type}</td>
								<td>
									<shiro:hasPermission name="sys:org:delete">
								    <a href="${ctxModule}/delete/${org.id}" class="btn btn-danger" title="删除"
								    	onclick="return confirmx('要删除该机构及所有子机构项吗?', this.href)">
								    	<span class="glyphicon glyphicon-remove"></span>
								    </a> 
								    </shiro:hasPermission>
								    <shiro:hasPermission name="sys:org:create">
								    <a href="${ctxModule}/create?parentId=${org.id}&areaId=${org.areaId}" class="btn btn-primary" title="添加子机构"> 
								    	<span class="glyphicon glyphicon-plus"></span>
									</a>
									</shiro:hasPermission>	 
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
