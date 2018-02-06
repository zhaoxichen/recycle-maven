package com.lianjiu.rest.mapper.orders;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.vo.SearchObjecVo;

public interface OrdersFacefaceDetailsMapper {

	int deleteByPrimaryKey(String orFfDetailsId);

	int insert(OrdersFacefaceDetails record);

	int insertSelective(OrdersFacefaceDetails record);

	OrdersFacefaceDetails selectByPrimaryKey(String orFfDetailsId);

	int updateByPrimaryKeySelective(OrdersFacefaceDetails record);

	int updateByPrimaryKey(OrdersFacefaceDetails record);

	int modifyVisitPrice(OrdersFacefaceDetails ordersFacefaceDetails);

	OrdersFacefaceDetails selectByFacefaceId(String facefaceId);

	int modifyRetrPrice(@Param("orFfDetailsRetrPrice") String orFfDetailsRetrPrice,
			@Param("orFacefaceId") String ordersId);

	String slectPriceByOrderNo(String outTradeNo);

	List<OrdersFacefaceDetails> selectBySearchObjecVo(SearchObjecVo vo);

	OrdersFacefaceDetails selectByOrdersId(String ordersId);

	String getRetrPriceByOid(String orderId); 

}