package com.lianjiu.rest.service.orders;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.rest.model.ExcellentItemVo;

public interface OrderExcellentService {
	LianjiuResult getExcellentById(String excellentId);

	LianjiuResult addExcellent(OrdersExcellent excellent);

	LianjiuResult updateExcellent(OrdersExcellent excellent);

	LianjiuResult deleteExcellent(String excellentId);

	LianjiuResult modifyExcellentState(String excellentId,Byte orExcellentStatus);

	LianjiuResult modifyExcellentHandleTime(String excellentId);
	
	LianjiuResult addExcellentOrdersItem(ExcellentItemVo excellentItemVo);
	
	LianjiuResult getByExcellentList(Long categoryId);

	LianjiuResult getByExcellentList(Long cid, int pageNum, int pageNum2);

	LianjiuResult selectAddressDefault(String userId);

	LianjiuResult getExcellentAll(int pageNum, int pageSize);

	LianjiuResult findAllByStatus(Byte orExcellentStatus,int pageNum,int pageSize);

	LianjiuResult getExcellentByUserId(String userId,int pageNum,int pageSize);

	LianjiuResult getExcellentByUserStatus(Byte orExcellentStatus, String userId,int pageNum,int pageSize);
}
