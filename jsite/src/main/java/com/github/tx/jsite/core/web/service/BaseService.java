package com.github.tx.jsite.core.web.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.utils.Constant;
import com.github.tx.jsite.utils.Servlets;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;

/**
 * 提供基础的crud等服务
 * 
 * @author tangx
 * @since 2014年10月22日
 *
 * @param <T>
 */
public abstract class BaseService<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected BaseDao<T> dao;

	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}

	/**
	 * 获取所有记录
	 * 
	 * @return 实体对象列表
	 */
	public List<T> select() {
		return dao.select();
	}

	/**
	 * 根据主键获取记录（实体类没有@Id注解会报错）
	 * 
	 * @param id
	 *            主键值
	 * @return 实体对象
	 */
	public T selectById(String id) {
		return dao.selectByPrimaryKey(id);
	}

	/**
	 * 根据前端参数进行查询
	 * 
	 * @param request
	 * @return 实体对象列表
	 */
	public List<T> select(HttpServletRequest request) {
		QueryCondition condition = getQueryCondition(request);
		return dao.selectByCondition(condition);
	}

	/**
	 * 根据条件进行查询
	 * 
	 * @param condition
	 * @return 实体对象列表
	 */
	public List<T> select(QueryCondition condition) {
		return dao.selectByCondition(condition);
	}

	/**
	 * 获取记录数
	 * 
	 * @return 记录数
	 */
	public int count() {
		return dao.count();
	}

	/**
	 * 根据条件获取记录数
	 * 
	 * @param request
	 * @return 记录数
	 */
	public int count(HttpServletRequest request) {
		QueryCondition condition = getQueryCondition(request);
		return dao.countByCondition(condition);
	}

	/**
	 * 插入记录
	 * 
	 * @param entity
	 *            实体对象
	 * @return 影响的记录行数
	 */
	public int insert(T entity) {
		return dao.insertWithUuid(entity);
	}

	/**
	 * 根据主键更新记录（实体类没有@Id注解会报错）
	 * 
	 * @param entity
	 *            实体对象
	 * @return 影响的记录行数
	 */
	public int updateById(T entity) {
		return dao.updateByPrimaryKey(entity);
	}

	/**
	 * 根据主键删除记录（实体类没有@Id注解会报错）
	 * 
	 * @param id
	 *            主键
	 * @return 影响的记录行数
	 */
	public int deleteById(String id) {
		return dao.deleteByPrimaryKey(id);
	}

	/**
	 * 根据主键批量删除记录（实体类没有@Id注解会报错）
	 * 
	 * @param ids
	 *            主键列表
	 */
	public void deleteByIds(List<String> ids) {
		for (String id : ids) {
			dao.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 根据前端参数进行分页查询
	 * 
	 * @param request
	 * @return 分页对象
	 */
	public DataTableEntity<T> selectByPage(HttpServletRequest request) {
		startPagination(request);
		QueryCondition condition = getQueryCondition(request);
		List<T> data = dao.selectByCondition(condition);
		return buildDataTableEntity(request, data);
	}

	/**
	 * 启动分页
	 * @param request
	 */
	protected void startPagination(HttpServletRequest request) {
		int start = Integer.parseInt(StringUtils.defaultIfBlank(
				request.getParameter("start"), "0"));
		int pageSize = Integer.parseInt(StringUtils.defaultIfBlank(
				request.getParameter("length"), Constant.PAGINATION_SIZE));
		int pageNum = start/pageSize + 1;
		PageHelper.startPage(pageNum, pageSize);
	}

	/**
	 * 构建dataTable分页对象
	 * 
	 * @param request
	 * @param data
	 * @return
	 */
	protected DataTableEntity<T> buildDataTableEntity(
			HttpServletRequest request, List<T> data) {
		long count = ((Page<T>) data).getTotal();
		int draw = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("draw"), "1"));
		DataTableEntity<T> entity = new DataTableEntity<T>(count, count, draw, data);
		return entity;
	}

	/**
	 * 使用前台参数构造查询条件对象。默认各参数间用and连接
	 * 
	 * @param request
	 * @return
	 */
	protected QueryCondition getQueryCondition(HttpServletRequest request) {
		QueryCondition condition = new QueryCondition();
		Criteria criteria = new Criteria();
		// 相等
		Map<String, Object> eqParams = Servlets.getParametersStartingWith(
				request, "eq_");
		for (String key : eqParams.keySet()) {
			criteria.eq(key, eqParams.get(key));
		}
		// like
		Map<String, Object> likeParams = Servlets.getParametersStartingWith(
				request, "li_");
		for (String key : likeParams.keySet()) {
			criteria.like(key, likeParams.get(key));
		}
		// 大于
		Map<String, Object> gtParams = Servlets.getParametersStartingWith(
				request, "gt_");
		for (String key : gtParams.keySet()) {
			criteria.gt(key, gtParams.get(key));
		}
		// 小于
		Map<String, Object> ltParams = Servlets.getParametersStartingWith(
				request, "lt_");
		for (String key : ltParams.keySet()) {
			criteria.lt(key, ltParams.get(key));
		}
		// 大等于
		Map<String, Object> geParams = Servlets.getParametersStartingWith(
				request, "ge_");
		for (String key : geParams.keySet()) {
			criteria.ge(key, geParams.get(key));
		}
		// 小等于
		Map<String, Object> leParams = Servlets.getParametersStartingWith(
				request, "le_");
		for (String key : leParams.keySet()) {
			criteria.le(key, leParams.get(key));
		}
		condition.or(criteria);
		String orderColumnIndex = StringUtils.defaultString(request
				.getParameter("order[0][column]"));
		if (!orderColumnIndex.equals("")) {
			String orderDirection = StringUtils.defaultString(request
					.getParameter("order[0][dir]"));
			String orderColumn = StringUtils.defaultString(request
					.getParameter("columns[" + orderColumnIndex + "][data]"));
			if (!orderColumn.equals("")) {
				if (orderDirection.equals("asc")) {
					condition.asc(orderColumn);
				} else if (orderDirection.equals("desc")) {
					condition.desc(orderColumn);
				}
			}
		}
		return condition;
	}
}
