package com.lianjiu.rest.service.product;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersItem;

public interface ProductWasteService {

	LianjiuResult getWasteByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getWasteDetails(String wasteId);

	LianjiuResult introductionWaste(String userId,OrdersItem wasteItem);
}
