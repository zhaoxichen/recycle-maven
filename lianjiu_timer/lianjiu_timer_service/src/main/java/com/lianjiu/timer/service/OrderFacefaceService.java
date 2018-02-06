package com.lianjiu.timer.service;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;

public interface OrderFacefaceService {

	LianjiuResult orderConfirm(String ordersId);
	
	LianjiuResult orderConfirmDel(String ordersId);

	LianjiuResult orderAutoDispatch(String ordersId);

	LianjiuResult orderDispatch(String ordersId);
	
	LianjiuResult orderDispatchDel(String ordersId);

	LianjiuResult orderNightDispatch(String ordersId, String price);

	LianjiuResult orderNightDispatchHandle(String ordersId);

	LianjiuResult orderStatusReduction(List<String> ordersIdList);
}
