package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersBulk;

public interface OrderBulkService {

	LianjiuResult getOrdersPrice(String TAKEN);
	
	LianjiuResult getOrdersPreview(String TAKEN);

	LianjiuResult submit(String TAKEN, OrdersBulk ordersBulk);

	LianjiuResult getOrdersByUserStatus(Integer status, String userId, int pageNum, int pageSize);

	LianjiuResult cancelOrders(String ordersId);

	LianjiuResult agreeOrders(String ordersId);

	LianjiuResult ordersDetails(String ordersId);

}
