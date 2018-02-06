package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdHotTopic;

public interface AdHotTopicMapper {
	
	int deleteByPrimaryKey(String topId);
	
	int insertSelective(AdHotTopic record);
	
	int updateByPrimaryKeySelective(AdHotTopic record);
	
	int updateByPrimaryKey(AdHotTopic record);
	
	AdHotTopic selectByPrimaryKey(String topId);

	int insertAdHotTopic(AdHotTopic record);

	List<AdHotTopic> selectElectronicList();

}