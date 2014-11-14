package com.github.tx.jsite.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.modules.sys.entity.Dict;

/**
 * 字典Dao
 * @author tangx
 * @since 2014-06-25
 */
@MyBatisDao
public interface DictDao extends BaseDao<Dict> {
	
	@Select("select type from sys_dict group by type")
	public List<String> findTypeList();
	
}
