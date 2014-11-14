<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
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
			<div class="text-muted bootstrap-admin-box-title">区域编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">上级区域:</label>
					<div class="col-md-6">
						<tags:treeselect id="area" name="parentId"
							value="${entity.parentId}" labelName="parentName"
							labelValue="${entity.parentName}" allowClear="false" title="区域"
							url="/sys/area/treeData" extId="${entity.id}" cssClass="required" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">区域名称:</label>
					<div class="col-md-6">
						<input name="name" maxlength="50" class="form-control required"
							value="${entity.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">区域编码:</label>
					<div class="col-md-6">
						<input name="code" maxlength="50" class="form-control required"
							value="${entity.code}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">区域类型:</label>
					<div class="col-md-6">
						<input name="type" maxlength="50" class="form-control required"
							value="${entity.type}" />
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
