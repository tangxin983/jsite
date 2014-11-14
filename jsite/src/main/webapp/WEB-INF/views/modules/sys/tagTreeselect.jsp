<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/treeview.jsp" %>
<script type="text/javascript">
	var nodeList = [];
	var setting = {
		view: {
			selectedMulti: false,
			fontCss:function(treeId, treeNode) {
				return (!!treeNode.highlight) ? {"font-weight":"bold"} : {"font-weight":"normal"};
			}
		},
		check:{
			enable:"${checked}",
			nocheckInherit:true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			beforeClick:function(id, node){
				if("${checked}" == "true"){
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}
			}
		}
	};
	$(document).ready(function(){
		$.get("${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}extId=${extId}&t="+new Date().getTime(), function(zNodes){
			// 初始化树结构
			tree = $.fn.zTree.init($("#${id}Tree"), setting, zNodes);
			
			// 默认展开一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, false, false);
			}
			// 默认选择节点
			var ids = "${selectIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				if("${checked}" == "true"){
					try{tree.checkNode(node, true, true);}catch(e){}
					tree.selectNode(node, false);
				}else{
					tree.selectNode(node, true);
				}
			}
		});
	});
  
	function searchNode() {
		// 取得输入的关键字的值
		var value = $.trim($("#${id}Key").val());
		
		// 如果要查空字串，就退出不查了。
		if (value === "") {
			return;
		}
		updateNodes(false);
		nodeList = tree.getNodesByParamFuzzy("name", value);
		updateNodes(true);
	}
	
	function updateNodes(highlight) {
		for(var i=0, l=nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;				
			tree.updateNode(nodeList[i]);
			tree.expandNode(nodeList[i].getParentNode(), true, false, false);
		}
	}
</script>
<div class="input-group">
    <input class="form-control" id="${id}Key" name="key" maxlength="50" placeholder="关键字">
    <span class="input-group-btn">
		<a class="btn btn-default" onclick="searchNode();">
			<span class="glyphicon glyphicon-search">
		</a>
	</span>
</div>
<div id="${id}Tree" class="ztree" style="padding:15px 20px;"></div>
