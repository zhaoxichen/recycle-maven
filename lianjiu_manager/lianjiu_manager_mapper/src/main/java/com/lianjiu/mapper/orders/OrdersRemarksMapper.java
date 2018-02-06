package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersRemarks;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersRemarksMapper {

	int deleteByPrimaryKey(String orRemarksId);

	int insert(OrdersRemarks record);

	int insertSelective(OrdersRemarks record);

	OrdersRemarks selectByPrimaryKey(String orRemarksId);

	int updateByPrimaryKeySelective(OrdersRemarks record);

	int updateByPrimaryKeyWithBLOBs(OrdersRemarks record);

	int updateByPrimaryKey(OrdersRemarks record);

	List<OrdersRemarks> selectBySearchObjecVo(SearchObjecVo vo);
}