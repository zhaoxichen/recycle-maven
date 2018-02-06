package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdSecond;

import com.lianjiu.model.AdHotTopic;
import com.lianjiu.model.AdSecond;

public interface AdSecondMapper {
	
	int deleteByPrimaryKey(String secId);
	
	int insertSelective(AdSecond record);
	
	int updateByPrimaryKeySelective(AdSecond record);
	
	int updateByPrimaryKey(AdSecond record);
	
	AdSecond selectByPrimaryKey(String secId);

	int insertAdSecond(AdSecond record);

	List<AdSecond> selectAdSecondList();
}