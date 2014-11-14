<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>我的首页</title>
<script type="text/javascript">
	$(document).ready(function() {
	});
</script>
</head>

<body>
	<tags:message content="${message}" />

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">快速开始</div>
		</div>
		<div class="panel-body ">
			<!-- 
			<div class="row">
				<div class="col-sm-6">
					<a href="/host/host/" class="btn btn-primary btn-quick">
					Host
					</a>
				</div>
				<div class="col-sm-6">
					<a href="/host/idc/" class="btn btn-primary btn-quick"> 
					IDC
					</a>
				</div>
			</div>
			 -->
		</div>
	</div>

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">个人信息</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctx}/sys/user/updateInfo"
				method="post">
				<input type="hidden" name="id" value="${shiroEntity.user.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">登录名:</label>
					<div class="col-md-6">
						<p class="form-control-static">${shiroEntity.user.loginName}</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">角色:</label>
					<div class="col-md-6">
						<p class="form-control-static">${fns:extractProperty(shiroEntity.user.roles, "name", true)}</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">姓名:</label>
					<div class="col-md-6">
						<input name="name" maxlength="50" class="form-control required"
							value="${shiroEntity.user.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">密码:</label>
					<div class="col-md-6">
						<input type="password" id="plainPassword" name="plainPassword"
							placeholder="不变请留空" maxlength="50" minlength="6"
							class="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">确认密码:</label>
					<div class="col-md-6">
						<input type="password" id="confirmPassword" name="confirmPassword"
							class="form-control" equalTo="#plainPassword" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">邮箱:</label>
					<div class="col-md-6">
						<input name="email" maxlength="100" class="form-control email"
							value="${shiroEntity.user.email}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">电话:</label>
					<div class="col-md-6">
						<input name="phone" maxlength="100" class="form-control"
							value="${shiroEntity.user.phone}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">手机:</label>
					<div class="col-md-6">
						<input name="mobile" maxlength="100" class="form-control"
							value="${shiroEntity.user.mobile}" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="submit" class="btn btn-primary" value="更新个人信息" /> 
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
