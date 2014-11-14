//在页面加载完毕后设置jquery及其插件属性
(function($) {
	
	// jq ajax配置
	$.ajaxSetup({
		//ajax session过期时触发
		complete: function(xhr, textStatus) {
			if(xhr.getResponseHeader("sessionstatus")=="timeout"){  
		        if(xhr.getResponseHeader("loginPath")){  
		            window.location.replace(xhr.getResponseHeader("loginPath"));  
		        }else{  
		        	bootbox.alert("会话过期，请重新登录");  
		        }  
		    }
		},
		//ajax错误处理
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			if(XMLHttpRequest.responseText && XMLHttpRequest.responseText != ""){
				bootbox.alert(XMLHttpRequest.responseText);
				return false;
			}
		}
	});
	
	// 设置dataTable默认属性
	$.extend( $.fn.dataTable.defaults, {
		processing: true, // 当datatable获取数据时候是否显示正在处理提示信息
		autoWidth: true,// 列宽自适应
		lengthChange: true,// 允许自定义每页记录数
		searching: false,// 隐藏搜索框
		lengthMenu: [10, 20, 30],
	    language: {
	    	processing:   "处理中...",
	        lengthMenu:   "显示 _MENU_ 项结果",
	        zeroRecords:  "没有匹配结果",
	        info:         "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
	        infoEmpty:    "显示第 0 至 0 项结果，共 0 项",
	        infoFiltered: "(由 _MAX_ 项结果过滤)",
	        infoPostFix:  "",
	        search:       "搜索:",
	        emptyTable:     "表中数据为空",
	        loadingRecords: "载入中...",
	        thousands:  ",",
	        paginate: {
	            first:    "首页",
	            previous: "上页",
	            next:     "下页",
	            last:     "末页"
	        },
	        aria: {
	            sortAscending:  ": 以升序排列此列",
	            sortDescending: ": 以降序排列此列"
	        }
        }
	} );
	
	// 校验插件配置
	$.validator.setDefaults({
		showErrors : function(errorMap, errorList) {
			$.each(this.successList,function(i,e){
				var element = $(e);
				element.parent("div").removeClass("has-error");
				element.tooltip("destroy");
			});
			
			$.each(errorList, function(i, e) {
				var element = $(e.element);
				var tip = element.data('bs.tooltip');
				if ($.isNotEmpty(tip)) {
					tip.options.title = e.message;
					element.next().children(".tooltip-inner").html(e.message);
				} else {
					element.parent("div").addClass("has-error");
					element.tooltip({
						title:e.message,
						trigger:"focus"
					});
				}
			});
		}
	});
	
	// 对话框插件配置
	bootbox.setDefaults({
		locale: "zh_CN"
	});
	
})(jQuery);

// 页面加载完毕后执行初始化
$(document).ready(function(){
	initComponent();
});

// 初始化页面控件样式及行为
function initComponent(){
	
	// 表单校验、提交、reset
	$("form[valid!='false']").each(function(i,o){
		var temp = $(o);
		
		temp.find(":input:not(:disabled):not(:hidden)").first().focus();
		
		if ($.value(temp.attr("data-ajax-submit"),"false").booleanValue()) {
			// ajax提交
			temp.validate({
				submitHandler : function(form) {
					temp.ajaxSubmit();
				},
				ignore: ":hidden:not(.selectpicker)"
			});
		} else {
			// 普通提交
			temp.validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				ignore: ":hidden:not(.selectpicker)"
			});
		}
		
		temp.find("button[type='reset']").click(function(){
			
			temp.find("input:not(:disabled):not(:hidden)").each(function(i,field){
				$(field).val("");
			});
			
			temp.find("select:not(:disabled):not(:hidden)").each(function(i,field){
				$(field).val("");
			});
			
			temp.find("textarea:not(:disabled):not(:hidden)").each(function(i,field){
				$(field).val("");
			});
			
			return false;
		});
	});
	
	//下拉框
	$('.selectpicker').selectpicker();
	
	//全选
	if($("#selectAll")){
		$("#selectAll").click(function() {
			var checkbox = $(this), children = checkbox.parents("table").find("tbody input[type='checkbox']");
			children.prop("checked", checkbox.is(":checked"));
		});
	}
	//批量删除弹出框提示
	if($("#user-view-form") && $("#selectAll")){
		$("#user-view-form").submit(function(e) {
			var currentForm = this;
			e.preventDefault();
			if($(this).find("input:checked").length > 0){
				bootbox.confirm("确定要删除吗？", function(result) {
		            if (result) currentForm.submit();
		        });
			}
	    });
	}
	
	//表格行点击高亮（排除checkbox）
	$("[clickable='true']").each(function(i,o){
		$(o).css('cursor', 'pointer');
		$(o).on('click', 'tr', function(event) {
			if(event.target.type != 'checkbox'){
				$(this).addClass('highlight').siblings().removeClass('highlight');
			}
		});
	});
	
	// 边栏隐藏
    $('.hide-sidebar').click(function() {
	  $('#sidebar').hide('fast', function() {
	  	$('#mainContent').removeClass('col-md-10');
	  	$('#mainContent').addClass('col-md-12');
	  	$('.hide-sidebar').hide();
	  	$('.show-sidebar').show();
	  });
	});

    // 边栏显示
	$('.show-sidebar').click(function() {
		$('#mainContent').removeClass('col-md-12');
	   	$('#mainContent').addClass('col-md-10');
	   	$('.show-sidebar').hide();
	   	$('.hide-sidebar').show();
	  	$('#sidebar').show('fast');
	});
	
	// 边栏点击
	$("#sidebarMenu .panel-heading a").click(function(){
		$("#sidebarMenu .panel-heading a").children('span').removeClass('glyphicon-chevron-down');
		$("#sidebarMenu .panel-heading a").children('span').addClass('glyphicon-chevron-right');
		if(!$($(this).attr('href')).hasClass('in')){
			$(this).children('span').removeClass('glyphicon-chevron-right');
			$(this).children('span').addClass('glyphicon-chevron-down');
		}
	});
	$("#sidebarMenu .panel-body a").click(function(){
		$("#sidebarMenu .panel-body a").removeClass("active");
		$(this).addClass("active");
		localStorage.menuId = $(this).attr("id");
	});
}

//显示加载框
function loading(mess){
	top.$.jBox.tip.mess = null;
	top.$.jBox.tip(mess,'loading',{opacity:0});
}

/**
 * 确认对话框
 * @param mess 消息
 * @param href 要跳转的链接
 * @returns {Boolean}
 */
function confirmx(mess, href){
	bootbox.confirm(mess, function(result) {
        if (result) {
			location = href;
		}
    });
	return false;
}

/**
 * 确认对话框
 * @param mess 消息
 * @param func 要执行的函数
 * @returns {Boolean}
 */
function confirmx_func(mess, func){
	bootbox.confirm(mess, function(result) {
        if (result) {
			func();
		}
    });
	return false;
}