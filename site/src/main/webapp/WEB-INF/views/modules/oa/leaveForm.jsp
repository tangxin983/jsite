<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假管理</title>
	<!-- 这里引入额外的css和js 
	<link rel="stylesheet" type="text/css" href="" />
	<script type="text/javascript" src=""></script>
	-->
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function formSubmit() {
			$("#procType").val("leave1");
			$("#workflowForm").submit();
		}
	</script>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="text-muted bootstrap-admin-box-title">请假编辑</div>
		</div>
		<div class="panel-body">
			<form class="form-horizontal" action="${ctxModule}/${action}" method="post" id="workflowForm">
				<input type="hidden" name="id" value="${entity.id}">
				<!-- 测试用 -->
				<input type="hidden" id="procType" name="procType">
				<div class="form-group">
					<label class="col-md-2 control-label">请假类型：</label>
					<div class="col-md-6">
						<select name="leaveType" class="form-control required">
							<c:forEach items="${fns:getDictList('oa_leave_type')}" var="type">
								<option value="${type.value}" ${entity.leaveType eq type.value ? "selected" : ""}>${type.label}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">开始时间：</label>
					<div class="col-md-6">
						<input name="startTime" readonly="readonly" maxlength="20" class="form-control Wdate required"
							value="${entity.startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">结束时间：</label>
					<div class="col-md-6">
						<input name="endTime" readonly="readonly" maxlength="20" class="form-control Wdate required"
							value="${entity.endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">请假原因：</label>
					<div class="col-md-6">
						<textarea name="reason" rows="5" maxlength="255" class="form-control required">
						${entity.reason} 
						</textarea>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<input type="submit" class="btn btn-primary" value="保存" /> 
						<!-- 测试用 -->
						<input type="button" onclick="formSubmit()" class="btn btn-primary" value="启动leave1流程" /> 
						<a href="${ctxModule}" class="btn btn-default">返 回</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
