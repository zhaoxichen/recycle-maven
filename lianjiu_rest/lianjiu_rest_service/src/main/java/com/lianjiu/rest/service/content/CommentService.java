package com.lianjiu.rest.service.content;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Comment;

public interface CommentService {

	LianjiuResult getAllCommentByPage(int pageNum, int pageSize);

	LianjiuResult getAllComment();

	LianjiuResult getPageCommentByUser(String userId, int pageNum, int pageSize);

	LianjiuResult getCommentByUser(String userId);

	LianjiuResult deleteCommentById(String commentId);

	LianjiuResult deleteCommentByUser(String userId);

	LianjiuResult addComment(Comment comment);

	LianjiuResult updateComment(Comment comment);

	LianjiuResult getCommentByCurrent(String relativeId, int pageNum, int pageSize);

	LianjiuResult getCommentByCommentType(Integer commentType, int pageNum, int pageSize);

}
