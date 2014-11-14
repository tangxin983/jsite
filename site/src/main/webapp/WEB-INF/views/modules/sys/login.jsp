<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>登录页</title>
<script type="text/javascript">
	function reloadCaptcha() {
		$("#captchaImg").attr("src", "getCaptcha");
	}
</script>
</head>

<body>
	<form action="${ctx}/login" method="post" class="bootstrap-admin-login-form">
		<c:if test="${!empty shiroLoginFailure}">
			<div class="alert alert-danger fade in">
				<a class="close" data-dismiss="alert">&times;</a>
				${shiroLoginFailure}
			</div>
		</c:if>
		<legend>
			<span style="color: #08c;">系统登陆</span>
		</legend>
		<div class="form-group">
			<input class="form-control required" placeholder="登录名"
				name="username" value="${username}">
		</div>
		<div class="form-group">
			<input type="password" class="form-control required" placeholder="密码"
				name="password">
		</div>
		<div class="form-group">
			<select class="form-control" name="rememberMe">
				<option value="">无</option>
				<option value="25200">记住一周</option>
				<option value="2592000">记住一个月</option>
				<option value="31536000">记住一年</option>
			</select>
		</div>
		<c:if test="${!empty showCaptcha and showCaptcha}">
			<div class="form-group">
				<div class="row">
					<div class="col-md-8">
						<input class="form-control required" name="captcha"
							placeholder="请输入验证码" id="captcha">
					</div>
					<div class="col-md-2">
						<img id="captchaImg" src="getCaptcha"
							onclick="javascript:reloadCaptcha();" />
					</div>
				</div>
			</div>
		</c:if>
		<button class="btn btn-lg btn-primary" type="submit">登录</button>
	</form>
</body>
</html>


