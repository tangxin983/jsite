<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>菜单管理</title>
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#treeTable").treeTable({
			expandLevel : 2
		});
	});
</script>
</head>

<body>

	<tags:message content="${message}" />
	 
	<shiro:hasPermission name="sys:menu:create">
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<a href="${ctxModule}/create" class="btn btn-primary">
				<span class="glyphicon glyphicon-plus"></span> 添加一级菜单
			</a>
		</form>
	</nav>
	</shiro:hasPermission>
	
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">菜单列表</div>
		</div>
		<div class="panel-body">
			<table id="treeTable" class="table table-striped table-hover">
				<thead>
					<tr>
						<th>名称</th>
						<th>链接</th>
						<th>排序</th>
						<th>可见</th>
						<th>权限标识</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${entitys}" var="menu">
						<tr id="${menu.id}" pId="${menu.parentId ne '1' ? menu.parentId : '0'}">
							<td>
								<c:if test="${not empty menu.icon}">
									<span class="glyphicon glyphicon-${menu.icon}"></span>
								</c:if>
								<shiro:hasPermission name="sys:menu:edit">
		    					<a href="${ctxModule}/update/${menu.id}" title="修改">
									${menu.name}
								</a>
								</shiro:hasPermission>
								<shiro:lacksPermission name="sys:menu:edit">
								${menu.name}
								</shiro:lacksPermission>
							</td>
							<td>${menu.href}</td>
							<td>${menu.sort}</td>
							<td>${menu.isShow eq '1'?'显示':'隐藏'}</td>
							<td>${menu.permission}</td>
							<td>
								<shiro:hasPermission name="sys:menu:delete">
							    <a href="${ctxModule}/delete/${menu.id}" class="btn btn-danger" title="删除"
							    	onclick="return confirmx('要删除该菜单及所有子菜单项吗?', this.href)">
							    	<span class="glyphicon glyphicon-remove"></span>
							    </a> 
							    </shiro:hasPermission>
							    <shiro:hasPermission name="sys:menu:create">
							    <a href="${ctxModule}/create?parentId=${menu.id}" class="btn btn-primary" title="添加子菜单"> 
							    	<span class="glyphicon glyphicon-plus"></span>
								</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>
