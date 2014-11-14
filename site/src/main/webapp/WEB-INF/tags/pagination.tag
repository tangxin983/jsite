<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="page" type="tangx.jsite.site.core.persistence.entity.Page" required="true"%>
<ul id="pagination"></ul>
<script type='text/javascript'>
	$(document).ready(function(){
		var options = {
		    bootstrapMajorVersion: 3,
		    currentPage: "${page.currentPage}",
		    totalPages: "${page.totalPage}",
		    tooltipTitles: function (type, page, current) {
                switch (type) {
	                case "first":
	                    return "首页";
	                case "prev":
	                    return "上一页";
	                case "next":
	                    return "下一页";
	                case "last":
	                    return "末页";
	                case "page":
	                    return "";
                }
            },
		     
		    pageUrl: function(type, page, current){
                return "?page=" + page + "&${searchParams}";
            }
		};
		if(<%=page.getTotalPage()%> > 1){
			$('#pagination').bootstrapPaginator(options);
			$('#pagination').append("<li class='disabled'><a>第${page.currentResult + 1} - ${page.currentEndResult}条记录 / 共${page.recordsTotal}条</a></li>");
		}else if(<%=page.getTotalPage()%> == 1){
			$('#pagination').addClass('pagination');
			$('#pagination').append("<li class='disabled'><a>第${page.currentResult + 1} - ${page.currentEndResult}条记录 / 共${page.recordsTotal}条</a></li>");
		}else{
			$('#pagination').addClass('pagination');
			$('#pagination').append("<li class='disabled'><a>共${page.recordsTotal}条记录</a></li>");
		}
	});
	
</script>