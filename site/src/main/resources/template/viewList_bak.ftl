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
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">${functionName}列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${r"${ctxModule}"}/delete" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th><input type="checkbox" id="selectAll"></th>
							<#list entityFields as field>
							<th>${field.colRemark}</th>
							</#list>
							<!--
							<th>操作</th>
							-->
						</tr>
					</thead>
					<tbody>
						<#if isPagination>
						<c:forEach items="${r"${page.result}"}" var="entity">
						<#else>
						<c:forEach items="${r"${entitys}"}" var="entity">
						</#if>
							<tr>
								<td><input type="checkbox" name="ids" value="${r"${entity.id}"}"></td>
								<#list entityFields as field>
								<#if field_index == 0>
		    					<td>
		    						<shiro:hasPermission name="${permissionPrefix}:edit">
		    						<a href="${r"${ctxModule}"}/update/${r"${entity.id}"}" title="修改">
										${r"${entity."}${field.name}}
									</a>
									</shiro:hasPermission>
									<shiro:lacksPermission name="${permissionPrefix}:edit">
									${r"${entity."}${field.name}}
									</shiro:lacksPermission>
		    					</td>
		    					<#else>
			    				<#if field.type == 'Date'>
								<td><fmt:formatDate value="${r"${entity."}${field.name}}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<#else>
								<td>${r"${entity."}${field.name}}</td>
								</#if>
								</#if>
								</#list>
								<!--
								<td>
									<a href="${r"${ctxModule}"}/update/${r"${entity.id}"}" class="btn btn-default" title="修改">
										<span class="glyphicon glyphicon-edit"></span>
									</a>
								    <a href="${r"${ctxModule}"}/delete/${r"${entity.id}"}" class="btn btn-danger" title="删除"
								    	onclick="return confirmx('确定要删除吗?', this.href)">
								    	<span class="glyphicon glyphicon-remove"></span>
								    </a> 
								</td>
								-->
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<#if isPagination>
			<tags:pagination page="${r"${page}"}" />
			</#if>
		</div>
	</div>
</body>
</html>
