package com.lianjiu.timer.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface OrderExpressService {
	
	LianjiuResult orderConfirm(String ordersId);
	
	LianjiuResult orderConfirmDel(String ordersId);
}
