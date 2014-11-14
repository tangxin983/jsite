<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionName}管理</title>
	<!-- 这里引入额外的css和js 
	<link rel="stylesheet" type="text/css" href="" />
	<script type="text/javascript" src=""></script>
	-->
	<script type="text/javascript">
	function del(){
		return confirmx_func('确定要删除选中的记录吗?', function(){
			location.href = "${r"${ctxModule}"}/delete/" + $("#${className}TableSelectedId").val();
		});
	}
	</script>
</head>
<body>
	<tags:message content="${r"${message}"}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<#list entityFields as field>
			<div class="form-group">
				<input name="eq_${field.name}" class="form-control" placeholder="${field.colRemark}">
			</div>
			</#list>
		</form>
	</nav>

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">${functionName}列表</div>
		</div>
		<div class="panel-body">
			<!-- table -->
			<ui:datagrid id="${className}Table" title="${functionName}列表" <#if isPagination>serverSide="true"<#else>serverSide="false"</#if>
				queryUrl="${r"${ctxModule}"}?datagrid" css="table table-striped table-hover table-bordered">
				<ui:dgToolBar title="查询" script="${className}Table.draw();" />
				<shiro:hasPermission name="${permissionPrefix}:create">
				<ui:dgToolBar title="添加" url="${r"${ctxModule}"}/create" />
				</shiro:hasPermission>
				<shiro:hasPermission name="${permissionPrefix}:edit">
				<ui:dgToolBar title="编辑" type="select" url="${r"${ctxModule}"}/update/{selected}" />
				</shiro:hasPermission>
				<shiro:hasPermission name="${permissionPrefix}:delete">
				<ui:dgToolBar title="删除" type="select" onclick="del" />
				</shiro:hasPermission>
				<#list entityFields as field>
				<ui:dgCol title="${field.colRemark}" field="${field.name}"></ui:dgCol>
				</#list>
			</ui:datagrid>
		</div>
	</div>
	
</body>
</html>
