package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersExpressItem;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersExpressItemMapper {

	int deleteByPrimaryKey(String orExpItemId);

	int insert(OrdersExpressItem record);

	int insertSelective(OrdersExpressItem record);

	OrdersExpressItem selectByPrimaryKey(String orExpItemId);

	int updateByPrimaryKeySelective(OrdersExpressItem record);

	int updateByPrimaryKey(OrdersExpressItem record);

	List<OrdersExpressItem> selectBySearchObjecVo(SearchObjecVo vo);
}