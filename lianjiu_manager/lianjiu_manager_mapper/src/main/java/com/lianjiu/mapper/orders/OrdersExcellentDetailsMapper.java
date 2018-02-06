package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersExcellentDetails;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersExcellentDetailsMapper {

	int deleteByPrimaryKey(String orExceDetailsId);

	int insert(OrdersExcellentDetails record);

	int insertSelective(OrdersExcellentDetails record);

	OrdersExcellentDetails selectByPrimaryKey(String orExceDetailsId);

	int updateByPrimaryKeySelective(OrdersExcellentDetails record);

	int updateByPrimaryKeyWithBLOBs(OrdersExcellentDetails record);

	int updateByPrimaryKey(OrdersExcellentDetails record);
	
	List<OrdersExcellentDetails> selectBySearchObjecVo(SearchObjecVo vo);
	
}