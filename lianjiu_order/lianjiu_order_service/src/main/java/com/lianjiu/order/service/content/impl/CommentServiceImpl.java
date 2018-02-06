package com.lianjiu.order.service.content.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Comment;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.order.service.content.CommentService;
import com.lianjiu.rest.mapper.CommentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;

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
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;

	@Override
	public LianjiuResult getAllCommentByPage(int pageNum, int pageSize) {
		SearchObjecVo vo = new SearchObjecVo();
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<Comment> comments = null;
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
		List<Comment> comments = null;
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
		List<Comment> comments = null;
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
		List<Comment> comments = null;
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
			return LianjiuResult.build(501, "传入对象为空");
		}
		if (comment.getCommentEmoji() != null && comment.getCommentEmoji() != 90) {
			if (comment.getCommentEmoji() != 60) {
				if (comment.getCommentEmoji() != 30) {
					return LianjiuResult.build(502, "评分只能是90，60，30");
				}
			}
		}
		// 设置id
		String ordersID = comment.getOrdersId();
		if (Util.isEmpty(ordersID)) {
			comment.setCommentId(IDUtils.genCmIDs());
		} else {
			comment.setCommentId(ordersID);
		}
		comment.setCommentCreated(new Date());
		comment.setCommentUpdated(new Date());
		// 存入数据库
		int rowsAffected = commentMapper.insert(comment);
		// 去修改状态
		switch (comment.getCommentType()) {
		case 7:// 精品的
			OrdersExcellent excellentOrders = new OrdersExcellent();
			excellentOrders.setOrExcellentId(ordersID);
			excellentOrders.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_EVALUATE_YES);
			excellentOrders.setOrExcellentUpdated(new Date());
			ordersExcellentMapper.modifyExcellentState(excellentOrders);
			break;

		default:
			break;
		}

		return LianjiuResult.ok(comment.getCommentId());
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

	@Override
	public LianjiuResult getCommentByCurrent(String relativeId, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<Comment> comments = commentMapper.selectByRelativeId(relativeId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Comment> listComment = (Page<Comment>) comments;
		System.out.println("总页数：" + listComment.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(comments);
		result.setTotal(listComment.getTotal());
		return result;
	}

	/**
	 * 通过主键id查询
	 */
	@Override
	public LianjiuResult getOneComment(String commentId) {
		Comment comment = commentMapper.selectByPrimaryKey(commentId);
		return LianjiuResult.ok(comment);
	}

}
