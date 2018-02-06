package com.lianjiu.service.content.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Comment;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.CommentMapper;
import com.lianjiu.service.content.CommentService;

/**
 * 评论，，服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentMapper commentMapper;

	@Override
	public LianjiuResult getAllCommentByPage(int pageNum, int pageSize) {
		SearchObjecVo vo = new SearchObjecVo();
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<Comment> comments = commentMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Comment> listComment = (Page<Comment>) comments;
		System.out.println("总页数：" + listComment.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(comments);
		result.setTotal(listComment.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getAllComment() {
		SearchObjecVo vo = new SearchObjecVo();
		List<Comment> comments = commentMapper.selectBySearchObjecVo(vo);
		return LianjiuResult.ok(comments);
	}

	@Override
	public LianjiuResult getPageCommentByUser(String userId, int pageNum, int pageSize) {
		Comment comment = new Comment();
		comment.setUserId(userId);
		SearchObjecVo vo = new SearchObjecVo(comment);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<Comment> comments = commentMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Comment> listComment = (Page<Comment>) comments;
		System.out.println("总页数：" + listComment.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(comments);
		result.setTotal(listComment.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getCommentByUser(String userId) {
		Comment comment = new Comment();
		comment.setUserId(userId);
		SearchObjecVo vo = new SearchObjecVo(comment);
		List<Comment> comments = commentMapper.selectBySearchObjecVo(vo);
		return LianjiuResult.ok(comments);
	}

	@Override
	public LianjiuResult deleteCommentById(String commentId) {
		if (Util.isEmpty(commentId)) {
			return LianjiuResult.build(501, "请指定要删除的评论commentId");
		}
		// 去数据库删除
		int rowsAffected = commentMapper.deleteByPrimaryKey(commentId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteCommentByUser(String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(501, "请指定要删除的评论userId");
		}
		// 去数据库删除
		int rowsAffected = commentMapper.deleteByPrimaryKey(userId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult addComment(Comment comment) {
		if (null == comment) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		// 设置id
		comment.setCommentId(IDUtils.genCmIDs());
		comment.setCommentCreated(new Date());
		comment.setCommentUpdated(new Date());
		comment.setCommentEmoji(90);
		// 存入数据库
		int rowsAffected = commentMapper.insert(comment);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateComment(Comment comment) {
		if (null == comment) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		if (Util.isEmpty(comment.getCommentId())) {
			return LianjiuResult.build(501, "请指定要更新的评论id");
		}
		comment.setCommentUpdated(new Date());
		// 存入数据库
		int rowsAffected = commentMapper.updateByPrimaryKeySelective(comment);
		return LianjiuResult.ok(rowsAffected);
	}

}
