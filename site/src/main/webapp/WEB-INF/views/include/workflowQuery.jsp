<%@ page language="java" pageEncoding="UTF-8"%>
<nav class="navbar navbar-default">
	<form class="navbar-form navbar-left" valid="false">
		<div class="form-group">流程名称：</div>
		<div class="form-group">
			<select name="s_processDefinitionId" class="form-control">
				<option value=""
					${param.s_processDefinitionId eq "" ? "selected" : ""}>全部</option>
				<c:forEach items="${processDefinitionList}" var="processDefinition">
					<option value="${processDefinition.id}"
						${param.s_processDefinitionId eq processDefinition.id ? "selected" : ""}>
						${processDefinition.name}(版本:${processDefinition.version})</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">单据号：</div>
		<div class="form-group">
			<input name="s_businessKey" value="${param.s_businessKey}"
				class="form-control" />
		</div>
		<div class="form-group">到达时间：</div>
		<div class="form-group">
			<input name="s_taskCreateTime1" value="${param.s_taskCreateTime1}" class="form-control Wdate" 
					readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
		</div>
		<div class="form-group">
			<input name="s_taskCreateTime2" value="${param.s_taskCreateTime2}" class="form-control Wdate" 
					readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
		</div>
		<button type="submit" class="btn btn-primary">
			<span class="glyphicon glyphicon-search"></span> 查询
		</button>
	</form>
</nav>

