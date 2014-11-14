<%@ page language="java" pageEncoding="UTF-8"%>
<div class="panel-group" id="sidebarMenu">
	<c:forEach items="${shiroEntity.menus}" var="pmenu">
		<div class="panel panel-default">
			<div class="panel-heading">
				<a data-toggle="collapse" data-parent="#sidebarMenu" href="#collapse${pmenu.id}">
					<span class="glyphicon glyphicon-chevron-right"></span> ${pmenu.name}
				</a>
			</div>
			<div id="collapse${pmenu.id}" class="list-group panel-collapse collapse">
				<div class="panel-body">
					<c:forEach items="${pmenu.children}" var="menu">
						<a href="${ctx}${menu.href}" class="list-group-item" id="${menu.id}"> 
							<c:if test="${not empty menu.icon}">
								<span class="glyphicon glyphicon-${menu.icon}"></span>
							</c:if>
							${menu.name}
						</a>
					</c:forEach>
				</div>
			</div>
		</div>
	</c:forEach>
</div>