<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionName}管理</title>
	<!-- 这里引入额外的css和js 
	<link rel="stylesheet" type="text/css" href="" />
	<script type="text/javascript" src=""></script>
	-->
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body>
	<tags:message content="${r"${message}"}" />

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">${functionName}编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${r"${ctxModule}"}/${r"${action}"}" method="post">
				<input type="hidden" name="id" value="${r"${entity.id}"}">
				<#list entityFields as field>
				<div class="form-group">
					<label class="col-md-2 control-label">${field.colRemark}：</label>
					<div class="col-md-6">
						<#if field.type == 'Date'>
						<input name="${field.name}" readonly="readonly" class="form-control Wdate <#if field.notNull == '1'>required</#if>"
							value="<fmt:formatDate value="${r"${entity."}${field.name}}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
						<#else>
						<input name="${field.name}" maxlength="20" class="form-control<#if field.notNull == '1'> required</#if><#if field.type == 'Byte' || field.type == 'Short' || field.type == 'Integer' || field.type == 'Long'> digits</#if><#if field.type == 'Float' || field.type == 'Double'> number</#if>"
							value="${r"${entity."}${field.name}}" />
						</#if>
					</div>
				</div>
				</#list>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="submit" class="btn btn-primary" value="保存" /> 
						<a href="${r"${ctxModule}"}" class="btn btn-default">返 回</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
