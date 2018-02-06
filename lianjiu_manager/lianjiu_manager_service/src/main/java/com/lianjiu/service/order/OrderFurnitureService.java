package com.lianjiu.service.order;

import com.lianjiu.common.pojo.LianjiuResult;

public interface OrderFurnitureService {

	LianjiuResult getOrders(int pageNum, int pageSize);

	LianjiuResult cancelOrders(String ordersId);

}
