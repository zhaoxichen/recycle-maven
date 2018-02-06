package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdTheme;

public interface AdThemeMapper {


    AdTheme selectByPrimaryKey(String theId);


	List<AdTheme> selectAdSecondList();


	AdTheme getImageByOrderId(String str);


	int insertAdTheme(AdTheme record);


	int deleteByPrimaryKey(String theId);


	int updateByPrimaryKeySelective(AdTheme record);

	
}