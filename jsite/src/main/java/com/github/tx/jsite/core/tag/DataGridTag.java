package com.github.tx.jsite.core.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 数据列表标签
 * 
 * @author tangx
 * @since 2014年10月16日
 */
public class DataGridTag extends SimpleTagSupport {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String id;// 表格id

	private String queryUrl;// 查询url

	private String title;// 列表标题
	
	private boolean serverSide = true;// 是否服务端分页

	private String css;// 表格css

	private String width;// 表格宽度

	private String height;// 表格高度
	
	private boolean lengthChange = true;// 是否允许修改每页记录数(默认允许)
	
	private boolean ordering = true;// 是否允许排序
	
	private boolean selected = true;// 是否允许选中行
	
	private String paraFuncName = "manipulateParam";
	
	private String fnRowSelected;

	private List<DataGridColumnTag> columnList = Lists.newArrayList();// 列表集合
	
	private List<DataGridToolBarTag> toolBarList = Lists.newArrayList();// 工具栏集合
	

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public String getQueryUrl() {
		return queryUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getCss() {
		return css;
	}

	public String getWidth() {
		return width;
	}

	public String getHeight() {
		return height;
	}

	public boolean isServerSide() {
		return serverSide;
	}

	public void setServerSide(boolean serverSide) {
		this.serverSide = serverSide;
	}

	public boolean isLengthChange() {
		return lengthChange;
	}

	public void setLengthChange(boolean lengthChange) {
		this.lengthChange = lengthChange;
	}
	
	public boolean isOrdering() {
		return ordering;
	}

	public void setOrdering(boolean ordering) {
		this.ordering = ordering;
	}

	public String getParaFuncName() {
		return paraFuncName;
	}

	public void setParaFuncName(String paraFuncName) {
		this.paraFuncName = paraFuncName;
	}

	public String getFnRowSelected() {
		return fnRowSelected;
	}

	public void setFnRowSelected(String fnRowSelected) {
		this.fnRowSelected = fnRowSelected;
	}

	/**
	 * 添加列
	 */
	public void addColumn(DataGridColumnTag column) {
		columnList.add(column);
	}
	
	/**
	 * 添加工具栏
	 */
	public void addToolBar(DataGridToolBarTag toolBar) {
		toolBarList.add(toolBar);
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspBody().invoke(null);
		getJspContext().getOut().print(dataTables()); 
	}

	public String dataTables() {
		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\">");
		sb.append("$(document).ready(function() {");
		sb.append("var selected = '';");
		/*********dataTable初始化开始**********/
		sb.append("var " + this.getId() + " = $('#" + this.getId() + "').DataTable({");
		sb.append("\"ajax\" : {\"url\" : \"" + this.getQueryUrl() + "\",");// 查询url
		sb.append("\"data\" : function(d){" + this.getParaFuncName() + "(d);}"); // 获取查询参数
		sb.append("},"); 
		sb.append("\"serverSide\" : " + this.isServerSide() + ",");// 是否从服务器端分页获取数据
		sb.append("\"lengthChange\" : " + this.isLengthChange() + ",");// 是否允许修改每页记录数
		sb.append("\"ordering\" : " + this.isOrdering() + ",");// 是否允许排序
		sb.append("\"order\" : [],");// 初始无排序
		/*********翻页也可以记住选中行开始**********/
		if(this.isSelected()){
			sb.append("\"rowCallback\" : function( row, data ){");// 
			sb.append("if ( selected != '' && selected == data.DT_RowId ) {");
			sb.append("var oTT = TableTools.fnGetInstance( '" + this.getId() + "' );");
			sb.append("oTT.fnSelect(row);");
			sb.append("}},");
		}
		/*********翻页也可以记住选中行结束**********/
		/*********列定义开始**********/
		sb.append("\"columns\" : [ ");
		int i = 0;
		for (DataGridColumnTag column : columnList) {
			i++;
			sb.append("{");
			sb.append("data:\"" + column.getField() + "\",");
			sb.append("className:\"col-" + column.getAlign() + "\",");
			sb.append("visible:" + !column.isHidden() + ",");
			sb.append("orderable:" + column.isOrderable());
			sb.append("}");
			if (i < columnList.size())
				sb.append(",");
		}
		/*********列定义结束**********/
		sb.append("]" + "});");
		/*********dataTable初始化结束**********/
		/*********存储选中行的id开始**********/
		if(this.isSelected()){
			sb.append("$('#" + this.getId() + " tbody').on('click', 'tr', function () {");
			sb.append("selected = this.id;");
			sb.append("$('#" + this.getId() + "SelectedId').val(this.id);");
			sb.append("} );");
		}
		/*********存储选中行的id结束**********/
		/*********TableTool初始化开始**********/
		sb.append("var tt = new $.fn.dataTable.TableTools( " + this.getId() + ",{");
		if(this.isSelected()){
			sb.append("\"sRowSelect\":\"single\",");//选择行
		}
		if(this.isSelected() && StringUtils.isNotBlank(this.getFnRowSelected())){
			sb.append("\"fnRowSelected\":function(nodes){" + this.getFnRowSelected() + "(nodes)},");//选择行后的回调函数
		}
		/*********按键初始化开始**********/
		sb.append("\"aButtons\":[");
		i = 0;
		for (DataGridToolBarTag toolBar : toolBarList) {//按钮
			i++;
			sb.append("{");
			sb.append("\"sExtends\": \"" + toolBar.getType() + "\",");
			sb.append("\"sButtonText\": \"" + toolBar.getTitle() + "\",");
			sb.append("\"fnClick\": function (nButton, oConfig, oFlash){");
			if(StringUtils.isNotBlank(toolBar.getUrl())){
				if(toolBar.getUrl().indexOf("{selected}") != -1){
					sb.append("location.href='" + toolBar.getUrl().replace("{selected}", "") + "' + selected;");
				}else{
					sb.append("location.href='" + toolBar.getUrl() + "';");
				}
			}else if(StringUtils.isNotBlank(toolBar.getScript())){
				sb.append(toolBar.getScript());
			}else if(StringUtils.isNotBlank(toolBar.getOnclick())){
				sb.append(toolBar.getOnclick() + "(nButton, oConfig, oFlash);");
			}
			sb.append("}}");
			if (i < toolBarList.size())
				sb.append(",");
		}
		sb.append("]");
		/*********按键初始化结束**********/
		sb.append("});");
		sb.append("$( tt.fnContainer() ).insertBefore('div.dataTables_wrapper');");
		/*********TableTool初始化结束**********/
		sb.append("});");
		sb.append("</script>");
		sb.append("<input type=\"hidden\" id=\"" + this.getId() + "SelectedId\">");
		sb.append("<table class=\"" + this.getCss() + "\" id=\"" + this.getId() + "\"><thead><tr>");
		for (DataGridColumnTag column : columnList) {
			sb.append("<th>" + column.getTitle() + "</th>");
		}
		sb.append("</tr></thead></table>");
		logger.debug(sb.toString());
		return sb.toString();
	}

}
