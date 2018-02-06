 package com.lianjiu.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.payment.service.TransferService;

/**
 * 微信 企业支付  个人提现
 * 创建时间：2016年11月9日 下午5:49:00
 * 
 * @author huangfu
 * @version 2.2
 */

@Controller
@RequestMapping("/transferController")
public class TransferController {
	
	@Autowired
	private TransferService transferService;

	//测试公众号
	/*private static final String APP_ID = "wx77f8b42d3163922d";
	//测试appscript
	private static final String API_SECRET = "qwertyuiopasdfghjklzxcvbnm123456";
	//测试
	private static final String MCH_ID = "1456215002";*/
	
	/**
	 * 微信企业支付资格验证
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/wxpayCheck", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult wxpayCheck(String userId,HttpServletRequest request, HttpServletResponse response){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		return transferService.wxpayCheck(userId,request,response);
	}
	
	/**
	 * 微信企业支付 GZH内 获取code接口
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wxpayCode", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult wxpayCode(String userId,String money,String name,HttpServletRequest request,
			HttpServletResponse response){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		return transferService.wxpayCode(userId,money,name,request,response);
	}

	/**
	 * 企业向个人支付转账 GZH内
	 * @param request
	 * @param response
	 * @param openid 用户openid
	 * @param callback
	 */
	@RequestMapping(value = "/GZHpayReturn", method = RequestMethod.GET)
	@ResponseBody
	public synchronized void transferPay(HttpServletRequest request, HttpServletResponse response) {
		transferService.transferPay(request,response);
	}

	/**
	 * 企业向个人支付转账 APP 
	 * @param request
	 * @param response
	 * @param openid 用户openid
	 * @param callback
	 */
	@RequestMapping(value = "/payOther", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult transferPayOther(HttpServletRequest request, HttpServletResponse response,String userId,String money,String openid,String name) {
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}else if(StringUtil.isEmpty(money)){
			return LianjiuResult.build(401, "money不能为空");
		}else if(StringUtil.isEmpty(openid)){
			return LianjiuResult.build(401, "openid不能为空");
		}else if(StringUtil.isEmpty(name)){
			return LianjiuResult.build(401, "name不能为空");
		}
		return transferService.transferPayOther(request,response,userId,money,openid,name);
	}
	
	/**
	 * 企业向个人转账查询
	 * @param request
	 * @param response
	 * @param tradeno 商户转账订单号
	 * @param callback
	 */
	@RequestMapping(value = "/pay/query", method = RequestMethod.POST)
	@ResponseBody
	public void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno,
			String callback) {
		
		transferService.orderPayQuery(request,response,tradeno,callback);
	}

	/**
	 * 银行卡支现
	 * @param userId
	 * @param money
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insert", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult unionpay(String userId,String money){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}else if(StringUtil.isEmpty(money)){
			return LianjiuResult.build(401, "money不能为空");
		}
		return  transferService.unionpay(userId,money);
	}

	/**
	 * 微信H5支现 获取code接口
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wxCashCode", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult wxCashCode(String userId,HttpServletRequest request,
			HttpServletResponse response) {
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		return transferService.wxCashCode(userId,request,response);
	}
	
	/**
	 * 微信H5支现
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/wxpayH5return", method = RequestMethod.GET)
	@ResponseBody
	public synchronized void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response) {
		transferService.wxpayGZHreturn(request,response);
	}
	
	/**
	 * 微信企业支付资格验证
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/wxpayCheckCode", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult wxpayCheckCode(String userId,String checkCode,HttpServletRequest request, HttpServletResponse response){
		if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(401, "用户id不能为空");
		}
		return transferService.wxpayCheckCode(userId,checkCode,request,response);
	}
	
	/** 
	  * 微信公众平台 成为开发者验证入口 
	  *  
	  * @param request 
	  *            the request send by the client to the server 
	  * @param response 
	  *            the response send by the server to the client 
	  */  
	@RequestMapping(value="/invisit", method = RequestMethod.GET)
	@ResponseBody
	 public void doGet(HttpServletRequest request, HttpServletResponse response){  
	  transferService.doGet(request,response);
	 }  



}
