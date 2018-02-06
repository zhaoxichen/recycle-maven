package com.lianjiu.rest.dao;

import com.lianjiu.common.pojo.LianjiuResult;

public interface OrdersFacefaceDao {
	
	LianjiuResult pushFaceOrder(String ordersId);// 订单放出，推送消息给附近的加盟商
	
	LianjiuResult dispatchFaceOrder(String ordersId);// 订单放出后，30分钟无人抢单，强制派单给距离最近的加盟商

	LianjiuResult dispatchFaceOrderFixed(String ordersId);// 回收评估价格大于100块自动派单给指定加盟商
}
