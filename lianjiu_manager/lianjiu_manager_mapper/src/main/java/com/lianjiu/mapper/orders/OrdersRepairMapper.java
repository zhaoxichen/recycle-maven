package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersRepair;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersRepairMapper {

	int deleteByPrimaryKey(String orRepairId);

	int insert(OrdersRepair record);

	int insertSelective(OrdersRepair record);

	OrdersRepair selectByPrimaryKey(String orRepairId);

	int updateByPrimaryKeySelective(OrdersRepair record);

	int updateByPrimaryKey(OrdersRepair record);
	
	List<OrdersRepair> selectAll();

	List<OrdersRepair> selectBySearchObjecVo(SearchObjecVo vo);
}