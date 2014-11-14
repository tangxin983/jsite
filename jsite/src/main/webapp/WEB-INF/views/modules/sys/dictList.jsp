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
	function del(){
		return confirmx_func('确定要删除选中的记录吗?', function(){
			location.href = "${ctxModule}/delete/" + $("#dictTableSelectedId").val();
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
				<select name="eq_type" class="form-control">
					<option value=""></option>
					<c:forEach items="${typeList}" var="type">
						<option value="${type}">${type}</option>
					</c:forEach>
				</select>
			</div>
		</form>
	</nav>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">字典列表</div>
		</div>
		<div class="panel-body">
			<!-- table -->
			<ui:datagrid id="dictTable" title="字典列表" serverSide="true" 
				queryUrl="${ctxModule}?datagrid" css="table table-striped table-hover table-bordered">
				<ui:dgToolBar title="查询" script="dictTable.draw();" />
				<ui:dgToolBar title="添加" url="${ctxModule}/create" />
				<ui:dgToolBar title="编辑" type="select" url="${ctxModule}/update/{selected}" />
				<ui:dgToolBar title="删除" type="select" onclick="del" />
				<ui:dgCol title="字典名称" field="label"></ui:dgCol>
				<ui:dgCol title="字典值" field="value"></ui:dgCol>
				<ui:dgCol title="字典类型" field="type"></ui:dgCol>
				<ui:dgCol title="描述" field="description"></ui:dgCol>
				<ui:dgCol title="创建者" field="createBy"></ui:dgCol>
				<ui:dgCol title="创建时间" field="createTime"></ui:dgCol>
				<ui:dgCol title="更新者" field="updateBy"></ui:dgCol>
				<ui:dgCol title="更新时间" field="updateTime"></ui:dgCol>
				<ui:dgCol title="备注信息" field="remarks"></ui:dgCol>
				<ui:dgCol title="删除标记" field="delFlag"></ui:dgCol>
			</ui:datagrid>
		</div>
	</div>
	
</body>
</html>
