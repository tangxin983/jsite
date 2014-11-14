<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<div class="modal fade" id="${id}Modal" aria-hidden="true">
	<div class="modal-dialog" style="width:320px;height:400px;">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="customerModalLabel">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					选择${title}
				</h4>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="${id}Confirm">确定</button>
				<c:if test="${allowClear}">
				<button type="button" class="btn btn-danger" id="${id}Clean">清除</button>
				</c:if>
				<button type="button" class="btn btn-default" data-dismiss="modal" id="${id}Close">关闭</button>
			</div>
		</div>
	</div>
</div>
<div class="input-group">
	<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}" 
		${disabled eq 'true' ? ' disabled=\'disabled\'' : ''}/>
	<input id="${id}Name" name="${labelName}" readonly type="text" class="form-control ${cssClass}" value="${labelValue}" 
		${disabled eq 'true' ? ' disabled=\'disabled\'' : ''}/>
	<span class="input-group-btn">
		<a id="${id}Button" class="btn btn-default${disabled eq 'true' ? ' disabled' : ''}">
			<span class="glyphicon glyphicon-search">
		</a>
	</span>
</div>
<script type="text/javascript">
	$("#${id}Button").click(function(){
		// 是否限制选择，如果限制则直接返回
		if ($("#${id}Id").attr("disabled")){
			return false;
		}
		var url = "${ctx}/sys/tag/treeselect?url="+encodeURIComponent("${url}")
			+"&checked=${checked}&extId=${extId}&id=${id}&selectIds="+$("#${id}Id").val();
        var modal = $('#${id}Modal'), modalBody = $('#${id}Modal .modal-body');
        modal.on('show.bs.modal', function () {
        	modalBody.load(url);
        }).modal();
	});
	
	$("#${id}Confirm").click(function(){
		var tree = $.fn.zTree.getZTreeObj("${id}Tree");
		var ids = [], names = [], nodes = [];
		if ("${checked}" == "true"){
			nodes = tree.getCheckedNodes(true);
		}else{
			nodes = tree.getSelectedNodes();
		}
		for(var i=0; i<nodes.length; i++) {
			//<c:if test="${checked}">
			if (nodes[i].isParent){
				continue;// 如果为复选框选择，则过滤掉父节点
			}
			//</c:if>
			//<c:if test="${notAllowSelectRoot}">
			if (nodes[i].level == 0){
				top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
				return false;
			}
			//</c:if>
			//<c:if test="${notAllowSelectParent}">
			if (nodes[i].isParent){
				top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
				return false;
			}
			//</c:if>
			ids.push(nodes[i].id);
			names.push(nodes[i].name);
			//<c:if test="${!checked}">
			break; // 如果为非复选框选择，则返回第一个选择  
			//</c:if>
		}
		$("#${id}Id").val(ids);
		$("#${id}Name").val(names);
		$('#${id}Close').click();
	});
	
	//<c:if test="${allowClear}">
	$("#${id}Clean").click(function(){
		$("#${id}Id").val("");
		$("#${id}Name").val("");
		$('#${id}Close').click();
	});
	//</c:if>
</script>
