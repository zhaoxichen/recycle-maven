package com.lianjiu.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Comment;
import com.lianjiu.order.service.content.CommentService;

/**
 * 评论
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/comment/")
public class CommentsController {
	@Autowired
	private CommentService commentService;

	/**
	 * 
	 * xiangyang 2017年9月5日 descrption:分页获取所有评论
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getChoseComment/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getComment(int pageNum, int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if(1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return commentService.getAllCommentByPage(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:获取所有评论
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getComment/all", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getComment() {
		return commentService.getAllComment();
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:分页获取指定用户所有评论 指定用户的评论信息量不大，用get请求足够
	 * 
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getComment/{userId}/{pageNum}/{pageSize}/user", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCommentByUser(@PathVariable String userId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if(1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(403, "请传入正确的用户id");
		}
		return commentService.getPageCommentByUser(userId, pageNum, pageSize);

	}

	@RequestMapping(value = "getComment/{relativeId}/{pageNum}/{pageSize}/current", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCommentByCurrent(@PathVariable String relativeId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if(1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		if(Util.isEmpty(relativeId)){
			return LianjiuResult.build(403, "请传入正确的relativeId");
		}
		return commentService.getCommentByCurrent(relativeId, pageNum, pageSize);

	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:获取指定用户所有评论
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getComment/{userId}/user", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getCommentByUser(@PathVariable String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的用户id");
		}
		return commentService.getCommentByUser(userId);

	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:删帖，，，
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "removeComment", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult removeCommentById(String commentId) {
		if(Util.isEmpty(commentId)){
			return LianjiuResult.build(401, "请传入正确的commentId");
		}
		return commentService.deleteCommentById(commentId);

	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:删帖，，，，该用户所有的删
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "removeComment/{userId}/user", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult removeCommentByUser(@PathVariable String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的用户id");
		}
		return commentService.deleteCommentByUser(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption: 提交一条评论
	 * 
	 * @param comment
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "submitComment", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult submitComment(Comment comment) {
		if(null == comment){
			return LianjiuResult.build(401, "comment对象绑定错误");
		}
		return commentService.addComment(comment);

	}

	/**
	 * 
	 * zhaoxi 2017年9月5日 descrption:修改评论
	 * 
	 * @param comment
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyComment", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyCommentByUser(Comment comment) {
		if(null == comment){
			return LianjiuResult.build(401, "comment对象绑定错误");
		}
		return commentService.updateComment(comment);
	}

	/**
	 * 
	 * zhaoxi 2017年11月8日 descrption:
	 * 
	 * @param commentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getOneComment/{commentId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOneComment(@PathVariable String commentId) {
		if(Util.isEmpty(commentId)){
			return LianjiuResult.build(401, "请传入正确的commentId");
		}
		return commentService.getOneComment(commentId);
	}

}
