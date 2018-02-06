package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdActivity;

public interface AdActivityMapper {

    int deleteByPrimaryKey(String actId);

    int insertActivity(AdActivity record);

    int insertSelective(AdActivity record);

    AdActivity selectByPrimaryKey(String actId);

    int updateByPrimaryKeySelective(AdActivity record);

    int updateByPrimaryKey(AdActivity record);

	List<AdActivity> selectActivityList();
}