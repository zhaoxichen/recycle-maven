package com.lianjiu.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersExcellentMapper {

	int deleteByPrimaryKey(String orExcellentId);

	int insert(OrdersExcellent record);

	int insertSelective(OrdersExcellent record);

	OrdersExcellent selectByPrimaryKey(String orExcellentId);

	int updateByPrimaryKeySelective(OrdersExcellent record);

	int updateByPrimaryKeyWithBLOBs(OrdersExcellent record);

	int updateByPrimaryKey(OrdersExcellent record);
	
	List<OrdersExcellent> selectBySearchObjecVo(SearchObjecVo vo);
	
	int modifyOrdersStatus(@Param("status") Byte status, @Param("ordersId") String ordersId);
}