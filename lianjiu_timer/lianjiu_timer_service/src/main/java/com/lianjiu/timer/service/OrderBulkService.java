package com.lianjiu.timer.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface OrderBulkService {

	LianjiuResult orderConfirm(String ordersId);
	
	LianjiuResult orderConfirmDel(String ordersId);

}
