package com.github.tx.jsite.core.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 列表字段标签
 * 
 * @author tangx
 * @since 2014年10月16日
 */
public class DataGridColumnTag extends SimpleTagSupport {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String title;

	protected String field;

	protected Integer width;

	protected String rowspan;

	protected String colspan;

	protected String align = "left";

	protected boolean orderable = true;

	protected boolean checkbox;

	protected String formatter;

	protected boolean hidden = false;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getTitle() {
		return title;
	}

	public String getField() {
		return field;
	}

	public Integer getWidth() {
		return width;
	}

	public String getRowspan() {
		return rowspan;
	}

	public String getColspan() {
		return colspan;
	}

	public String getAlign() {
		return align;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public String getFormatter() {
		return formatter;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}

	@Override
	public void doTag() throws JspException {
		// 找到最近的DataGrid标签,将自身加入列集合
		DataGridTag parent = (DataGridTag) findAncestorWithClass(this,
				DataGridTag.class);
		parent.addColumn(this);
	}

}
