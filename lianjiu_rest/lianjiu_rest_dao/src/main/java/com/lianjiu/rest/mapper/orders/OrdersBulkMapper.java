package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersBulk;

public interface OrdersBulkMapper {
	int deleteByPrimaryKey(String orBulkId);

	int insert(OrdersBulk record);

	OrdersBulk selectByPrimaryKey(String orBulkId);

	int updateByPrimaryKeySelective(OrdersBulk record);

	List<OrdersBulk> getAllOrdersBulk(OrdersBulk ordersBulk);

	int modifyCancle(OrdersBulk ordersBulk);

	OrdersBulk getOrderById(String orBulkId);

	List<OrdersBulk> getOrdersListByStatus(@Param(value = "uid") String uid,
			@Param(value = "statusList") List<Byte> status);

	int modifyOrdersStatus(@Param(value = "orderStatus") Byte status, @Param(value = "ordersId") String ordersId);

	int ordersAutoCancel(@Param(value = "ordersIdList") List<String> ordersIdList);
	
	List<OrdersBulk> searchOrders(String parameter);

}