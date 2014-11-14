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
	<tags:message content="${r"${message}"}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<#list entityFields as field>
			<div class="form-group">
				<input name="s_${field.name}" value="${r"${param.s_"}${field.name}}" class="form-control" placeholder="${field.colRemark}">
			</div>
			</#list>
			<button type="submit" class="btn btn-primary">
				<span class="glyphicon glyphicon-search"></span> 查询
			</button>
			<shiro:hasPermission name="${permissionPrefix}:create">
			<a href="${r"${ctxModule}"}/create" class="btn btn-primary"> 
				<span class="glyphicon glyphicon-plus"></span> 添加${functionName}
			</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${permissionPrefix}:delete">
			<a onclick="multiDel()" class="btn btn-danger">
				<span class="glyphicon glyphicon-remove"></span> 删除
			</a>
			</shiro:hasPermission>
		</form>
	</nav>
	
	<!-- table -->
	<ui:datagrid id="${className}Table" title="${functionName}列表" <#if isPagination>serverSide="true"<#else>serverSide="false"</#if>
		queryUrl="${r"${ctxModule}"}?datagrid" css="table table-striped table-hover">
		<#list entityFields as field>
		<ui:dgCol title="${field.colRemark}" field="${field.name}"></ui:dgCol>
		</#list>
	</ui:datagrid>
	
</body>
</html>
