package com.lianjiu.rest.controller.user;

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
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.rest.service.user.AllianceBusinessService;

/**
 * 加盟商登陆信息对应AllianceBusiness
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/allianceBusiness")
public class AllianceBusinessController {

	@Autowired
	private AllianceBusinessService allianceBusinessService;

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:加盟商登录,转发到登录系统
	 * 
	 * @param username
	 * @param password
	 * @return String
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String allianceLogin(String username, String password) {
		if (Util.isEmpty(username)) {
			return "请传入正确的username";
		}
		if (Util.isEmpty(username)) {
			return "请传入正确的password";
		}
		return HttpClientUtil.doPost(GlobalValueUtil.SSO_BASE_URL + "alliance/login/" + username + "/" + password);
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:修改手机号码
	 * 
	 * @param phone
	 * @return String
	 */
	@RequestMapping(value = "/phoneCheck/{phone}/{code}", method = RequestMethod.POST)
	@ResponseBody
	public String alliancePhoneCheck(String phone, String code) {
		if (Util.isEmpty(phone)) {
			return "请传入正确的phone";
		}
		if (Util.isEmpty(code)) {
			return "请传入正确的code";
		}
		return HttpClientUtil.doPost(GlobalValueUtil.SSO_BASE_URL + "phoneCheck/" + phone + "/" + code);
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:加盟商修改手机号
	 * 
	 * @param phone
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/phoneModify/{phone}/{code}", method = RequestMethod.POST)
	@ResponseBody
	public String alliancePhoneModify(String phone, String code) {
		if (Util.isEmpty(phone)) {
			return "请传入正确的phone";
		}
		if (Util.isEmpty(code)) {
			return "请传入正确的code";
		}
		return HttpClientUtil.doPost(GlobalValueUtil.SSO_BASE_URL + "phoneModify/" + phone + "/" + code);
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:修改密码
	 * 
	 * @param password
	 * @param code
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/passwordModify/{password}/{code}", method = RequestMethod.POST)
	@ResponseBody
	public String alliancePasswordModify(String password, String code) {
		String reg = "[0-9]{6}";
		if (password.matches(reg)) {
			System.out.println("长度够了");
		} else {
			return "密码长度不匹配";
		}
		if (Util.isEmpty(password)) {
			return "请传入正确的password";
		}
		if (Util.isEmpty(code)) {
			return "请传入正确的code";
		}
		return HttpClientUtil.doPost(GlobalValueUtil.SSO_BASE_URL + "passwordModify/" + password + "/" + code);
	}

	
	/**
	 * 
	 * zhaoxi 2017年10月12日 descrption:开通接单功能 abunes_accept_order
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyOrderAcceptStatus", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateBusinessById(AllianceBusinessDetails details) {
		if (null == details) {
			return LianjiuResult.build(401, "details对象绑定错误");
		}
		return allianceBusinessService.updateBusinessById(details);
	}

	/**
	 * 
	 * zhaoxi 2017年10月12日 descrption:获取加盟商用户基本信息
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectAsset/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectAsset(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return allianceBusinessService.selectAllianceById(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月12日 descrption:获取用户评论信息
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getComment/{userId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectComment(@PathVariable String userId, @PathVariable Integer pageNum,
			@PathVariable Integer pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return allianceBusinessService.selectCommentById(userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年10月12日 descrption:收益明细
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getUserWallet/{userId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectUserWallet(@PathVariable String userId, @PathVariable Integer pageNum,
			@PathVariable Integer pageSize) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return allianceBusinessService.selectUserWalletById(userId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年10月12日 descrption:上门订单
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getFaceOrder/faceFace", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectFaceOrder(Integer pageNum, Integer pageSize, String latitude, String longitude,
			String allianceId) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		if (Util.isEmpty(latitude)) {
			return LianjiuResult.build(403, "未获取当前位置");
		}
		if (Util.isEmpty(longitude)) {
			return LianjiuResult.build(404, "未获取当前位置");
		}
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(405, "请传入正确的allianceId");
		}
		return allianceBusinessService.selectFaceOrderByIds(pageNum, pageSize, latitude, longitude, allianceId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 派单
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/dispatchFaceOrder/dispatch", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult dispatchFaceOrder(String orderFaceId) {
		if (Util.isEmpty(orderFaceId)) {
			return LianjiuResult.build(401, "请传入正确的orderFaceId");
		}
		return allianceBusinessService.dispatchFaceOrder(orderFaceId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 抢单
	 * @param allianceId
	 * @param orFacefaceId
	 * @param radius
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/modifyFaceOrder/modify", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyFaceOrder(String orFacefaceAllianceId, String orFacefaceId) {
		if (Util.isEmpty(orFacefaceAllianceId)) {
			return LianjiuResult.build(403, "请传入正确的orFacefaceAllianceId");
		}
		if (Util.isEmpty(orFacefaceId)) {
			return LianjiuResult.build(403, "请传入正确的orFacefaceId");
		}
		return allianceBusinessService.modifyFaceOrder(orFacefaceAllianceId, orFacefaceId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 已抢订单
	 * @param allianceId
	 * @param orFacefaceId
	 * @param radius
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/alreadyFaceOrder/already", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult alreadyFaceOrder(Integer pageNum, Integer pageSize, String orFacefaceAllianceId,
			Integer orFacefaceStatus) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		if (Util.isEmpty(orFacefaceAllianceId)) {
			return LianjiuResult.build(403, "请传入正确的orFacefaceAllianceId");
		}
		if (null == orFacefaceStatus) {
			return LianjiuResult.build(404, "请传入正确的orFacefaceStatus");
		}
		return allianceBusinessService.alreadyFaceOrder(pageNum, pageSize, orFacefaceAllianceId, orFacefaceStatus);
	}

	/**
	 *
	 * @author whd
	 * @Description: 回收明细单
	 * @param allianceId
	 * @param orFacefaceId
	 * @param radius
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/getOrders/{orOrdersId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getOrders(@PathVariable String orOrdersId) {
		if (Util.isEmpty(orOrdersId)) {
			return LianjiuResult.build(401, "请传入正确的orOrdersId");
		}
		return allianceBusinessService.getOrders(orOrdersId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 修改价格
	 * @param allianceId
	 * @param orFacefaceId
	 * @param radius
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/modifyOrders", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult modifyOrders(OrdersItem ordersItem) {
		if (null == ordersItem) {
			return LianjiuResult.build(401, "ordersItem对象绑定错误");
		}
		return allianceBusinessService.modifyOrders(ordersItem);
	}

	/**
	 *
	 * @author whd
	 * @Description: 质检明细
	 * @param allianceId
	 * @param orFacefaceId
	 * @param radius
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/selectReappraisal/{productId}/id=126", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectReappraisal(@PathVariable String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return allianceBusinessService.selectReappraisal(productId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 质检明细
	 * @param allianceId
	 * @param orFacefaceId
	 * @param radius
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/selectProduct/{orItemsId}/id=126", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectProduct(@PathVariable String orItemsId) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		return allianceBusinessService.selectProductByid(orItemsId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 退出登陆
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/Logout/{alliance}/id=186", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult Logout() {
		return LianjiuResult.build(200, "退出登陆成功");
	}

	/**
	 *
	 * @author whd
	 * @Description: 查是否开通接单
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/isDredge/{allianceId}/id=186", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult isDredge(@PathVariable String allianceId) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入正确的allianceId");
		}
		return allianceBusinessService.selectById(allianceId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 确认修改，更新总价orderfacefaceDetails表
	 * @return LianjiuResult
	 * @date 20172017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/modifyTotalPrice/id=186", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyTotalPrice(@RequestParam("idList") List<String> idList,
			@RequestParam("numList") List<String> numList, @RequestParam("priceList") List<String> priceList,
			String orFfDetailsRetrPrice, String ordersId) {
		if (null == idList) {
			return LianjiuResult.build(401, "idList绑定错误");
		}
		if (null == numList) {
			return LianjiuResult.build(402, "numList绑定错误");
		}
		if (null == priceList) {
			return LianjiuResult.build(403, "priceList绑定错误");
		}
		if (Util.isEmpty(orFfDetailsRetrPrice)) {
			return LianjiuResult.build(404, "请传入正确的orFfDetailsRetrPrice");
		}
		if (Util.isEmpty(ordersId)) {
			return LianjiuResult.build(405, "请传入正确的ordersId");
		}
		System.out.println("---------------");
		System.out.println("idList" + idList);
		System.out.println("numList" + numList);
		System.out.println("priceList" + priceList);
		return allianceBusinessService.modifyTotalPrice(idList, numList, priceList, orFfDetailsRetrPrice, ordersId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 卖家电重新估价
	 * @return LianjiuResult
	 * @date 2017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/reappraisal", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult reappraisal(String orItemsId, String productId, String paramJson, String itemsPrice) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		if (Util.isEmpty(paramJson)) {
			return LianjiuResult.build(402, "请传入正确的paramJson");
		}
		if (Util.isEmpty(itemsPrice)) {
			return LianjiuResult.build(403, "请传入正确的itemsPrice");
		}
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		System.out.println("productId:" + productId);
		return allianceBusinessService.reappraisal(orItemsId, productId, paramJson, itemsPrice);
	}

	/**
	 *
	 * @author whd
	 * @Description: 消息中心
	 * @return LianjiuResult
	 * @date 2017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/messageCenter", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult messageCenter(String allianceId, Integer pageSize, Integer pageNum) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "请传入正确的allianceId");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return allianceBusinessService.messageCenter(allianceId, pageSize, pageNum);
	}

	/**
	 *
	 * @author whd
	 * @Description: 更改消息阅读状态
	 * @return LianjiuResult
	 * @date 2017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/modifyMessageCenter", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyMessageCenter(String messageId) {
		if (Util.isEmpty(messageId)) {
			return LianjiuResult.build(401, "请传入正确的messageId");
		}
		return allianceBusinessService.modifyMessageCenterStatus(messageId);
	}

	/**
	 *
	 * @author whd
	 * @Description: 删除推送消息
	 * @return LianjiuResult
	 * @date 2017年10月16日 下午7:02:11
	 */
	@RequestMapping(value = "/deleMessageCenter", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleMessageCenter(String messageId) {
		if (Util.isEmpty(messageId)) {
			return LianjiuResult.build(401, "请传入正确的messageId");
		}
		return allianceBusinessService.deleMessageCenter(messageId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月23日 descrption:加盟商申请
	 * 
	 * @param allianceBusinessApplication
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/application", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addalliaceApplication(AllianceBusinessApplication allianceBusinessApplication) {
		if (null == allianceBusinessApplication) {
			return LianjiuResult.build(401, "传入对象为空");
		}
		Util.printModelDetails(allianceBusinessApplication);
		return allianceBusinessService.addAlliaceApplication(allianceBusinessApplication);
	}

	/**
	 * 
	 * zhaoxi 2017年11月23日 descrption:校验当前订单是否已经加锁
	 * 
	 * @param allianceBusinessApplication
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult lock(String orderFaceId) {
		if (Util.isEmpty(orderFaceId)) {
			return LianjiuResult.build(401, "订单获取失败");
		}
		return allianceBusinessService.lock(orderFaceId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月23日 descrption:校验当前订单是否已经加锁
	 * 
	 * @param allianceBusinessApplication
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/unLock", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult unLock(String orderFaceId) {
		if (Util.isEmpty(orderFaceId)) {
			return LianjiuResult.build(401, "订单获取失败");
		}
		return allianceBusinessService.unLock(orderFaceId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月23日 descrption:校验当前订单是否已经加锁
	 * 
	 * @param allianceBusinessApplication
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/checkLock", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult checkLock(String orderFaceId) {
		if (Util.isEmpty(orderFaceId)) {
			return LianjiuResult.build(401, "订单获取失败");
		}
		return allianceBusinessService.checkLock(orderFaceId);
	}
	/**
	 * 
	 * hongda 2017年11月23日 descrption:更新加盟定位信息
	 * 
	 * @param allianceBusinessApplication
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateSite", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateSite(String allianceId,String latitude, String longitude) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(401, "加盟商编号获取失败");
		}
		if (Util.isEmpty(latitude)) {
			return LianjiuResult.build(402, "加盟商定位获取失败");
		}
		if (Util.isEmpty(longitude)) {
			return LianjiuResult.build(402, "加盟商定位获取失败");
		}
		return allianceBusinessService.updateSite(allianceId,latitude,longitude);
	}
}
