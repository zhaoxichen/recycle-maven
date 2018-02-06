package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersExpress;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.model.OrdersExpressItemVo;

public interface OrdersExpressMapper {

	int deleteByPrimaryKey(String orExpressId);

	int insert(OrdersExpress record);

	OrdersExpress selectByPrimaryKey(String orExpressId);

	int updateByPrimaryKeySelective(OrdersExpress record);

	int updateByPrimaryKey(OrdersExpress record);

	List<OrdersExpress> selectExpressItemByOrdersId(String userId);
	
	List<OrdersExpressItemVo> selectExpressByUserId(String userId);

	int updateExpressNum(OrdersExpress ordersExpress);

	int updateExpressSubmit(OrdersExpress ordersExpress);

	List<OrdersExpressItemVo> selectListByUserId(String userId);

	List<OrdersExpressItemVo> getExpressStutsList(OrdersExpress ordersExpress);

	int ModifyExpressOrderStatus(OrdersExpress ordersExpress);
	
	int ordersAutoCancel(@Param(value = "ordersIdList") List<String> ordersIdList);

	List<OrdersExpress> selectBySearchObjecVo(SearchObjecVo vo);

	int modifyOrdersStatus(@Param("status") Byte status, @Param("ordersId") String ordersId);

	int updateOrdersToFinish(@Param("ordersId") String ordersId, @Param("status") Byte orderExpressBalanceYes);

	List<OrdersExpress> vagueQuery(@Param("express") OrdersExpress ordersExpress,@Param("cratedStart")  String cratedStart,@Param("cratedOver") String cratedOver);

}