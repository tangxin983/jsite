<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>评论管理</title>
	<!-- 这里引入额外的css和js 
	<link rel="stylesheet" type="text/css" href="" />
	<script type="text/javascript" src=""></script>
	-->
	<script type="text/javascript">
	function del(){
		return confirmx_func('确定要删除选中的记录吗?', function(){
			location.href = "${ctxModule}/delete/" + $("#roleTableSelectedId").val();
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
				<input name="eq_officeId" class="form-control" placeholder="归属机构">
			</div>
			<div class="form-group">
				<input name="eq_name" class="form-control" placeholder="角色名称">
			</div>
			<div class="form-group">
				<input name="eq_enName" class="form-control" placeholder="角色英文名(即act_id_group的id_)">
			</div>
			<div class="form-group">
				<input name="eq_dataScope" class="form-control" placeholder="数据范围">
			</div>
			<div class="form-group">
				<input name="eq_createBy" class="form-control" placeholder="创建者">
			</div>
			<div class="form-group">
				<input name="eq_createTime" class="form-control" placeholder="创建时间">
			</div>
			<div class="form-group">
				<input name="eq_updateBy" class="form-control" placeholder="更新者">
			</div>
			<div class="form-group">
				<input name="eq_updateTime" class="form-control" placeholder="更新时间">
			</div>
			<div class="form-group">
				<input name="eq_remarks" class="form-control" placeholder="备注信息">
			</div>
			<div class="form-group">
				<input name="eq_delFlag" class="form-control" placeholder="删除标记">
			</div>
		</form>
	</nav>

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">评论列表</div>
		</div>
		<div class="panel-body">
			<!-- table -->
			<ui:datagrid id="roleTable" title="评论列表" serverSide="true"
				queryUrl="${ctxModule}?datagrid" css="table table-striped table-hover table-bordered">
				<ui:dgToolBar title="查询" script="roleTable.draw();" />
				<shiro:hasPermission name="sys:role:create">
				<ui:dgToolBar title="添加" url="${ctxModule}/create" />
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:role:edit">
				<ui:dgToolBar title="编辑" type="select" url="${ctxModule}/update/{selected}" />
				</shiro:hasPermission>
				<shiro:hasPermission name="sys:role:delete">
				<ui:dgToolBar title="删除" type="select" onclick="del" />
				</shiro:hasPermission>
				<ui:dgCol title="角色名称" field="name"></ui:dgCol>
				<ui:dgCol title="角色英文名" field="enName"></ui:dgCol>
			</ui:datagrid>
		</div>
	</div>
	
</body>
</html>
