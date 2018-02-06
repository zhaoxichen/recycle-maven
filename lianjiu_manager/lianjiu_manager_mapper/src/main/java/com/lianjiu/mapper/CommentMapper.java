package com.lianjiu.mapper;

import java.util.List;

import com.lianjiu.model.Comment;
import com.lianjiu.model.vo.SearchObjecVo;

public interface CommentMapper {
    int deleteByPrimaryKey(String commentId);

    int insert(Comment comment);

    int insertSelective(Comment comment);

    Comment selectByPrimaryKey(String commentId);

    int updateByPrimaryKeySelective(Comment comment);

    int updateByPrimaryKey(Comment comment);

	List<Comment> selectBySearchObjecVo(SearchObjecVo vo);
}