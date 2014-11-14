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
		$(document).ready(function() {
		});
	</script>
</head>
<body>
	<tags:message content="${message}" />

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">字典编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">字典名称：</label>
					<div class="col-md-6">
						<input name="label" maxlength="20" class="form-control required"
							value="${entity.label}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">字典值：</label>
					<div class="col-md-6">
						<input name="value" maxlength="20" class="form-control required"
							value="${entity.value}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">字典类型：</label>
					<div class="col-md-6">
						<input name="type" maxlength="20" class="form-control required"
							value="${entity.type}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">描述：</label>
					<div class="col-md-6">
						<input name="description" maxlength="20" class="form-control required"
							value="${entity.description}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">创建者：</label>
					<div class="col-md-6">
						<input name="createBy" maxlength="20" class="form-control"
							value="${entity.createBy}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">创建时间：</label>
					<div class="col-md-6">
						<input name="createTime" readonly="readonly" class="form-control Wdate "
							value="${entity.createTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">更新者：</label>
					<div class="col-md-6">
						<input name="updateBy" maxlength="20" class="form-control"
							value="${entity.updateBy}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">更新时间：</label>
					<div class="col-md-6">
						<input name="updateTime" readonly="readonly" class="form-control Wdate "
							value="${entity.updateTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">备注信息：</label>
					<div class="col-md-6">
						<input name="remarks" maxlength="20" class="form-control"
							value="${entity.remarks}" />
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
