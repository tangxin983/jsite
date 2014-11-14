<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>区域管理</title>
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
			<shiro:hasPermission name="sys:area:create">
				<a href="${ctxModule}/create" class="btn btn-primary"> <span
					class="glyphicon glyphicon-plus"></span> 添加区域
				</a>
			</shiro:hasPermission>
		</form>
	</nav>

	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">区域列表</div>
		</div>
		<div class="panel-body">
			<table id="treeTable" class="table table-striped table-hover">
				<thead>
					<tr>
						<th>区域名称</th>
						<th>区域编码</th>
						<th>区域类型</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${entitys}" var="area">
						<tr id="${area.id}" pId="${area.parentId ne '1' ? area.parentId : '0'}">
							<td>
		    					<a href="${ctxModule}/update/${area.id}" title="修改">
									${area.name}
								</a>
							</td>
							<td>${area.code}</td>
							<td>${area.type}</td>
							<td>
								<shiro:hasPermission name="sys:area:delete">
							    <a href="${ctxModule}/delete/${area.id}" class="btn btn-danger" title="删除"
							    	onclick="return confirmx('要删除该区域及所有子区域项吗?', this.href)">
							    	<span class="glyphicon glyphicon-remove"></span>
							    </a> 
							    </shiro:hasPermission>
							    <shiro:hasPermission name="sys:area:create">
							    <a href="${ctxModule}/create?parentId=${area.id}" class="btn btn-primary" title="添加子区域"> 
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
