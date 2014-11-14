package com.github.tx.jsite.modules.cms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tx.jsite.core.persistence.entity.DataTableEntity;
import com.github.tx.jsite.core.web.service.BaseService;
import com.github.tx.jsite.modules.cms.dao.CommentDao;
import com.github.tx.jsite.modules.cms.entity.Comment;

/**
 * 评论Service
 * @author tangx
 * @since 2014-11-06
 */
@Service
@Transactional
public class CommentService extends BaseService<Comment> {

	private CommentDao commentDao;

	@Autowired
	public void setCommentDao(CommentDao commentDao) {
		super.setDao(commentDao);
		this.commentDao = commentDao;
	}
	
	// ========== 以下为简单增删改示例。 修改以适应实际需求===========
	@Override
	public DataTableEntity<Comment> selectByPage(HttpServletRequest request) {
		return super.selectByPage(request);
	}
	
	@Override
	public int insert(Comment entity) {
		return super.insert(entity);
	}
	
	@Override
	public int updateById(Comment entity) {
		return super.updateById(entity);
	}

	@Override
	public int deleteById(String id) {
		return super.deleteById(id);
	}
	 
	@Override
	public void deleteByIds(List<String> ids) {
		super.deleteByIds(ids);
	}
	
}
