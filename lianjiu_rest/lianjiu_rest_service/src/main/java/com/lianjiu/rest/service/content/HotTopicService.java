package com.lianjiu.rest.service.content;

import com.lianjiu.common.pojo.LianjiuResult;

public interface HotTopicService {


	LianjiuResult selectHotTopicById(String topId);

	LianjiuResult selectHotTopicList(int pageNum, int pageSize);

}
