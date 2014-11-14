<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="entity" type="tangx.jsite.site.core.persistence.entity.WorkFlowEntity" required="true"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<div class="text-muted bootstrap-admin-box-title">流程历史记录</div>
	</div>
	<div class="panel-body">
		<c:if test="${not empty entity.historicTaskInstances}">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th>序号</th>
						<th>节点</th>
						<th>操作人</th>
						<th>创建时间</th>
						<th>结束时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>启动流程</td>
						<td>${fns:getUserById(entity.applyUser).name}</td>
						<td><fmt:formatDate
								value="${entity.applyTime}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><fmt:formatDate value="${entity.applyTime}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td></td>
					</tr>
					<c:forEach items="${entity.historicTaskInstances}"
						var="historicTaskInstance" varStatus="status">
						<tr>
							<td>${status.index + 2}</td>
							<td>${historicTaskInstance.name}</td>
							<td>${fns:getUserById(historicTaskInstance.assignee).name}</td>
							<td><fmt:formatDate
									value="${historicTaskInstance.startTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${historicTaskInstance.endTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${entity.commentMap[historicTaskInstance.id]}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</div>