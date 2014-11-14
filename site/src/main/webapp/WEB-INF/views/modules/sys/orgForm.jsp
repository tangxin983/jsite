<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">机构编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">上级机构:</label>
					<div class="col-md-6">
						<tags:treeselect id="org" name="parentId"
							value="${entity.parentId}" labelName="parentName"
							labelValue="${entity.parentName}" allowClear="false" title="机构"
							url="/sys/org/treeData" extId="${entity.id}" cssClass="required" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">归属区域:</label>
					<div class="col-md-6">
						<tags:treeselect id="area" name="areaId"
							value="${entity.areaId}" labelName="areaName"
							labelValue="${entity.areaName}" allowClear="false" title="区域"
							url="/sys/area/treeData" cssClass="required" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">机构名称:</label>
					<div class="col-md-6">
						<input name="name" maxlength="50" class="form-control required"
							value="${entity.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">机构编码:</label>
					<div class="col-md-6">
						<input name="code" maxlength="50" class="form-control required"
							value="${entity.code}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">机构类型:</label>
					<div class="col-md-6">
						<input name="type" maxlength="50" class="form-control required"
							value="${entity.type}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">机构级别:</label>
					<div class="col-md-6">
						<input name="grade" maxlength="50" class="form-control required"
							value="${entity.grade}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">联系地址:</label>
					<div class="col-md-6">
						<input name="address" maxlength="50" class="form-control"
							value="${entity.address}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">邮政编码:</label>
					<div class="col-md-6">
						<input name="zipCode" maxlength="50" class="form-control"
							value="${entity.zipCode}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">负责人:</label>
					<div class="col-md-6">
						<input name="master" maxlength="50" class="form-control"
							value="${entity.master}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">电话:</label>
					<div class="col-md-6">
						<input name="phone" maxlength="50" class="form-control"
							value="${entity.phone}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">传真:</label>
					<div class="col-md-6">
						<input name="fax" maxlength="50" class="form-control"
							value="${entity.fax}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">邮箱:</label>
					<div class="col-md-6">
						<input name="email" maxlength="50" class="form-control email"
							value="${entity.email}" />
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
