<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
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
				<input name="s_name" value="${param.s_name}" class="form-control" placeholder="姓名">
			</div>
			<div class="form-group">
				<input name="s_loginName" value="${param.s_loginName}" class="form-control" placeholder="登录名">
			</div>
			<button type="submit" class="btn btn-primary">
				<span class="glyphicon glyphicon-search"></span> 查询
			</button>
			<shiro:hasPermission name="sys:user:create">
			<a href="${ctxModule}/create" class="btn btn-primary"> 
				<span class="glyphicon glyphicon-plus"></span> 添加用户
			</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:user:delete">
			<a onclick="multiDel()" class="btn btn-danger">
				<span class="glyphicon glyphicon-remove"></span> 删除
			</a>
			</shiro:hasPermission>
		</form>
	</nav>
	
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">用户列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${ctxModule}/delete" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th><input type="checkbox" id="selectAll"></th>
							<th>姓名</th>
							<th>登录名</th>
							<th>角色</th>
							<th>邮箱</th>
							<th>手机</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="entity">
							<tr>
								<td><input type="checkbox" name="ids" value="${entity.id}"></td>
								<td>
									<shiro:hasPermission name="sys:user:edit">
		    						<a href="${ctxModule}/update/${entity.id}" title="修改">
										${entity.name}
									</a>
									</shiro:hasPermission>
									<shiro:lacksPermission name="sys:user:edit">
									${entity.name}
									</shiro:lacksPermission>
								</td>
								<td>${entity.loginName}</td>
								<td>${fns:extractProperty(entity.roles, "name", true)}</td>
								<td>${entity.email}</td>
								<td>${entity.mobile}</td>
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
