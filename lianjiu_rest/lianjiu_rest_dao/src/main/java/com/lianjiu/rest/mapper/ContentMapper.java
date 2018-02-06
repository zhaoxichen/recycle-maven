package com.lianjiu.rest.mapper;

import com.lianjiu.model.Content;

public interface ContentMapper {

	int deleteByPrimaryKey(Long contentId);

	int insert(Content record);

	int insertSelective(Content record);

	Content selectByPrimaryKey(Long contentId);

	int updateByPrimaryKeySelective(Content record);
}