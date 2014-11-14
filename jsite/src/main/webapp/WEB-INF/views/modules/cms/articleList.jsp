<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>内容发布</title>
<meta name="decorator" content="cmsPublish" />
<script type="text/javascript">
	function multiDel() {
		$("[name='ids']").each(function() {
			if ($(this).is(":checked")) {
				return confirmx_func('确定要删除选中的记录吗?', function() {
					$("#viewForm").submit();
				})
			}
		});
	}
</script>
</head>
<body>
	<tags:message content="${message}" />

	<!-- search form -->
	<nav class="navbar navbar-default">
		<form class="navbar-form navbar-left" valid="false">
			<div class="form-group col-md-3">
				<tags:treeselect id="category" name="s_categoryId"
					value="${category.id}" labelName="categoryName"
					labelValue="${category.name}" allowClear="false" title="栏目"
					url="/cms/category/treeData" />
			</div>
			<div class="form-group">
				<input name="s_title" value="${param.s_title}" class="form-control"
					placeholder="标题">
			</div>
			<button type="submit" class="btn btn-primary">
				<span class="glyphicon glyphicon-search"></span> 查询
			</button>
			<a href="${ctxModule}/create" class="btn btn-primary"> <span
				class="glyphicon glyphicon-plus"></span> 添加文章
			</a> <a onclick="multiDel()" class="btn btn-danger"> <span
				class="glyphicon glyphicon-remove"></span> 删除
			</a>
		</form>
	</nav>

	<!-- table -->
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">文章列表</div>
		</div>
		<div class="panel-body">
			<form id="viewForm" action="${ctxModule}/delete" valid="false"
				method="post">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th><input type="checkbox" id="selectAll"></th>
							<th>categoryId</th>
							<th>title</th>
							<!--
							<th>操作</th>
							-->
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.data}" var="entity">
							<tr>
								<td><input type="checkbox" name="ids" value="${entity.id}">
								</td>
								<td>${entity.category.name}</td>
								<td><shiro:hasPermission name="cms:article:edit">
										<a href="${ctxModule}/update/${entity.id}" title="修改">
											${entity.title} </a>
									</shiro:hasPermission> <shiro:lacksPermission name="cms:article:edit">
											${entity.title}
											</shiro:lacksPermission></td>
								<!--
								<td>
									<a href="${ctxModule}/update/${entity.id}" class="btn btn-default" title="修改">
										<span class="glyphicon glyphicon-edit"></span>
									</a>
								    <a href="${ctxModule}/delete/${entity.id}" class="btn btn-danger" title="删除"
								    	onclick="return confirmx('确定要删除吗?', this.href)">
								    	<span class="glyphicon glyphicon-remove"></span>
								    </a> 
								</td>
								-->
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<tags:pagination page="${page}" />
		</div>
	</div>
</body>
</html>
