package com.lianjiu.rest.mapper.content;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.AdHotTopic;

public interface AdHotTopicMapper {


    AdHotTopic selectByPrimaryKey(String topId);

	List<AdHotTopic> selectElectronicList();

	int insertAdHotTopic(AdHotTopic record);

	int deleteByPrimaryKey(String topId);

	int updateByPrimaryKeySelective(AdHotTopic record);

	List<AdHotTopic> vagueQuery(@Param(value="adHotTopic") AdHotTopic adHotTopic,@Param(value="cratedStart")  String cratedStart,@Param(value="cratedOver")  String cratedOver);
}