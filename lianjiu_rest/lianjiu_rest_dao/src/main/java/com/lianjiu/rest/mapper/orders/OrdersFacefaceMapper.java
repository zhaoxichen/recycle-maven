package com.lianjiu.rest.mapper.orders;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.model.OrdersFacefaceFull;
import com.lianjiu.rest.model.OrdersFacefaceItemVo;

public interface OrdersFacefaceMapper {

	int deleteByPrimaryKey(String orFacefaceId);

	int insert(OrdersFaceface record);

	OrdersFaceface selectByPrimaryKey(String orFacefaceId);

	int updateByPrimaryKeySelective(OrdersFaceface record);

	int updateByPrimaryKeyWithBLOBs(OrdersFaceface record);

	int updateByPrimaryKey(OrdersFaceface record);

	List<OrdersFacefaceFull> selectListByUserId(String userId);

	int addHomeVisitTime(String orFacefaceId, String visitTime);

	List<OrdersFacefaceFull> getHomeVistStutsList(OrdersFaceface info);

	List<OrdersFacefaceFull> getOrdersListByStatus(@Param(value = "uid") String uid,
			@Param(value = "statusList") List<Byte> status);

	List<OrdersFaceface> findAll();

	List<OrdersFaceface> findByUserId(String userId);

	List<OrdersFaceface> getFaceFaceByState(Byte state);

	int selectByOrdersFacefaceCheck(String facefaceId);

	int updateByStatus(OrdersFaceface ordersFaceface);

	int updateFacefaceBrothersId(OrdersFaceface ordersFaceface);

	int updateFacefaceBySubmit(OrdersFaceface ordersFaceface);

	int updateFaceFaceState(OrdersFaceface ordersFaceface);

	List<OrdersFacefaceItemVo> selectFacefaceByUserId(String userId);

	List<OrdersFaceface> findAll(Integer radius, String allianceId);

	List<OrdersFaceface> selectOrdersByallianceId(@Param(value = "allianceId") String allianceId,
			@Param(value = "statusList") List<Byte> statusList);

	List<OrdersFaceface> selectAll();

	void updateByPrimaryKey(String orders);

	int updateFaceFaceStates(OrdersFaceface ordersFaceface);

	List<OrdersFaceface> selectByOrdersId(String orOrdersId);

	List<OrdersFaceface> findFaceAll(String allianceId);

	List<OrdersFacefaceFull> selectByAddressId(String userAddressId);

	OrdersFaceface getMessage(String orFacefaceId);

	Integer getOrdersStatusByAcliacneId(String orFacefaceAllianceId);

	String selectAddressIdById(String orderFaceId);

	List<String> getByaId(String allianceId);

	int deleteById(String faceId);

	int deleteByaId(String allianceId);

	// 取消价格
	int orderPriceRefuse(@Param("orFacefaceId") String orFacefaceId, @Param("updated") Date updated);

	OrdersFaceface selectFullByPrimaryKey(String orFacefaceId);

	int ordersAutoCancel(@Param(value = "ordersIdList") List<String> ordersIdList);

	int modifyOrdersStatus(@Param("status") Byte status, @Param("ordersId") String ordersId);

	// 查订单状态
	Integer getOrdersStatusByOrdersId(@Param("orFacefaceId") String ordersId);

	List<OrdersFaceface> getOrdersFaceLL();

	// 根据订单编号查相应加盟商编号
	String getAllianceIDById(String orFacefaceId);

	int updateForOrderStatusReduction(@Param("ordersIdList") List<String> ordersIdList);

	int updateFaceFaceFinishState(OrdersFaceface orders);

	int getCancelCountByAllianceId(String orFacefaceAllianceId);

	List<OrdersFaceface> selectBySearchObjecVo(SearchObjecVo vo);

	List<OrdersFaceface> selectBySearchFilter(SearchObjecVo vo);

	// 获取订单当前的状态
	Byte selectOrdersStatus(String ordersId);

	int ordersCancel(String ordersId);

	List<OrdersFaceface> vagueQuery(@Param(value="faceface") OrdersFaceface faceface,@Param(value="cratedStart")  String cratedStart,@Param(value="cratedOver")  String cratedOver);
}