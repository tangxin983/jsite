package com.github.tx.jsite.core.persistence.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * DataTables期望返回的实体
 * 
 * @author tangx
 * @since 2014年10月23日
 */
public class DataTableEntity<T> {

	private int draw;// 计数器

	private long recordsFiltered;// dataTable显示记录数

	private long recordsTotal;// dataTable总记录数

	private List<T> data = new ArrayList<T>();// 结果集
	
	public DataTableEntity(){
		
	}

	public DataTableEntity(long recordsTotal, long recordsFiltered, int draw,
			List<T> data) {
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
		this.draw = draw;
		this.data = data;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public long getRecordsFiltered() {
		if (recordsFiltered == 0) {
			return getRecordsTotal();
		}
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

}