package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersExpressDetails;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersExpressDetailsMapper {

	int deleteByPrimaryKey(String orExpDetailsId);

	int insert(OrdersExpressDetails record);

	int insertSelective(OrdersExpressDetails record);

	OrdersExpressDetails selectByPrimaryKey(String orExpDetailsId);

	int updateByPrimaryKeySelective(OrdersExpressDetails record);

	int updateByPrimaryKey(OrdersExpressDetails record);

	List<OrdersExpressDetails> selectBySearchObjecVo(SearchObjecVo vo);
}