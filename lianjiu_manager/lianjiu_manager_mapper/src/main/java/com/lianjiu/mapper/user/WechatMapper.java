package com.lianjiu.mapper.user;

import com.lianjiu.model.Wechat;

public interface WechatMapper {
	int insert(Wechat wechat);

	int insertSelective(Wechat wechat);

	// 更新
	int updateByPrimaryKey(Wechat wechat);

	int updateByPrimaryKeySelective(Wechat wechat);

	int deleteProductByid(Long id);

	int deleteByPrimaryKey(Long id);
}