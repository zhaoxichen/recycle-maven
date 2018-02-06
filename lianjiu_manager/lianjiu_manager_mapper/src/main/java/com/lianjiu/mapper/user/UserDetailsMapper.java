package com.lianjiu.mapper.user;

import com.lianjiu.model.UserDetails;

public interface UserDetailsMapper {
    int deleteByPrimaryKey(String udetailsId);

    int insert(UserDetails record);

    int insertSelective(UserDetails record);

    UserDetails selectByPrimaryKey(String udetailsId);

    int updateByPrimaryKeySelective(UserDetails record);

    int updateByPrimaryKey(UserDetails record);

	void saveOpenId(String openId, String userId);
}