package com.lianjiu.rest.service.content.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.Comment;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.User;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CommentMapper;
import com.lianjiu.rest.mapper.orders.OrdersBulkMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExpressMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.service.content.CommentService;

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
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private OrdersExpressMapper ordersExpressMapper;
	@Autowired
	private OrdersBulkMapper ordersBulkMapper;
	@Autowired
	private JedisClient jedisClient;
	private static Logger loggerOperation = Logger.getLogger("operation");

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
		/**
		 * 从redis中取出用户
		 */
		String userJson = jedisClient.get(GlobalValueJedis.REDIS_USER_SESSION_KEY + comment.getUserId());
		if (Util.isEmpty(userJson)) {
			// 发个短信通知工作人员
			sendSMS.sendMessage("16", "+86", GlobalValueJedis.ADMIN_PHONE,
					GlobalValueJedis.REDIS_USER_SESSION_KEY + comment.getUserId() + "--提交评论");
			loggerOperation.info("掉线用户的key：" + GlobalValueJedis.REDIS_USER_SESSION_KEY + comment.getUserId());
			return LianjiuResult.build(501, "用户已经掉线");
		}
		User user = JsonUtils.jsonToPojo(userJson, User.class);
		if (null != user) {
			comment.setUsername(user.getUsername());
		}
		/**
		 * 校验参数
		 */
		if (comment.getCommentEmoji() != null) {
			if (comment.getCommentEmoji() != 90 && comment.getCommentEmoji() != 60 && comment.getCommentEmoji() != 30) {
				return LianjiuResult.build(502, "评分只能是90，60，30");
			}
		}
		if (comment.getCommentLabelPrice() != null) {
			if (!"价格高,价格公道,价格低".contains(comment.getCommentLabelPrice())) {
				return LianjiuResult.build(503, "commentLabelPrice只能是 价格高,价格公道,价格低");
			}
		}
		if (comment.getCommentLabelRemit() != null) {
			if (!"打款快,打款慢".contains(comment.getCommentLabelRemit())) {
				return LianjiuResult.build(504, "commentLabelRemit只能是 打款快,打款慢");
			}
		}
		if (comment.getCommentLabelService() != null) {
			if (!"态度好,服务不好,极贴心".contains(comment.getCommentLabelService())) {
				return LianjiuResult.build(505, "commentLabelService只能是 态度好,服务不好,极贴心");
			}
		}
		// 设置id
		comment.setCommentId(IDUtils.genCmIDs());
		comment.setCommentCreated(new Date());
		comment.setCommentUpdated(new Date());
		String ordersID = null;
		int rowsAffected = 0;
		try {
			// 去修改状态
			switch (comment.getCommentType()) {
			case 3:// 上门回收
				ordersID = comment.getRelativeId();
				comment.setOrdersId(ordersID);
				// 存入数据库
				rowsAffected = commentMapper.insert(comment);
				ordersFacefaceMapper.modifyOrdersStatus(GlobalValueUtil.ORDER_FACEFACE_EVALUATE_YES, ordersID);
				break;
			case 4:// 快递回收
				ordersID = comment.getRelativeId();
				comment.setOrdersId(ordersID);
				// 存入数据库
				rowsAffected = commentMapper.insert(comment);
				ordersExpressMapper.modifyOrdersStatus(GlobalValueUtil.ORDER_EXPRESS_EVALUATE_YES, ordersID);
				break;
			case 5:// 大宗单回收
				ordersID = comment.getRelativeId();
				comment.setOrdersId(ordersID);
				// 存入数据库
				rowsAffected = commentMapper.insert(comment);
				ordersBulkMapper.modifyOrdersStatus(GlobalValueUtil.ORDER_BLUK_EVALUATE_YES, ordersID);
				break;
			case 7:// 精品的
				ordersID = comment.getOrdersId();
				// 存入数据库
				rowsAffected = commentMapper.insert(comment);
				ordersExcellentMapper.modifyOrdersStatus(GlobalValueUtil.ORDER_STATUS_EVALUATE_YES, ordersID);
				break;
			default:
				break;
			}
		} catch (RuntimeException e) {
			return LianjiuResult.build(510, "请提交文字格式的评论内容");
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
	 * 通过来源获取评论
	 */
	@Override
	public LianjiuResult getCommentByCommentType(Integer commentType, int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<Comment> comments = commentMapper.selectByCommentType(commentType);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Comment> listComment = (Page<Comment>) comments;
		System.out.println("总页数：" + listComment.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(comments);
		result.setTotal(listComment.getTotal());
		return result;
	}

}
