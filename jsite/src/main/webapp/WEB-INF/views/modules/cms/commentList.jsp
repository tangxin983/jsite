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
			location.href = "${ctxModule}/delete/" + $("#commentTableSelectedId").val();
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
				<input name="eq_categoryId" class="form-control" placeholder="栏目编号">
			</div>
			<div class="form-group">
				<input name="eq_contentId" class="form-control" placeholder="栏目内容的编号">
			</div>
			<div class="form-group">
				<input name="eq_title" class="form-control" placeholder="栏目内容的标题">
			</div>
			<div class="form-group">
				<input name="eq_content" class="form-control" placeholder="评论内容">
			</div>
			<div class="form-group">
				<input name="eq_name" class="form-control" placeholder="评论姓名">
			</div>
			<div class="form-group">
				<input name="eq_ip" class="form-control" placeholder="评论IP">
			</div>
			<div class="form-group">
				<input name="eq_createDate" class="form-control" placeholder="评论时间">
			</div>
			<div class="form-group">
				<input name="eq_auditUserId" class="form-control" placeholder="审核人">
			</div>
			<div class="form-group">
				<input name="eq_auditDate" class="form-control" placeholder="审核时间">
			</div>
			<div class="form-group">
				<input name="eq_delFlag" class="form-control" placeholder="删除标记">
			</div>
			<div class="form-group">
				<input name="eq_salary" class="form-control" placeholder="">
			</div>
			<div class="form-group">
				<input name="eq_age" class="form-control" placeholder="">
			</div>
		</form>
	</nav>

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">评论列表</div>
		</div>
		<div class="panel-body">
			<!-- table -->
			<ui:datagrid id="commentTable" title="评论列表" serverSide="true"
				queryUrl="${ctxModule}?datagrid" css="table table-striped table-hover">
				<ui:dgToolBar title="查询" script="commentTable.draw();" />
				<ui:dgToolBar title="添加" url="${ctxModule}/create" />
				<ui:dgToolBar title="编辑" type="select" url="${ctxModule}/update/{selected}" />
				<ui:dgToolBar title="删除" type="select" onclick="del" />
				<ui:dgCol title="栏目编号" field="categoryId"></ui:dgCol>
				<ui:dgCol title="栏目内容的编号" field="contentId"></ui:dgCol>
				<ui:dgCol title="栏目内容的标题" field="title"></ui:dgCol>
				<ui:dgCol title="评论内容" field="content"></ui:dgCol>
				<ui:dgCol title="评论姓名" field="name"></ui:dgCol>
				<ui:dgCol title="评论IP" field="ip"></ui:dgCol>
				<ui:dgCol title="评论时间" field="createDate"></ui:dgCol>
				<ui:dgCol title="审核人" field="auditUserId"></ui:dgCol>
				<ui:dgCol title="审核时间" field="auditDate"></ui:dgCol>
				<ui:dgCol title="删除标记" field="delFlag"></ui:dgCol>
				<ui:dgCol title="" field="salary"></ui:dgCol>
				<ui:dgCol title="" field="age"></ui:dgCol>
			</ui:datagrid>
		</div>
	</div>
	
</body>
</html>
