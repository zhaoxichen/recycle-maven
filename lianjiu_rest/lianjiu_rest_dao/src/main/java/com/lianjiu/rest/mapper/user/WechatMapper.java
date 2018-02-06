package com.lianjiu.rest.mapper.user;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.Wechat;

public interface WechatMapper {

    int deleteByPrimaryKey(String wechatId);

    int insert(Wechat record);

    int insertSelective(Wechat record);

    Wechat selectByPrimaryKey(String wechatId);

    int updateByPrimaryKeySelective(Wechat record);

    int updateByPrimaryKey(Wechat record);

	String getUserIdByNum(@Param(value = "wechatNum") String num);

	Wechat getByOpenId(@Param(value = "wechatNum") String openId);

	Wechat getByNum(String openId);

	Wechat getNpByUserId(String userId);
}