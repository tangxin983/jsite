package com.github.tx.jsite.modules.sys.dao;

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.modules.sys.entity.Org;

/**
 * 机构Dao
 * @author tangx
 * @since 2014-05-15
 */
@MyBatisDao
public interface OrgDao extends BaseDao<Org> {
	
}
