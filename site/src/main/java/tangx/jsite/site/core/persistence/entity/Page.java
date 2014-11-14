package tangx.jsite.site.core.persistence.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页基础类
 */
public class Page<T> {
	
	/**dataTable计数器*/
	private int draw;
	
	/**dataTable当前页头条记录序号*/
	private int start;
	
	/**dataTable显示记录数*/
	private int recordsFiltered;
	
	/**每页显示几条*/
	private int size = 20;
	
	/**数据库总记录数*/
	private int recordsTotal = 0; 
	
	/**当前页*/
	private int currentPage = 0; 
	
	/**当前页起始索引*/
	private int currentResult = 0; 
	
	/**当前页结束索引*/
	private int currentEndResult = 0;
	
	/**存放结果集*/
	private List<T> data = new ArrayList<T>();
	
	public List<T> getData() {
		if (data == null) {
			return new ArrayList<T>();
		}
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * 
	 * <p>
	 * 获取总页数
	 * </p>
	 * 
	 * @return
	 */
	public int getTotalPage() {
		if (recordsTotal % size == 0) {
			return recordsTotal / size;
		}
		return recordsTotal / size + 1;
	}

	/**
	 * 
	 * <p>
	 * 获取总条数
	 * </p>
	 * 
	 * @return
	 */
	public int getRecordsTotal() {
		return recordsTotal;
	}

	/**
	 * 
	 * <p>
	 * 设置总条数
	 * </p>
	 * 
	 * @param total
	 */
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	/**
	 * 当前页数
	 * @return
	 */
	public int getCurrentPage() {
		if (currentPage <= 0) {
			currentPage = 1;
		}
		if (currentPage > getTotalPage()) {
			currentPage = getTotalPage();
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size == 0) {
			size = 10;
		}
		this.size = size;
	}

	public int getCurrentResult() {
		currentResult = (getCurrentPage() - 1) * getSize();
		if (currentResult < 0) {
			currentResult = 0;
		}
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	
	public boolean hasPreviousPage(){
		return currentPage > 1 ? true : false;
	}
	
	public boolean hasNextPage(){
		return currentPage < getTotalPage() ? true : false;
	}

	public int getCurrentEndResult() {
		if(getCurrentPage() != getTotalPage()){
			this.currentEndResult = getSize() * getCurrentPage();
		}else{
			this.currentEndResult = getRecordsTotal();
		}
		return currentEndResult;
	}

	public void setCurrentEndResult(int currentEndResult) {
		this.currentEndResult = currentEndResult;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	
}