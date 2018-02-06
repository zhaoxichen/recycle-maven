package com.lianjiu.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.service.order.OrderFacefaceService;

/**
 * 上门回收，当面交易
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/orderFaceface")
public class OrderFaceFaceController {

	@Autowired
	private OrderFacefaceService orderFacefaceService;

	/**
	 * 
	 * huangfu 2017年9月13日 descrption:分页查询所有上门回收订单
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceface/{pageNum}/{pageSize}/All", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFacefaceAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getFacefaceAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年11月13日 descrption:订单过滤器
	 * 
	 * @param orders
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/filter", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersFilter(OrdersFaceface orders, @RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "15") int pageSize) {
		if (null == orders) {
			return LianjiuResult.build(401, "orders对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.ordersFilter(orders, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月13日 descrption:获取当前分类的所有上门回收订单，分页
	 * 
	 * @param State
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceface/{categoryId}/{pageNum}/{pageSize}/ByCategoryId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFacefaceByState(@PathVariable Long categoryId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (null == categoryId) {
			return LianjiuResult.build(401, "categoryId对象绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getFacefaceByCategoryId(categoryId, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月13日 descrption:获取当前分类的所有上门回收订单
	 * 
	 * @param State
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceface/{allianceId}/ByAllianceId/All", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getFacefaceByAllianceId(@PathVariable String allianceId) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入正确的allianceId");
		}
		return orderFacefaceService.getFacefaceByAllianceId(allianceId);
	}

	/**
	 * 
	 * huangfu 2017年9月13日 descrption:获取当前分类的所有上门回收订单，分页
	 * 
	 * @param State
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceface/{allianceId}/{pageNum}/{pageSize}/ByAllianceId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getFacefaceByAllianceId(@PathVariable String allianceId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入正确的allianceId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getFacefaceByAllianceId(allianceId, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2017年9月13日 descrption:根据主键id查询
	 * 
	 * @param FacefaceId
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
	 * huangfu 2017年9月13日 descrption:更新上门回收订单
	 * 
	 * @param Faceface
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
	 * zhaoxi 2017年9月15日 descrption: 查询本订单的详情
	 * 
	 * @param ordersId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getItem/{ordersId}/{pageNum}/{pageSize}/ByOrdersId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getItemByOrdersId(@PathVariable String ordersId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.getItemByOrdersId(ordersId, pageNum, pageSize);
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
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "modifyParam", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyParam(String orItemsId, String orItemsParamModify) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		if (Util.isEmpty(orItemsParamModify)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return orderFacefaceService.modifyParam(orItemsId, orItemsParamModify);
	}

	/**
	 * 
	 * zhaoxi 2018年1月5日 descrption:取消废弃已久的订单
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/ordersExpireCancel", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ordersExpireCancel(String ordersId) {
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(401, "请传入正确的ordersId");
		}
		return orderFacefaceService.ordersExpireCancel(ordersId);
	}
	
	/**
	 * 
	 * huangfu 2017年1月9日 descrption:上门订单模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(OrdersFaceface faceface, int pageNum,int pageSize,String cratedStart,String cratedOver) {
//		if(null == ordersRepair){
//			return LianjiuResult.build(401, "ordersRepair对象绑定错误");
//		}
		System.out.println("faceface getOrFacefaceId："+faceface.getOrFacefaceId());
		System.out.println("faceface getUserId："+faceface.getUserId());
		System.out.println("faceface getUserPhone："+faceface.getUserPhone());
		System.out.println("faceface getOrFacefaceStatus："+faceface.getOrFacefaceStatus());
		System.out.println("cratedStart："+cratedStart);
		System.out.println("cratedOver："+cratedOver);
		System.out.println("pageNum："+pageNum);
		System.out.println("pageSize："+pageSize);
		
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return orderFacefaceService.vagueQuery(faceface, pageNum,pageSize,cratedStart,cratedOver);
	}
	
}
