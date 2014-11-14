package tangx.jsite.site.core.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

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

	private List<DataGridColumnTag> columnList = Lists.newArrayList();// 列表集合

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

	/**
	 * 添加列
	 */
	public void addColumn(DataGridColumnTag column) {
		columnList.add(column);
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspBody().invoke(null);
		getJspContext().getOut().print(dataTables()); 
	}

	public String dataTables() {
		StringBuffer sb = new StringBuffer();
		sb.append("<style type=\"text/css\">");
		sb.append(".col-center { text-align: center; }");
		sb.append(".col-left { text-align: left; }");
		sb.append(".col-right { text-align: right; }");
		sb.append("</style>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("$(document).ready(function() {");
		sb.append("$(\'#" + id + "\').DataTable({");
		sb.append("\"ajax\" : {\"url\" : \"" + this.getQueryUrl() + "\"},");// 查询url
		sb.append("\"serverSide\" : " + this.isServerSide() + ",");// 是否从服务器端分页获取数据
		sb.append("\"lengthChange\" : " + this.isLengthChange() + ",");// 是否允许修改每页记录数
		sb.append("\"ordering\" : " + this.isOrdering() + ",");// 是否允许排序
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
		sb.append("]" + "});" + "});" + "</script>");
		sb.append("<div class=\"panel panel-default\">");
		sb.append("<div class=\"panel-heading\">");
		sb.append("<div class=\"text-muted bootstrap-admin-box-title\">" + this.getTitle() + "</div>");
		sb.append("</div>");
		sb.append("<div class=\"panel-body\">");
		sb.append("<table class=\"" + this.getCss() + "\" id=\"" + this.getId() + "\"><thead><tr>");
		for (DataGridColumnTag column : columnList) {
			sb.append("<th>" + column.getTitle() + "</th>");
		}
		sb.append("</tr></thead></table>");
		sb.append("</div>");
		sb.append("</div>");
		logger.debug(sb.toString());
		return sb.toString();
	}

}
