package com.lianjiu.mapper.content;

import java.util.List;

import com.lianjiu.model.AdThird;

public interface AdThirdMapper {

    int deleteByPrimaryKey(String thiId);

    int insertAdTheme(AdThird record);

    int insertSelective(AdThird record);

    AdThird selectByPrimaryKey(String thiId);

    int updateByPrimaryKeySelective(AdThird record);

    int updateByPrimaryKey(AdThird record);
    
	List<AdThird> selectAdThirdList();
    
}
