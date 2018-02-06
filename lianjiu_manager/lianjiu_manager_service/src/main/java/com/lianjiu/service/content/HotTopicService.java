package com.lianjiu.service.content;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AdHotTopic;

public interface HotTopicService {


	LianjiuResult insertHotTopic(AdHotTopic record);

	LianjiuResult deleteHotTopic(String topId);

	LianjiuResult updateHotTopic(AdHotTopic record);

	LianjiuResult selectHotTopicById(String topId);

	LianjiuResult selectHotTopicList(int pageNum, int pageSize);

	LianjiuResult vagueQuery(AdHotTopic adHotTopic, int pageNum, int pageSize, String cratedStart, String cratedOver);

}
