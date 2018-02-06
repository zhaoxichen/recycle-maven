package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdSecond;

public interface AdSecondMapper {


    AdSecond selectByPrimaryKey(String secId);


	List<AdSecond> selectAdSecondList();


	int insertAdSecond(AdSecond record);


	int deleteByPrimaryKey(String secId);


	int updateByPrimaryKeySelective(AdSecond record);
}