package com.lianjiu.order.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersFurniture;

public interface OrderFurnitureService {

	LianjiuResult getOrders(int pageNum, int pageSize);

	LianjiuResult cancelOrders(String ordersId);

	LianjiuResult submit(OrdersFurniture furniture);

}
