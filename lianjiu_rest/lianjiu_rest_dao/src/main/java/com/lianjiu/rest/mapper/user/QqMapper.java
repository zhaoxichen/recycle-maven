package com.lianjiu.rest.mapper.user;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.Qq;

public interface QqMapper {
    int deleteByPrimaryKey(Long qqId);

    int insert(Qq record);

    int insertSelective(Qq record);

    Qq selectByPrimaryKey(Long qqId);

    int updateByPrimaryKeySelective(Qq record);

    int updateByPrimaryKey(Qq record);

	String getUserIdByNum(@Param(value = "qqNum") String num);

	Qq getByOpenId(@Param(value = "qqNum") String openId);

	Qq getByNum(String openId);
	
	Qq getNpByUserId(String userId);
}