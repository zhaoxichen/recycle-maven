package com.lianjiu.mapper.user;

import com.lianjiu.model.UserAddress;

public interface UserAddressMapper {
    int deleteByPrimaryKey(String userAddressId);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    UserAddress selectByPrimaryKey(String userAddressId);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);
}