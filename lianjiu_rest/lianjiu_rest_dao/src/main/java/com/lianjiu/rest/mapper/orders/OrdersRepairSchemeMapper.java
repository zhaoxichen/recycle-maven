package com.lianjiu.rest.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersRepairScheme;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersRepairSchemeMapper {

	int deleteByPrimaryKey(String orReSchemeId);

	int insert(OrdersRepairScheme record);

	int insertSelective(OrdersRepairScheme record);

	OrdersRepairScheme selectByPrimaryKey(String orReSchemeId);

	int updateByPrimaryKeySelective(OrdersRepairScheme record);

	int updateByPrimaryKeyWithBLOBs(OrdersRepairScheme record);

	int updateByPrimaryKey(OrdersRepairScheme record);

	List<OrdersRepairScheme> selectBySearchObjecVo(SearchObjecVo vo);

	OrdersRepairScheme selectSchameByOrdersId(String ordersId);

	void updateByOrdersId(OrdersRepairScheme scheme);
}