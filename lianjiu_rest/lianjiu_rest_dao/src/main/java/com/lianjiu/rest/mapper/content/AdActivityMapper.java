package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdActivity;

public interface AdActivityMapper {


    AdActivity selectByPrimaryKey(String actId);


	List<AdActivity> selectActivityList();


	int deleteByPrimaryKey(String actId);


	int insert(AdActivity record);


	int updateByPrimaryKeySelective(AdActivity record);


	int getCount();
}