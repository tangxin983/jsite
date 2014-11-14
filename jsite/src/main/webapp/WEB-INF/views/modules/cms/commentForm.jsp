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
		$(document).ready(function() {
		});
	</script>
</head>
<body>
	<tags:message content="${message}" />

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">评论编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">栏目编号：</label>
					<div class="col-md-6">
						<input name="categoryId" maxlength="20" class="form-control required"
							value="${entity.categoryId}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">栏目内容的编号：</label>
					<div class="col-md-6">
						<input name="contentId" maxlength="20" class="form-control required"
							value="${entity.contentId}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">栏目内容的标题：</label>
					<div class="col-md-6">
						<input name="title" maxlength="20" class="form-control"
							value="${entity.title}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">评论内容：</label>
					<div class="col-md-6">
						<input name="content" maxlength="20" class="form-control"
							value="${entity.content}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">评论姓名：</label>
					<div class="col-md-6">
						<input name="name" maxlength="20" class="form-control"
							value="${entity.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">评论IP：</label>
					<div class="col-md-6">
						<input name="ip" maxlength="20" class="form-control"
							value="${entity.ip}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">评论时间：</label>
					<div class="col-md-6">
						<input name="createDate" readonly="readonly" class="form-control Wdate required"
							value="<fmt:formatDate value="${entity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">审核人：</label>
					<div class="col-md-6">
						<input name="auditUserId" maxlength="20" class="form-control"
							value="${entity.auditUserId}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">审核时间：</label>
					<div class="col-md-6">
						<input name="auditDate" readonly="readonly" class="form-control Wdate "
							value="<fmt:formatDate value="${entity.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">删除标记：</label>
					<div class="col-md-6">
						<input name="delFlag" maxlength="20" class="form-control required"
							value="${entity.delFlag}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">：</label>
					<div class="col-md-6">
						<input name="salary" maxlength="20" class="form-control required number"
							value="${entity.salary}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">：</label>
					<div class="col-md-6">
						<input name="age" maxlength="20" class="form-control required digits"
							value="${entity.age}" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="submit" class="btn btn-primary" value="保存" /> 
						<a href="${ctxModule}" class="btn btn-default">返 回</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
