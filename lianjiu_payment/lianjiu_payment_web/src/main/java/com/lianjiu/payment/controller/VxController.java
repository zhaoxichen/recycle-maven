package com.lianjiu.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.UserDetails;
import com.lianjiu.payment.service.VXService;

@RequestMapping("/vxController")
@Controller
public class VxController {

	@Autowired
	private VXService vxService;
	
	@Autowired
	private static Logger loggerVisit = Logger.getLogger("visit");

	/**
	 * 微信支付
	 * 
	 * @param money
	 * @param orderNo
	 * @param userId
	 * @param type
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "vxPay", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vxPay(HttpServletRequest request, HttpServletResponse response, String money,
			String userId, String orderNo,String type) {
		loggerVisit.info("微信h5支付 统一下单接口:vxPay");
		if (StringUtil.hasEmpty(money, userId, orderNo,type)) {
			return LianjiuResult.build(401, "参数不能为空");
		}
		//1=微信内支付      2为微信外支付
		if(type.equals("1")){
			return vxService.vxInnerPay(request,response,money,userId,orderNo);
		}else if(type.equals("2")){
			return vxService.vxOutPay(request,response,money,userId,orderNo);
		}else{
			return LianjiuResult.build(402, "支付方式出现问题");
		}
		
	}

	/**
	 * 微信内支付回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "vxInnerPayReturn", method = RequestMethod.GET)
	@ResponseBody
	public synchronized LianjiuResult vxInnerPayReturn(HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信内支付回调:vxInnerPayReturn");
		return vxService.vxInnerPayReturn(request,response);
	}
	
	/**
	 * 微信支付回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "vxPayReturn", method = RequestMethod.POST)
	@ResponseBody
	public void vxPayReturn(HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信支付回调:vxPayReturn");
		vxService.vxPayReturn(request,response);
		
	}

	/**
	 * 订单信息查询
	 * 
	 * @param money
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "vxPayQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vxPayQuery(String orderNo,HttpServletRequest request,HttpServletResponse response) {
		loggerVisit.info("微信支付查询:vxPayQuery");
		System.out.println("orderNO："+orderNo);
		if(null == orderNo || orderNo.length()==0){
			return LianjiuResult.build(401, "订单号不能为空");
		}
		return vxService.vxPayQuery(orderNo, request, response);
	}

	/**
	 * 微信支付状态改变
	 * 
	 * @param out_trade_no
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "ModifyStatus", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult ModifyStatus(String out_trade_no,String status, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("微信支付状态改变："+out_trade_no);
		System.out.println("微信支付状态改变："+status);
		if(null == out_trade_no || out_trade_no.length()==0){
			return LianjiuResult.build(501, "订单号不能为空");
		}
		if(null == status || status.length()==0){
			return LianjiuResult.build(501, "状态不能为空");
		}
		return vxService.ModifyStatus(out_trade_no, status, request, response);
	}

	/**
	 * 微信app支付Android加盟商 统一下单接口
	 * 
	 * @param money
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxapppayAndroid", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxapppayAndroid(String money, String userId, String outTradeNo,
			HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信app支付Android加盟商 统一下单接口");
		if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
			return LianjiuResult.build(401, "参数不能为空");
		}
		return vxService.wxapppayAndroid(money,userId,outTradeNo,request,response);
	}

	/**
	 * 微信app支付Android精品 统一下单接口
	 * 
	 * @param money
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxapppayAndroidJP", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxapppayAndroidJP(String money, String userId, String outTradeNo,
			HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信app支付Android精品 统一下单接口");
		if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
			return LianjiuResult.build(401, "参数不能为空");
		}
		return vxService.wxapppayAndroidJP(money, userId, outTradeNo, request, response);
	}
	
	/**
	 * 微信app支付ios加盟商 统一下单接口
	 * 
	 * @param money
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxpayapp", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxpayapp(String money, String userId, String outTradeNo,
			HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信app支付ios加盟商 统一下单接口");
		if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
			return LianjiuResult.build(401, "参数不能为空");
		}
		return vxService.wxpayapp(money, userId, outTradeNo, request, response);
	}

	/**
	 * 微信app支付ios链旧二手精品 统一下单接口
	 * 
	 * @param money
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxpayappJP", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxpayappJP(String money, String userId, String outTradeNo,
			HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信app支付ios加盟商 统一下单接口");
		if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
			return LianjiuResult.build(401, "参数不能为空");
		}
		return vxService.wxpayappJP(money, userId, outTradeNo, request, response);
	}

	
	
	/**
	 * 测试
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
	public synchronized LianjiuResult test(String id) {
		loggerVisit.info("微信内支付回调:vxInnerPayReturn  测试成功");
		id = "LJ15139140148501";
		//UserDetails ud = userDetailsMapper.selectByUserId(id);
		//if(null == ud){
			System.out.println("ud == null");
		//}
		//if(null == ud.getUserName()){
		//	System.out.println("ud.getUserName(): == null");
		//}
		//if(ud.getUserName().length()==0){
		//	System.out.println("ud.getUserName().length(): == null");
		//}
		UserDetails ud = new UserDetails();
		System.out.println("开始set");
		ud.setUserName("gg");
		System.out.println("set完毕");
		System.out.println(ud.toString());
		return LianjiuResult.ok(ud);
	}
	
}
