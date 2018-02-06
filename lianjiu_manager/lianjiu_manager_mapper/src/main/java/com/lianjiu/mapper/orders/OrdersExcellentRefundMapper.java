package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersExcellentRefundMapper {

	int deleteByPrimaryKey(String orExceRefundId);

	int insert(OrdersExcellentRefund record);

	int insertSelective(OrdersExcellentRefund record);

	OrdersExcellentRefund selectByPrimaryKey(String orExceRefundId);

	int updateByPrimaryKeySelective(OrdersExcellentRefund record);

	int updateByPrimaryKeyWithBLOBs(OrdersExcellentRefund record);

	int updateByPrimaryKey(OrdersExcellentRefund record);
	
	List<OrdersExcellentRefund> selectBySearchObjecVo(SearchObjecVo vo);
	
	List<OrdersExcellentRefund> selectRefoundList();

	int modifyStatus(OrdersExcellentRefund record);
}