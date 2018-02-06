package com.lianjiu.order.service;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.OrdersRepairScheme;

public interface OrderRepairService {

	LianjiuResult getRepairAll();

	LianjiuResult getRepairAll(int pageNum, int pageSize);

	LianjiuResult getRepairByCid(Long cid);

	LianjiuResult getRepairByCid(Long cid, int pageNum, int pageSize);

	LianjiuResult getRepairById(String repairId);

	LianjiuResult addRepair(OrdersRepair repair, OrdersRepairScheme scheme);

	LianjiuResult updateRepair(OrdersRepair repair);

	LianjiuResult deleteRepair(String repairId);

	LianjiuResult getItemByOrdersId(String ordersId, int pageNum, int pageSize);

	LianjiuResult getRepairByUid(String uid, Byte status);

	LianjiuResult getRepairByUid(String uid, List<Byte> status);

	LianjiuResult updateStatus(String ordersId, Byte status);

}
