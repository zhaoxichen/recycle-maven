package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersRemarks;

public interface OrderRemarksService {
	LianjiuResult getRemarksAll();

	LianjiuResult getRemarksAll(int pageNum, int pageSize);

	LianjiuResult getRemarksBySid(String ordersId);

	LianjiuResult getRemarksBySid(String ordersId, int pageNum, int pageSize);

	LianjiuResult getRemarksById(String remarksId);

	LianjiuResult addRemarks(OrdersRemarks remarks);

	LianjiuResult updateRemarks(OrdersRemarks remarks);

	LianjiuResult deleteRemarks(String remarksId);
}
