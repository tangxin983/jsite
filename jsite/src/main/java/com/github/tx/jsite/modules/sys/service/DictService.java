package com.github.tx.jsite.modules.sys.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.sys.dao.DictDao;
import com.github.tx.jsite.modules.sys.entity.Dict;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;

/**
 * 字典Service
 * 
 * @author tangx
 * @since 2014-06-25
 */
@Service
@Transactional
public class DictService extends BaseService<Dict> {

	private DictDao dictDao;

	@Autowired
	public void setDictDao(DictDao dictDao) {
		super.setDao(dictDao);
		this.dictDao = dictDao;
	}

	/**
	 * 字典类型列表
	 * 
	 * @return
	 */
	public List<String> findTypeList() {
		return dictDao.findTypeList();
	}
	
	@Override
	public DataTableEntity<Dict> selectByPage(HttpServletRequest request) {
		return super.selectByPage(request);
	}

	/**
	 * 根据字典类型获取字典列表
	 * @param type 字典类型
	 * @return
	 */
	@Cacheable(value = "dictCache", key = "#type")
	public List<Dict> findDictListByType(String type) {
		QueryCondition condition = new QueryCondition();
		condition.or(new Criteria().eq("type", type));
		return dictDao.selectByCondition(condition);
	}

	@CacheEvict(value = "dictCache", key="#entity.type")
	@Override
	public int insert(Dict entity) {
		return super.insert(entity);
	}

	@CacheEvict(value = "dictCache", key="#entity.type")
	@Override
	public int updateById(Dict entity) {
		return super.updateById(entity);
	}

	@CacheEvict(value = "dictCache", allEntries = true)
	@Override
	public int deleteById(String id) {
		return super.deleteById(id);
	}

	@CacheEvict(value = "dictCache", allEntries = true)
	@Override
	public void deleteByIds(List<String> ids) {
		super.deleteByIds(ids);
	}

}
