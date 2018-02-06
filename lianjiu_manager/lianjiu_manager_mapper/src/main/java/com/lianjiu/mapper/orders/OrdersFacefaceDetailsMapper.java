package com.lianjiu.mapper.orders;

import java.util.List;

import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersFacefaceDetailsMapper {

	int deleteByPrimaryKey(String orFfDetailsId);

	int insert(OrdersFacefaceDetails record);

	int insertSelective(OrdersFacefaceDetails record);

	OrdersFacefaceDetails selectByPrimaryKey(String orFfDetailsId);

	int updateByPrimaryKeySelective(OrdersFacefaceDetails record);

	int updateByPrimaryKey(OrdersFacefaceDetails record);

	List<OrdersFacefaceDetails> selectBySearchObjecVo(SearchObjecVo vo);

	OrdersFacefaceDetails selectByOrdersId(String ordersId);
}