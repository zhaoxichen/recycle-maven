package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.Orders;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersMapper {

	List<Orders> selectBySearchVo(SearchObjecVo vo);

	int deleteByPrimaryKey(String ordersId);

	int insert(Orders record);

	int insertSelective(Orders record);

	Orders selectByPrimaryKey(String ordersId);

	int updateByPrimaryKeySelective(Orders record);

	int updateByPrimaryKey(Orders record);
}