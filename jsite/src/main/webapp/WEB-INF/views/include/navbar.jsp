<%@ page language="java" pageEncoding="UTF-8"%>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="row">
			<div class="navbar-header">
				<a class="navbar-brand" href="${ctx}">管理平台</a>
			</div>
			<shiro:user>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<shiro:principal /> <b class="caret"></b>
							</a>
							<ul class="dropdown-menu">
								<li>
									<a href="#"> <span class="glyphicon glyphicon-file"></span>用户设置</a>
								</li>
								<li class="divider"></li>
								<li>
									<a href="${ctx}/logout"> <span class="glyphicon glyphicon-log-out"></span> 退出系统</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</shiro:user>
		</div>
	</div>
</nav>