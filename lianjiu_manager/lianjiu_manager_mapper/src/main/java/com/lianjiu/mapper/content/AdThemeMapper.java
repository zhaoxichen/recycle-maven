package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdTheme;

public interface AdThemeMapper {

	int insertAdTheme(AdTheme record);

	List<AdTheme> selectAdSecondList();

    int deleteByPrimaryKey(String theId);

    int insertSelective(AdTheme record);

    AdTheme selectByPrimaryKey(String theId);

    int updateByPrimaryKeySelective(AdTheme record);

    int updateByPrimaryKey(AdTheme record);


	
}