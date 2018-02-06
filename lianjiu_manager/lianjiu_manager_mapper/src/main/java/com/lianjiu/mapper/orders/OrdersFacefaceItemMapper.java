package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersFacefaceItem;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersFacefaceItemMapper {

	int deleteByPrimaryKey(String orFfItemId);

	int insert(OrdersFacefaceItem record);

	int insertSelective(OrdersFacefaceItem record);

	OrdersFacefaceItem selectByPrimaryKey(String orFfItemId);

	int updateByPrimaryKeySelective(OrdersFacefaceItem record);

	int updateByPrimaryKey(OrdersFacefaceItem record);

	List<OrdersFacefaceItem> selectBySearchObjecVo(SearchObjecVo vo);
}