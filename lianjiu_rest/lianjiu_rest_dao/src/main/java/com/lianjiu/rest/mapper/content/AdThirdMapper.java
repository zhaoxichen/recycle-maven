package com.lianjiu.rest.mapper.content;

import java.util.List;

import com.lianjiu.model.AdThird;

public interface AdThirdMapper {



    AdThird selectByPrimaryKey(String thiId);

	List<AdThird> selectAdThirdList();

	int insertAdTheme(AdThird record);

	int deleteByPrimaryKey(String thiId);

	int updateByPrimaryKeySelective(AdThird record);
}