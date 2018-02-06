package com.lianjiu.rest.service.content;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdActivity;

public interface ActivityService {


	LianjiuResult selectByPrimaryKey(String actId);

	LianjiuResult selectActivityList(int pageNum, int pageSize);
	
}
