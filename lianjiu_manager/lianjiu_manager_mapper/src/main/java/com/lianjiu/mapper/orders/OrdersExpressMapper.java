package com.lianjiu.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersExpressMapper {

	int deleteByPrimaryKey(String orExpressId);

	int insert(OrdersExpress record);

	int insertSelective(OrdersExpress record);

	OrdersExpress selectByPrimaryKey(String orExpressId);

	int updateByPrimaryKeySelective(OrdersExpress record);

	int updateByPrimaryKey(OrdersExpress record);

	List<OrdersExpress> selectBySearchObjecVo(SearchObjecVo vo);

	int modifyOrdersStatus(@Param("status") Byte status, @Param("ordersId") String ordersId);

	int updateOrdersToFinish(@Param("ordersId") String ordersId, @Param("status") Byte orderExpressBalanceYes);
}