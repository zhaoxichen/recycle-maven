package com.lianjiu.order.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.order.service.OrderFacefaceService;

/**
 * 上门回收，当面交易
 * 
 * @author zyx
 *
 */
@Controller
@RequestMapping("/orderFaceface")
public class OrderFaceFaceController {

	@Autowired
	private OrderFacefaceService orderFacefaceService;

	/**
	 * 
	 * zhaoxi 2017年10月30日 descrption:添加进回收车。放入redis并返回redis中所有的数据
	 * 
	 * @param userId
	 * @param wasteItem
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/introductionFaceface", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult introductionFaceface(String userId, OrdersItem productItem) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (null == productItem) {
			return LianjiuResult.build(402, "productItem对象绑定错误");
		}
		productItem.setOrItemsChooseFlag(0);
		return orderFacefaceService.introductionFaceface(userId, productItem);
	}

	/**
	 * 
	 * zyx 2017年9月13日 descrption:根据主键id查询
	 * 
	 * @param facefaceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceface/{facefaceId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFacefaceByState(@PathVariable String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(401, "请传入正确的facefaceId");
		}
		return orderFacefaceService.getFacefaceById(facefaceId);
	}

	/**
	 * 
	 * zyx 2017年9月13日 descrption:添加上门回收订单
	 * 
	 * @param faceface
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult submit(OrdersFaceface faceface) {
		if (null == faceface) {
			return LianjiuResult.build(401, "faceface对象绑定错误");
		}
		// AD1508123552447
		if (Util.isEmpty(faceface.getAddressId()) || !faceface.getAddressId().matches(GlobalValueUtil.REGEX_USER_ADDRESS)) {
			return LianjiuResult.build(402, "请传入正确的addressId");
		}
		if (Util.isEmpty(faceface.getUserId()) || !faceface.getUserId().matches(GlobalValueUtil.REGEX_USER_ID)) {
			return LianjiuResult.build(403, "请传入正确的userId");
		}
		if (null == faceface.getOrFacefaceVisittime()) {
			return LianjiuResult.build(404, "请传入预约时间，格式为 yy-hh-dd");
		}
		Util.printModelDetails(faceface);
		try {
			return orderFacefaceService.submit(faceface);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LianjiuResult.build(402, "异常错误");
		}
	}

	/**
	 * 
	 * zyx 2017年9月13日 descrption:更新上门回收订单
	 * 
	 * @param faceface
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyFaceface", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateFaceface(OrdersFaceface faceface) {
		if (null == faceface) {
			return LianjiuResult.build(401, "faceface对象绑定错误");
		}
		return orderFacefaceService.updateFaceface(faceface);
	}

	/**
	 * 
	 * zyx 2017年9月13日 descrption:删除上门回收订单
	 * 
	 * @param facefaceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeFaceface/{facefaceId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteFaceface(@PathVariable String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(401, "请传入正确的facefaceId");
		}
		return orderFacefaceService.deleteFaceface(facefaceId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:
	 * 
	 * @param orFaceItemId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getItem/{orFaceItemId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrderItemsById(@PathVariable String orFaceItemId) {
		if (Util.isEmpty(orFaceItemId)) {
			return LianjiuResult.build(401, "请传入正确的orFaceItemId");
		}
		return orderFacefaceService.getOrderItemsById(orFaceItemId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:获取用户验机参数
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "getParam/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getParamData(@PathVariable String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return orderFacefaceService.getParamData(productId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月15日 descrption:修改用户验机参数
	 * 
	 * @param orItemsId
	 * @param orItemsParamModify
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyParam", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyParam(String orItemsId, String orItemsParamModify) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		if (Util.isEmpty(orItemsParamModify)) {
			return LianjiuResult.build(402, "请传入正确的orItemsParamModify");
		}
		return orderFacefaceService.modifyParam(orItemsId, orItemsParamModify);
	}

	/**
	 * 
	 * zxy 2017年9月15日 descrption:查询当前用户所有上门回收订单
	 * 
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getHomeVistList/{userId}/{pageNum}/{pageSize}/userId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getHomeVistList(@PathVariable String userId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getHomeVistList(userId, pageNum, pageSize);

	}

	/**
	 * 
	 * zxy 2017年10月10日 descrption:添加上门预约时间
	 * 
	 * @param orFacefaceId
	 * @param visitTime
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addHomeVisitTime/{orFacefaceId}/{visitTime}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult addHomeVisitTime(@PathVariable String orFacefaceId, @PathVariable String visitTime) {
		if (Util.isEmpty(orFacefaceId)) {
			return LianjiuResult.build(401, "请传入正确的orFacefaceId");
		}
		if (Util.isEmpty(visitTime)) {
			return LianjiuResult.build(401, "请传入正确的visitTime");
		}
		return orderFacefaceService.addHomeVisitTime(orFacefaceId, visitTime);
	}

	/**
	 * 
	 * zxy 2017年10月11日 descrption:用户各种订单运行状态查询
	 * 
	 * @param userId
	 * @param type
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getHomeVistStutsList/{userId}/{type}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getHomeVistStutsList(@PathVariable String userId, @PathVariable Integer type,
			@PathVariable int pageNum, @PathVariable int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getHomeVistStutsList(userId, type, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年11月3日 descrption:
	 * 
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getHomeVistStutsList/{userId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getHomeVistStutsList(@PathVariable String userId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getHomeVistList(userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zxy 2017年10月12日 descrption:查询订单中所有产品详情页
	 * 
	 * @param orFacefaceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectOrderDetails/{ordersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectOrderDetails(@PathVariable String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFacefaceService.selectFaceFaceItemByOrderId(ordersId);
	}

	/**
	 * 
	 * zxy 2017年10月12日 descrption:确认修改价格
	 * 
	 * @param ordersFacefaceDetails
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyVisitPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyVisitPrice(OrdersFacefaceDetails ordersFacefaceDetails, Byte orFacefaceStatus) {
		if (null == ordersFacefaceDetails) {
			return LianjiuResult.build(401, "ordersFacefaceDetails对象绑定错误");
		}
		if (null == orFacefaceStatus) {
			return LianjiuResult.build(402, "orFacefaceStatus绑定错误");
		}
		return orderFacefaceService.modifyVisitPrice(ordersFacefaceDetails, orFacefaceStatus);
	}

	/**
	 * 
	 * zhaoxi 2017年11月14日 descrption:不同意价格
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/priceRefuse", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult orderPriceRefuse(String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFacefaceService.orderPriceRefuse(ordersId);
	}

	/**
	 * 
	 * zxy 2017年10月17日 descrption:质检明细
	 * 
	 * @param orItemsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/qualityCheckingDetails/{orItemsId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult qualityCheckingDetails(@PathVariable String orItemsId) {
		return orderFacefaceService.qualityCheckingDetails(orItemsId);
	}

	/**
	 * 
	 * huangfu 2017年10月11日 descrption:根据状态查询
	 * 
	 * @param state
	 *            pageNum pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceFaceByState/{state}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFaceFaceByState(@PathVariable Byte state, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (null == state) {
			return LianjiuResult.build(401, "state参数错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getFaceFaceByState(state, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年10月11日 descrption:修改订单状态
	 * 
	 * @param facefaceId
	 *            state
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateFaceFaceState", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateFaceFaceState(String facefaceId, Byte state) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(401, "请传入正确的facefaceId");
		}
		if (null == state) {
			return LianjiuResult.build(402, "state参数错误");
		}
		return orderFacefaceService.updateFaceFaceState(facefaceId, state);
	}

	/**
	 * 
	 * huangfu 2017年10月11日 descrption:获取当前订单所有的商品
	 * 
	 * @param facefaceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getByOrdersItem/{facefaceId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getByOrdersItem(@PathVariable String facefaceId) {
		if (Util.isEmpty(facefaceId)) {
			return LianjiuResult.build(401, "请传入正确的facefaceId");
		}
		return orderFacefaceService.getByOrdersItem(facefaceId);
	}

	/**
	 * 
	 * huangfu 2017年10月12日 descrption:加单
	 * 
	 * @param userId
	 *            ordersItem
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addWaste", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addWaste(String userId, OrdersItem ordersItem) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (ordersItem == null) {
			return LianjiuResult.build(402, "ordersItem对象绑定错误");
		}
		if (Util.isEmpty(ordersItem.getOrItemsName())) {
			return LianjiuResult.build(403, "请传入正确的废品名称");
		}
		if (Util.isEmpty(ordersItem.getOrItemsPicture())) {
			return LianjiuResult.build(404, "请传入正确的图片地址");
		}
		if (Util.isEmpty(ordersItem.getOrItemsProductId())) {
			return LianjiuResult.build(405, "请传入正确的报废信息编号");
		}
		if (null == ordersItem.getOrItemsPriceUnit()) {
			// 上门回收单位默认是台
			ordersItem.setOrItemsPriceUnit((long) 4);
		}
		if (Util.isEmpty(ordersItem.getOrItemsPrice())) {
			return LianjiuResult.build(407, "请传入正确的单价");
		}
		if (null == ordersItem.getOrItemsNum()) {
			return LianjiuResult.build(408, "请传入正确的数量");
		}
		System.out.println("-------------:" + ordersItem.getOrItemsPriceUnit());
		return orderFacefaceService.addWaste(userId, ordersItem);
	}

	/**
	 * 
	 * huangfu 2017年10月11日 descrption:查询该用户所有回收的订单
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWasteCar/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getWasteCar(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return orderFacefaceService.getWasteCar(userId);
	}

	/**
	 * 
	 * huangfu 2017年10月27日 descrption:删除redis中数据，
	 * 
	 * @param userId
	 * @param orItemsProductId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteWasteCar", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteWasteCar(String userId, String orItemsProductId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (Util.isEmpty(orItemsProductId)) {
			return LianjiuResult.build(402, "请传入正确的orItemsProductId");
		}
		return orderFacefaceService.deleteWasteCar(userId, orItemsProductId);
	}

	/**
	 * 
	 * huangfu 2017年10月12日 descrption:加单车结算
	 * 
	 * @param userId
	 * @param orFacefaceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/submitFace", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult submitFace(String userId, String orFacefaceId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (Util.isEmpty(orFacefaceId)) {
			return LianjiuResult.build(402, "请传入正确的orFacefaceId");
		}
		return orderFacefaceService.submitFace(userId, orFacefaceId);
	}

	/**
	 * 
	 * huangfu 2017年10月11日 descrption:查询该用户所有回收的订单
	 * 
	 * @param userId
	 *            pageNum pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceFaceByUserId/{userId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFaceFaceByUserId(@PathVariable String userId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getFaceFaceAll(userId, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年10月11日 descrption:查询该用户所有回收的订单
	 * 
	 * @param pageNum
	 *            pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceFace/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFaceFaceAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getFaceFaceAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年10月30日 descrption:
	 * 
	 * @param itemIdList
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult productBalance(@RequestParam("itemIdList") List<String> itemIdList, String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (null == itemIdList || itemIdList.size() == 0) {
			return LianjiuResult.build(401, "请传入正确的itemIdList");
		}
		return orderFacefaceService.productBalance(itemIdList, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:取结算之后的数据
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/balanceAfter/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult productBalanceAfter(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return orderFacefaceService.productBalanceAfter(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:从回收车中删除
	 * 
	 * @param itemId
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/reduceProductFromCar", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult reduceProductFromCar(String itemId, String userId) {
		if (Util.isEmpty(itemId)) {
			return LianjiuResult.build(401, "请传入正确的itemId");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		return orderFacefaceService.reduceProductFromCar(itemId, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:直接上门回收,买家电
	 * 
	 * @param productItem
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/directSubmit", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult directHomeVisit(OrdersItem productItem, String userId) {
		if (null == productItem) {
			return LianjiuResult.build(401, "productItem对象绑定错误");
		}
		if (Util.isEmpty(productItem.getOrItemsParam())) {
			return LianjiuResult.build(402, "param不能为空");
		}
		if (null == productItem.getOrItemsPriceUnit()) {
			productItem.setOrItemsPriceUnit((long) 4);
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(403, "请传入正确的userId");
		}
		productItem.setOrItemsVolume((long) 1);// 体积预存
		return orderFacefaceService.directSubmit(productItem, userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月31日 descrption:直接上门回收，卖废品
	 * 
	 * @param productItem
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/directSubmitWaste", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult directWaste(OrdersItem productItem, String userId) {
		if (null == productItem) {
			return LianjiuResult.build(401, "productItem对象绑定错误");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		if (Util.isEmpty(productItem.getOrItemsName())) {
			return LianjiuResult.build(403, "请传入正确的废品名称");
		}
		if (Util.isEmpty(productItem.getOrItemsPicture())) {
			return LianjiuResult.build(404, "请传入正确的图片地址");
		}
		if (null == productItem.getOrItemsStemFrom()) {
			return LianjiuResult.build(405, "请传入正确的来源");
		}
		if (null == productItem.getOrItemsPriceUnit()) {
			return LianjiuResult.build(406, "请传入正确的单位");
		}
		if (Util.isEmpty(productItem.getOrItemsPrice())) {
			return LianjiuResult.build(407, "请传入正确的单价");
		}
		if (null == productItem.getOrItemsNum()) {
			return LianjiuResult.build(408, "请传入正确的数量");
		}
		productItem.setOrItemsVolume((long) 1);// 体积预存
		return orderFacefaceService.directSubmitWaste(productItem, userId);
	}
}
