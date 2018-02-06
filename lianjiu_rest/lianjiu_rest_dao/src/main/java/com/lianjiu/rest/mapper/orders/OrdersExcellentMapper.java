package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.rest.model.OrdersView;

public interface OrdersExcellentMapper {

	int deleteByPrimaryKey(String orExcellentId);

	int insert(OrdersExcellent record);

	OrdersExcellent selectByPrimaryKey(String orExcellentId);

	int selectByPrimaryKeyCheck(String orExcellentId);

	int updateByPrimaryKeySelective(OrdersExcellent record);

	int updateByPrimaryKeyWithBLOBs(OrdersExcellent record);

	int updateByPrimaryKey(OrdersExcellent record);

	int modifyExcellentState(OrdersExcellent excellent);

	int modifyOrdersState(@Param(value = "ordersId") String ordersId, @Param(value = "status") Byte status);

	int modifyExcellentHandleTime(OrdersExcellent excellent);

	List<OrdersExcellent> getByExcellentList(long categoryId);

	Long selectCategoryId(String categoryName);

	List<OrdersExcellent> findAll();

	List<OrdersExcellent> findAllByStatus(OrdersExcellent excellent);

	List<OrdersView> getExcellentByUserId(String userId);

	List<OrdersView> getExcellentByUserStatus(@Param(value = "userId") String userId,
			@Param(value = "statusList") List<Byte> statusList);

	int insertOrders(@Param(value = "ordersExcellent") OrdersExcellent ordersExcellent,
			@Param(value = "addressId") String addressId);

	String slectPriceByOrderNo(String outTradeNo);

	int ordersAutoCancel(@Param(value = "ordersIdList") List<String> ordersIdList);
	
	int modifyOrdersStatus(@Param("status") Byte status, @Param("ordersId") String ordersId);

	int modifyExcellentFinishState(OrdersExcellent excellent);
	
	Byte getExcellentByState(String excellentId);

	List<OrdersExcellent> selectBySearchObjecVo(Object object);

	Byte getOrdersStatus(String ordersId);

	List<OrdersExcellent> vagueQuery(@Param(value = "excellent") OrdersExcellent ordersExcellent,@Param(value = "cratedStart") String cratedStart,@Param(value = "cratedOver") String cratedOver);
	
}