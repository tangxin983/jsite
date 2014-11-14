/**全局变量**/
var myimgmap = new imgmap({
	mode : "editor2",
	custom_callbacks : {
		'onHtmlChanged'   : function(str) {gui_htmlChanged(str);},//to display updated html on gui
		'onAddArea'       : function(id)  {gui_addArea(id);},//to add new form element on gui
		'onRemoveArea'    : function(id)  {gui_removeArea(id);},//to remove form elements from gui
		'onAreaChanged'   : function(obj) {gui_areaChanged(obj);},
		'onSelectArea'    : function(obj) {gui_selectArea(obj);}//to select form element when an area is clicked
	},
	lang : "en",
	label : '%a',
	pic_container: document.getElementById('pic_container'),
	bounding_box : false
});

var props = [];

/**
 * 加载图片
 * @param src 图片地址
 * @param mapCode 热区代码
 */
function gui_loadImage(src, mapCode) {
	// 将比例设置为100%
	$("#dd_zoom").val("1");
	// 删除已有图片
	var pic = document.getElementById('pic_container').getElementsByTagName('img')[0];
	if (typeof pic != 'undefined') {
		//delete already existing pic
		pic.parentNode.removeChild(pic);
		delete myimgmap.pic;
	}
	// 删除已有热区
	myimgmap.removeAllAreas();
	// 若有图片和热区则加载
	if(src && src != ""){
		myimgmap.loadImage(src);
		if(mapCode && mapCode != ""){
			myimgmap.setMapHTML(mapCode);
		}
	}  
}

/**
 * 图片放大缩小
 */
function gui_zoom() {
	var scale = $("#dd_zoom").val();
	if(scale == ''){
		return false;
	}
	if ($('#pic_container').children("img").length == 0) {return false;}
	var pic = $('#pic_container').children("img")[0];
	if (typeof pic.oldwidth == 'undefined' || !pic.oldwidth) {
		pic.oldwidth = pic.width;
	}
	if (typeof pic.oldheight == 'undefined' || !pic.oldheight) {
		pic.oldheight = pic.height;
	}
	pic.width  = pic.oldwidth * scale;
	pic.height = pic.oldheight * scale;
	myimgmap.scaleAllAreas(scale);
}

/**
 * 数据行选中高亮相应热区
 * @param id
 */
function gui_row_select(id) {
	if (myimgmap.is_drawing) {return;}//exit if in drawing state
	if (myimgmap.viewmode === 1) {return;}//exit if preview mode
	for (i=0; i<myimgmap.areas.length; i++) {
		myimgmap.blurArea(i);
	}
	myimgmap.highlightArea(id);
}

/**
 * 选中热区时高亮数据行
 * @param obj 热区
 */
function gui_selectArea(obj) {
	props[obj.aid].click();
}

/**
 * 添加热区时触发
 * @param id 热区id
 */
function gui_addArea(id) {
	props[id] = $("<tr>");
	props[id].prop('id','img_area_' + id);
	props[id].prop('aid',id);
	//var temp = '<td><div class="col-sm-3">' + id + '</div></td>';
	var temp = '<td class="col-sm-8"><div class="input-group"><input type="hidden" name="coord">';
	temp += '<input type="hidden" name="menuId"><input type="text" name="menuName" class="form-control" readonly>';
	temp += '<span class="input-group-btn"><a data-target="#menuModal" class="btn btn-default" data-toggle="modal">选择</a></span></td>';
	temp += '<td><a class="btn btn-danger" onclick="myimgmap.removeArea(' + id + ')"><span class="glyphicon glyphicon-remove"></span></a></td></div>';
	props[id].append(temp);
	props[id].on("click", function () {
		gui_row_select($(this).prop('aid'));
	});
	$('#bind-content').append(props[id]);
}

/**
 * 绑定商品时触发
 * @param obj 模态框按键
 * @param vals menuId,menuName
 */
function gui_input_change(obj, vals) {
	if (myimgmap.viewmode === 1) {return;}//exit if preview mode
	if (myimgmap.is_drawing) {return;}//exit if drawing
	var id = obj.closest("tr").prop('aid');
	if (myimgmap.areas[id]) {
		myimgmap.areas[id].aalt    = vals[1];
		myimgmap.areas[id].atitle  = vals[0] + "||" + vals[1];
		myimgmap._recalculate(id, obj.parent().parent().find("[name='coord']").val().replace(/;/g,','));
		myimgmap.fireEvent('onHtmlChanged', myimgmap.getMapHTML());
	}
}

/**
 * 删除热区时触发
 * @param id 热区id
 */
function gui_removeArea(id) {
	if (props[id]) {
		props[id].remove();
	}
}

/**
 * 热区改变时触发
 * @param area 热区对象
 */
function gui_areaChanged(area) {
	var id = area.aid;
	if (props[id]) {
		if (area.lastInput) {
			props[id].find("[name='coord']").val(area.lastInput.replace(/,/g,';'));
		}
		if (area.atitle && area.atitle != "") {
			var vals = area.atitle.split('||');
			props[id].find("[name='menuId']").val(vals[0]);
			props[id].find("[name='menuName']").val(vals[1]);
		}
	}
}

/**
 * 热区代码改变时触发
 * @param str 热区代码
 */
function gui_htmlChanged(str) {
	$('#mapCode').val(str);
}