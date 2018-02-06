package com.lianjiu.rest.mapper;

import java.util.List;

import com.lianjiu.model.Comment;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.model.CommentInfo;

public interface CommentMapper {
    int deleteByPrimaryKey(String commentId);

    int insert(Comment comment);

    Comment selectByPrimaryKey(String commentId);

    int updateByPrimaryKeySelective(Comment comment);

    int updateByPrimaryKey(Comment comment);

	List<Comment> selectByRelativeId(String relativeId);

	List<CommentInfo> selectByNewRelativeId(String productId);

	void selectByPageToUserId(String userId, String pageNum, String pageSize);

	List<Comment> selectByUserId(String userId);

	List<Integer> selectEmojiById(String userId);

	Integer selectCountById(String userId);

	List<Comment> selectByOrdersId(String ordersId);

	List<Comment> selectByCommentType(Integer commentType);

	String getAverageByUserId(String userId);

	List<Comment> selectBySearchObjecVo(SearchObjecVo vo);
}