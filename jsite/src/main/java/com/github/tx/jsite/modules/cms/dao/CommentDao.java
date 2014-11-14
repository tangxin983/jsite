package com.github.tx.jsite.modules.cms.dao;

import com.github.tx.jsite.core.persistence.annotation.MyBatisDao;
import com.github.tx.jsite.core.persistence.dao.BaseDao;
import com.github.tx.jsite.modules.cms.entity.Comment;

/**
 * 评论Dao
 * @author tangx
 * @since 2014-11-06
 */
@MyBatisDao
public interface CommentDao extends BaseDao<Comment> {
	
}
