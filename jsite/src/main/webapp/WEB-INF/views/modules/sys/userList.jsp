<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<!-- 这里引入额外的css和js 
	<link rel="stylesheet" type="text/css" href="" />
	<script type="text/javascript" src=""></script>
	-->
	<script type="text/javascript">
	function del(){
		return confirmx_func('确定要删除选中的记录吗?', function(){
			location.href = "${ctxModule}/delete/" + $("#userTableSelectedId").val();
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
				<input name="li_name" class="form-control" placeholder="姓名">
			</div>
		</form>
	</nav>

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">用户列表</div>
		</div>
		<div class="panel-body">
			<!-- table -->
			<ui:datagrid id="userTable" title="用户列表" serverSide="true"
				queryUrl="${ctxModule}?datagrid" css="table table-striped table-hover table-bordered">
				<ui:dgToolBar title="查询" script="userTable.draw();" />
				<shiro:hasPermission name="sys:user:create">
				<ui:dgToolBar title="添加" url="${ctxModule}/create" />
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:user:edit">
				<ui:dgToolBar title="编辑" type="select" url="${ctxModule}/update/{selected}" />
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:user:delete">
				<ui:dgToolBar title="删除" type="select" onclick="del" />
				</shiro:hasPermission>
				<ui:dgCol title="姓名" field="name"></ui:dgCol>
				<ui:dgCol title="登录名" field="loginName"></ui:dgCol>
				<ui:dgCol title="邮箱" field="email"></ui:dgCol>
				<ui:dgCol title="角色" field="roleNames" orderable="false"></ui:dgCol>
			</ui:datagrid>
		</div>
	</div>
	
</body>
</html>
