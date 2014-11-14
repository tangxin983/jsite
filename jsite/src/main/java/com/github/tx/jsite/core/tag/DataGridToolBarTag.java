package com.github.tx.jsite.core.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具栏标签
 * 
 * @author tangx
 * @since 2014年10月16日
 */
public class DataGridToolBarTag extends SimpleTagSupport {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String title;

	protected String icon;

	protected String onclick;

	protected String url;

	protected String type = "text";

	protected String script;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	@Override
	public void doTag() throws JspException {
		// 找到最近的DataGrid标签,将自身加入工具栏集合
		DataGridTag parent = (DataGridTag) findAncestorWithClass(this,
				DataGridTag.class);
		parent.addToolBar(this);
	}

}
