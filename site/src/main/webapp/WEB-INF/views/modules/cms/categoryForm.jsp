<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>栏目管理</title>
<script type="text/javascript">
	$(document).ready(function() {
	});
</script>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">栏目编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}"
				method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">上级栏目：</label>
					<div class="col-md-6">
						<tags:treeselect id="category" name="parentId"
							value="${entity.parentId}" labelName="parentName"
							labelValue="${entity.parentName}" allowClear="false" title="栏目"
							url="/cms/category/treeData" extId="${entity.id}"
							cssClass="required" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">栏目名称：</label>
					<div class="col-md-6">
						<input name="name" maxlength="50" class="form-control required"
							value="${entity.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">栏目模型：</label>
					<div class="col-md-6">
						<form:select path="entity.module" class="form-control required">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('cms_module')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<!-- 
				<div class="form-group">
					<label class="col-md-2 control-label">image：</label>
					<div class="col-md-6">
						<input name="image" maxlength="50" class="form-control required"
							value="${entity.image}" />
					</div>
				</div>
				 -->
				<div class="form-group">
					<label class="col-md-2 control-label">链接：</label>
					<div class="col-md-6">
						<input name="href" maxlength="50" class="form-control"
							value="${entity.href}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">目标：</label>
					<div class="col-md-6">
						<input name="target" maxlength="50" class="form-control"
							value="${entity.target}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">描述：</label>
					<div class="col-md-6">
						<textarea name="description" rows="5" maxlength="200"
							class="form-control">
							${entity.description}
						</textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">关键字：</label>
					<div class="col-md-6">
						<input name="keywords" maxlength="50" class="form-control"
							value="${entity.keywords}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">排序：</label>
					<div class="col-md-6">
						<input name="sort" maxlength="50" class="form-control required"
							value="${entity.sort}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">在导航中显示：</label>
					<div class="col-md-6">
						<form:select path="entity.inMenu" class="form-control required">
							<form:options items="${fns:getDictList('show_hide')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">在分类页中显示：</label>
					<div class="col-md-6">
						<form:select path="entity.inList" class="form-control required">
							<form:options items="${fns:getDictList('show_hide')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">展现方式：</label>
					<div class="col-md-6">
						<form:select path="entity.showModes" class="form-control required">
							<form:options items="${fns:getDictList('cms_show_mode')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">是否允许评论：</label>
					<div class="col-md-6">
						<form:select path="entity.allowComment" class="form-control required">
							<form:options items="${fns:getDictList('yes_no')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">是否需要审核：</label>
					<div class="col-md-6">
						<form:select path="entity.isAudit" class="form-control required">
							<form:options items="${fns:getDictList('yes_no')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">自定义列表视图：</label>
					<div class="col-md-6">
						<input name="customListView" maxlength="50" class="form-control"
							value="${entity.customListView}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">自定义内容视图：</label>
					<div class="col-md-6">
						<input name="customContentView" maxlength="50"
							class="form-control" value="${entity.customContentView}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">自定义视图参数：</label>
					<div class="col-md-6">
						<input name="viewConfig" maxlength="50" class="form-control"
							value="${entity.viewConfig}" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="submit" class="btn btn-primary" value="保存" /> <a
							href="${ctxModule}" class="btn btn-default">返 回</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
