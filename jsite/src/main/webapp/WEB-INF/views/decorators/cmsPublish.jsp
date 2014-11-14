<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sitemesh:title /></title>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/treeview.jsp"%>
<sitemesh:head />
</head>
<body>
	<!-- navbar begin -->
	<%@ include file="/WEB-INF/views/include/navbar.jsp"%>
	<!-- navbar end -->
	<div class="container">
		<!--[if lt IE 9]>
		<div class="row">
			<div class="col-md-12">
				<div class="alert alert-warning alert-dismissable">
				  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
				  <strong>温馨提示：</strong>
				  <p>
					您使用的浏览器可能会导致页面显示异常。为了获得更好的浏览体验，强烈建议您升级到最新版本的
					<a href="http://windows.microsoft.com/ie" target="_blank">IE</a>，
					<a href="https://chrome.google.com" target="_blank">Chrome</a>或者
					<a href="https://mozilla.org/firefox" target="_blank">Firefox</a>。
					IE9以上版本请关闭“兼容性视图”。      
				  </p>
				</div>
			</div>
		</div>
		<![endif]-->
		<div class="row">
			<shiro:user>
				<!-- sidebar begin -->
				<div class="col-md-2 bootstrap-admin-col-left" id="sidebar">
					<%@ include file="/WEB-INF/views/include/sidebar.jsp"%>
				</div>
				<!-- sidebar end -->
				<div class="col-md-10" id="mainContent">
					<ol class="breadcrumb" id="breadcrumb">
						<a href="#"> <span
							class="glyphicon glyphicon-chevron-left hide-sidebar"
							title="隐藏侧边栏"></span>
						</a>
						<a href="#"> <span
							class="glyphicon glyphicon-chevron-right show-sidebar"
							title="显示侧边栏" style="display: none;"></span>
						</a>
					</ol>
					<div class="row">
						<div class="col-md-3">
							<!-- 栏目树 -->
							<div class="panel panel-default">
								<div class="panel-heading">
									<div class="text-muted bootstrap-admin-box-title">栏目列表</div>
								</div>
								<div class="panel-body">
									<div id="cTree" class="ztree"></div>
								</div>
							</div>
						</div>
						<div class="col-md-9">
							<!-- content begin -->
							<sitemesh:body />
							<!-- content end -->
						</div>
					</div>
				</div>
			</shiro:user>
			<shiro:guest>
		   		<sitemesh:body />	
			</shiro:guest>
		</div>
	</div>
	 
	<!-- footer -->
	<%@ include file="/WEB-INF/views/include/footer.jsp"%>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		if(window.location.pathname.split("/")[window.location.pathname.split("/").length -1] == "index"){
			$("#breadcrumb").append("<li class=\"active\">我的首页</li>");
		}else if(localStorage.menuId){
			// 保持边栏状态
			var pMenuId = $("#" + localStorage.menuId).parent().parent().attr("id");
			$("[href='#" + pMenuId + "']").click();
			$("#" + localStorage.menuId).click();
			$.get("${ctx}/breadcrumb?id=" + localStorage.menuId,function(resp) {
				if (resp && resp != "") {
					$("#breadcrumb").append(resp);
				}
			});
		}
		
		var setting = {view : {selectedMulti : false},data : {simpleData : {enable : true}}};
		$.get("${ctx}/cms/category/treeData", function(zNodes) {
			for (var i = 0; i < zNodes.length; i++) {
				// 移除顶级节点
				if (zNodes[i] && zNodes[i].id == 1) {
					zNodes.splice(i, 1);
				}
				// 添加url
				zNodes[i].url = "${ctx}/cms/" + zNodes[i].type + "?s_categoryId=" + zNodes[i].id;
				zNodes[i].target = "_self";
			}
			// 初始化树结构
			var tree = $.fn.zTree.init($("#cTree"), setting, zNodes);
			// 默认展开全部节点
			tree.expandAll(true);
			//<c:if test="${not empty param.s_categoryId}">
			var node = tree.getNodeByParam("id", "${param.s_categoryId}");
			tree.selectNode(node);
			//</c:if>
		});
	});
</script>
</html>