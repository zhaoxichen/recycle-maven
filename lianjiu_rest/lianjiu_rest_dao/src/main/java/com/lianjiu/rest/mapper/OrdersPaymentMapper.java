package com.lianjiu.rest.mapper;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersPayment;

public interface OrdersPaymentMapper {

	int insert(OrdersPayment ordersPayment);

	int updateByPrimaryKeySelective(OrdersPayment ordersPayment);

	OrdersPayment selectByPrimaryKey(String id);

	int updateOrdersPrice(@Param("id") String id, @Param("price") String price);
}