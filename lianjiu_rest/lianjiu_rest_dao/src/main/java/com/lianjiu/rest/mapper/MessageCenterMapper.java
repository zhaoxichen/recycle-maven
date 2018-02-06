package com.lianjiu.rest.mapper;

import java.util.List;

import com.lianjiu.model.MessageCenter;

public interface MessageCenterMapper {
    int deleteByPrimaryKey(String messageId);

    int insert(MessageCenter messageCenter);

    int insertSelective(MessageCenter record);

    MessageCenter selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(MessageCenter record);

    int updateByPrimaryKey(MessageCenter record);

	List<MessageCenter> getContByUserId(String allianceId);

	int modifyStatus(String allianceId);

	int getCountByUserId(String userId);
}