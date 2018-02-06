package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersRepairItem;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersRepairItemMapper {

	int deleteByPrimaryKey(String orReItemId);

	int insert(OrdersRepairItem record);

	int insertSelective(OrdersRepairItem record);

	OrdersRepairItem selectByPrimaryKey(String orReItemId);

	int updateByPrimaryKeySelective(OrdersRepairItem record);

	int updateByPrimaryKeyWithBLOBs(OrdersRepairItem record);

	int updateByPrimaryKey(OrdersRepairItem record);

	List<OrdersRepairItem> selectBySearchObjecVo(SearchObjecVo vo);
}