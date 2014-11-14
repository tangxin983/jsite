<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="cmsPublish" />
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
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">文章编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">categoryId：</label>
					<div class="col-md-6">
						<input name="categoryId" maxlength="50" class="form-control required"
							value="${entity.categoryId}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">title：</label>
					<div class="col-md-6">
						<input name="title" maxlength="50" class="form-control required"
							value="${entity.title}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">link：</label>
					<div class="col-md-6">
						<input name="link" maxlength="50" class="form-control required"
							value="${entity.link}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">color：</label>
					<div class="col-md-6">
						<input name="color" maxlength="50" class="form-control required"
							value="${entity.color}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">image：</label>
					<div class="col-md-6">
						<input name="image" maxlength="50" class="form-control required"
							value="${entity.image}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">keywords：</label>
					<div class="col-md-6">
						<input name="keywords" maxlength="50" class="form-control required"
							value="${entity.keywords}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">description：</label>
					<div class="col-md-6">
						<input name="description" maxlength="50" class="form-control required"
							value="${entity.description}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">weight：</label>
					<div class="col-md-6">
						<input name="weight" maxlength="50" class="form-control required"
							value="${entity.weight}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">weightDate：</label>
					<div class="col-md-6">
						<input name="weightDate" maxlength="50" class="form-control required"
							value="${entity.weightDate}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">hits：</label>
					<div class="col-md-6">
						<input name="hits" maxlength="50" class="form-control required"
							value="${entity.hits}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">posid：</label>
					<div class="col-md-6">
						<input name="posid" maxlength="50" class="form-control required"
							value="${entity.posid}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">customContentView：</label>
					<div class="col-md-6">
						<input name="customContentView" maxlength="50" class="form-control required"
							value="${entity.customContentView}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">viewConfig：</label>
					<div class="col-md-6">
						<input name="viewConfig" maxlength="50" class="form-control required"
							value="${entity.viewConfig}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">createBy：</label>
					<div class="col-md-6">
						<input name="createBy" maxlength="50" class="form-control required"
							value="${entity.createBy}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">createDate：</label>
					<div class="col-md-6">
						<input name="createDate" maxlength="50" class="form-control required"
							value="${entity.createDate}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">updateBy：</label>
					<div class="col-md-6">
						<input name="updateBy" maxlength="50" class="form-control required"
							value="${entity.updateBy}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">updateDate：</label>
					<div class="col-md-6">
						<input name="updateDate" maxlength="50" class="form-control required"
							value="${entity.updateDate}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">remarks：</label>
					<div class="col-md-6">
						<input name="remarks" maxlength="50" class="form-control required"
							value="${entity.remarks}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">delFlag：</label>
					<div class="col-md-6">
						<input name="delFlag" maxlength="50" class="form-control required"
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
