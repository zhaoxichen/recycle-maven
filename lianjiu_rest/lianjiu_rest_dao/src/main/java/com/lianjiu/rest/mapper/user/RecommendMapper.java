package com.lianjiu.rest.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.Recommend;

public interface RecommendMapper {

	String getIdByNum(String referrer);

	int insert(Recommend recommend);

	String getRecommendNumByUser(String userId);

	List<String> getRecommendNumList();

	Recommend getRecomByNum(@Param(value = "recommendNum") String referrer);

	int modifyTotalNum(Recommend recommend);

	int getCountByPhone(String recommendPhone);
	
	int updateByPrimaryKeySelective(Recommend recommend);

	List<Recommend> getRecommeByParm(@Param(value = "parameter") String parameter);

	String getIdByPhone(String recommendPhone);

	String getPhoneById(String recommendId);

}
