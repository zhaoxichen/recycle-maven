package com.lianjiu.service.content;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdActivity;

public interface ActivityService {

	LianjiuResult deleteByPrimaryKey(String actId);

	LianjiuResult insertActivity(AdActivity record);

	LianjiuResult updateByPrimaryKeySelective(AdActivity record);

	LianjiuResult selectByPrimaryKey(String actId);

	LianjiuResult selectActivityList(int pageNum, int pageSize);
	
}
