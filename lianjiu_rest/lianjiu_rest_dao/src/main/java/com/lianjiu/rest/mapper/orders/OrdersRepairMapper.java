package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersRepairMapper {

	int deleteByPrimaryKey(String orRepairId);

	int insert(OrdersRepair record);

	int insertSelective(OrdersRepair record);

	OrdersRepair selectByPrimaryKey(String orRepairId);

	int updateByPrimaryKeySelective(OrdersRepair record);

	int updateByPrimaryKey(OrdersRepair record);

	List<OrdersRepair> selectBySearchObjecVo(SearchObjecVo vo);

	List<OrdersRepair> getRepairByUserId(String userId);

	int updateOrderStatus(@Param(value = "orRepairId") String ordersId, @Param(value = "status") Byte status);

	List<OrdersRepair> getRepairByUserId(@Param(value = "uid") String uid, @Param(value = "status") Byte status);

	List<OrdersRepair> getRepairByStatus(@Param(value = "uid") String uid,
			@Param(value = "statusList") List<Byte> status);

	List<OrdersRepair> selectAll();

	List<OrdersRepair> vagueQuery(@Param(value = "repair") OrdersRepair ordersRepair,@Param(value = "cratedStart") String cratedStart,
			@Param(value = "cratedOver") String cratedOver,@Param(value = "handleStart") String handleStart,@Param(value = "handleOven") String handleOven);
}