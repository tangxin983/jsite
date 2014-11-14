<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
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
	<tags:message content="${message}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<div class="form-group">
				<select name="s_type" class="form-control">
					<option value="" ${param.s_type eq "" ? "selected" : ""}></option>
					<c:forEach items="${typeList}" var="type">
						<option value="${type}" ${param.s_type eq type ? "selected" : ""}>${type}</option>
					</c:forEach>
				</select>
			</div>
			<button type="submit" class="btn btn-primary">
				<span class="glyphicon glyphicon-search"></span> 查询
			</button>
			<shiro:hasPermission name="sys:dict:create">
			
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:dict:delete">
			
			</shiro:hasPermission>
			<a href="${ctxModule}/create" class="btn btn-primary"> 
				<span class="glyphicon glyphicon-plus"></span> 添加字典
			</a>
			<a onclick="multiDel()" class="btn btn-danger">
				<span class="glyphicon glyphicon-remove"></span> 删除
			</a>
		</form>
	</nav>
	
	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">字典列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${ctxModule}/delete" valid="false" method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th><input type="checkbox" id="selectAll"></th>
							<th>字典类型</th>
							<th>字典名称</th>
							<th>字典值</th>
							<th>描述</th>
							<!--
							<th>操作</th>
							-->
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="entity">
							<tr>
								<td><input type="checkbox" name="ids" value="${entity.id}"></td>
								<td>${entity.type}</td>
		    					<td>
		    						<shiro:hasPermission name="sys:dict:edit">
		    						<a href="${ctxModule}/update/${entity.id}" title="修改">
										${entity.label}
									</a>
									</shiro:hasPermission>
									<shiro:lacksPermission name="sys:dict:edit">
									<a href="${ctxModule}/update/${entity.id}" title="修改">
										${entity.label}
									</a>
									</shiro:lacksPermission>
		    					</td>
							  	<td>${entity.value}</td>
							  	<td>${entity.description}</td>
								<!--
								<td>
									<a href="${ctxModule}/update/${entity.id}" class="btn btn-default" title="修改">
										<span class="glyphicon glyphicon-edit"></span>
									</a>
								    <a href="${ctxModule}/delete/${entity.id}" class="btn btn-danger" title="删除"
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
			<tags:pagination page="${page}" />
		</div>
	</div>
</body>
</html>
