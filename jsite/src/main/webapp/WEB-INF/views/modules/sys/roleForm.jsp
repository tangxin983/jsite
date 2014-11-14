<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>角色管理</title>
<%@ include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript">
	var tree;

	function formSubmit(){
		var ids = [], nodes = tree.getCheckedNodes(true);
		for (var i = 0; i < nodes.length; i++) {
			ids.push(nodes[i].id);
		}
		$("#menuIds").val(ids);
		$("#inputForm").submit();
	}

	$(document).ready(function() {
		$("#roleName").rules("add", {
			remote : {
				url : "${ctxModule}/checkName",
				type : "POST",
				data : {oldName : "${entity.name}"}
			}
		});
		$("#roleEnName").rules("add", {
			remote : {
				url : "${ctxModule}/checkEnName",
				type : "POST",
				data : {oldName : "${entity.enName}"}
			}
		});
		//<c:if test="${not empty entity.dataScope}">
		$('#dataScope').selectpicker('val', "${entity.dataScope}");
		//</c:if>
		var setting = {
			check : {
				enable : true,
				nocheckInherit : true
			},
			view : {
				selectedMulti : false
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeClick : function(id, node) {
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}
			}
		};
		$.get("${ctx}/sys/menu/treeData", function(zNodes){
			// 初始化树结构
			tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			// 默认展开全部节点
			tree.expandAll(true);
			// 默认选择节点
			//<c:forEach var="menu" items="${entity.menus}">
			var node = tree.getNodeByParam("id", "${menu.id}");
			tree.checkNode(node, true, false);
			//</c:forEach>
		});
	});
</script>
</head>
<body>
	<tags:message content="${message}" />
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">角色编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" id="inputForm"
				action="${ctxModule}/${action}" method="post">
				<input type="hidden" name="id" value="${entity.id}">
				<div class="form-group">
					<label class="col-md-2 control-label">角色名称:</label>
					<div class="col-md-6">
						<input id="roleName" name="name" maxlength="50" class="form-control required"
							value="${entity.name}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">角色英文:</label>
					<div class="col-md-6">
						<input id="roleEnName" name="enName" maxlength="50" class="form-control required"
							value="${entity.enName}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">数据范围:</label>
					<div class="col-md-6">
						<select id="dataScope" name="dataScope"
							class="selectpicker required form-control">
							<option value="1">所有数据</option>
							<option value="0">所在公司及以下数据</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">角色授权:</label>
					<div class="col-md-6">
						<div id="menuTree" class="ztree"
							style="margin-top: 3px; float: left;"></div>
						<input type="hidden" id="menuIds" name="menuIds">
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<a onclick="formSubmit();" class="btn btn-primary">保存</a>
						<a href="${ctxModule}" class="btn btn-default">返 回</a>
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
</html>