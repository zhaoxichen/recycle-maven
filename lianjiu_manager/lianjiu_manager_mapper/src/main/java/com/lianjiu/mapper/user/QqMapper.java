package com.lianjiu.mapper.user;

import com.lianjiu.model.Qq;

public interface QqMapper {
    int deleteByPrimaryKey(Long qqId);

    int insert(Qq record);

    int insertSelective(Qq record);

    Qq selectByPrimaryKey(Long qqId);

    int updateByPrimaryKeySelective(Qq record);

    int updateByPrimaryKey(Qq record);
}