<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>部署流程</title>
</head>
<body>
	<tags:message content="${message}" />

	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">部署流程</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" valid="false"
				action="${ctx}/workflow/process/deploy" method="post"
				enctype="multipart/form-data">
				<div class="form-group">
					<label class="col-md-2 control-label">部署流程：</label>
					<div class="col-md-6">
						<input type="file" name="file" />
						<input type="submit" value="Submit" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
