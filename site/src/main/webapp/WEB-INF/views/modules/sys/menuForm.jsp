<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>菜单管理</title>
<script type="text/javascript">
	$(document).ready(function() {
		//<c:if test="${not empty entity.icon}">
		$('#icon').selectpicker('val', "${entity.icon}");
		//</c:if>
		//<c:if test="${not empty entity.isShow}">
		$('#isShow').selectpicker('val', "${entity.isShow}");
		//</c:if>
		$("[name='href']").rules("add", {
			remote : {
				url : "${ctxModule}/checkHref",
				type : "POST",
				data : {old : "${entity.href}"}
			}
		});
	});
</script>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">菜单编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" id="inputForm"
				action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">上级菜单:</label>
					<div class="col-md-6">
						<tags:treeselect id="menu" name="parentId"
							value="${entity.parentId}" labelName="parentName"
							labelValue="${entity.parentName}" allowClear="false" title="菜单"
							url="/sys/menu/treeData" extId="${entity.id}" cssClass="required" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">名称:</label>
					<div class="col-md-6">
						<input name="name" maxlength="50" class="form-control required"
							value="${entity.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">链接:</label>
					<div class="col-md-6">
						<input name="href" maxlength="200" class="form-control"
							value="${entity.href}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">权限标识:</label>
					<div class="col-md-6">
						<input name="permission" maxlength="200" class="form-control"
							value="${entity.permission}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">目标:</label>
					<div class="col-md-6">
						<input name="target" maxlength="10" class="form-control"
							value="${entity.target}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">图标:</label>
					<div class="col-md-6">
						<tags:iconselect name="icon" value="${entity.icon}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">排序:</label>
					<div class="col-md-6">
						<input name="sort" maxlength="10"
							class="form-control required digits" value="${entity.sort}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">可见:</label>
					<div class="col-md-6">
						<select id="isShow" name="isShow" class="selectpicker required form-control">
							<option value="1">显示</option>
							<option value="0">隐藏</option>				 
						</select>
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