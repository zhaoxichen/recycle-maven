package com.lianjiu.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.order.service.WalletService;

/**
 * 链旧钱包
 * 
 * @author zxy
 *
 */
@Controller
@RequestMapping("/wallet/")
public class WalletController {

	@Autowired
	private WalletService walletService;

	/**
	 * 
	 * xyz 2017年9月27日 descrption:用户加钱
	 * 
	 * @return LianjiuResult
	 * 
	 * 参数:userId 用户id
	 * 		payment:密码
	 * 		walletMoney :金钱	
	 */
	@RequestMapping(value = "insertWallet", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult insertWallet(WalletLianjiu walletLianjiu) {
		if(null == walletLianjiu){
			return LianjiuResult.build(401, "walletLianjiu对象绑定错误");
		}
		return  walletService.insertWallet(walletLianjiu);
	}
	/**
	 * 
	 * xyz 2017年9月27日 descrption:查询指定用户的钱包信息
	 * 
	 * @return LianjiuResult
	 * 
	 * 参数:userId 用户id
	 */
	@RequestMapping(value = "selectWalletByUserId/{userId}/userId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectWalletByUserId(@PathVariable String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return  walletService.selectWalletByUserId(userId);
	}
	/**
	 * 
	 * xyz 2017年9月27日 descrption:用户钱包减钱
	 * 
	 * @return LianjiuResult
	 * 
	 * 参数:userId 用户id
	 * 		payment:密码
	 * 		walletMoney :金钱
	 */
	@RequestMapping(value = "reduceWallet", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult reduceWallet(WalletLianjiu walletLianjiu) {
		if(null == walletLianjiu){
			return LianjiuResult.build(401, "walletLianjiu对象绑定错误");
		}
		return  walletService.reduceWallet(walletLianjiu);
	}

	/**
	 * 2017年10月17日 descrption:支付获取短信验证码
	 * @param username
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sendSms/{userId}", method = RequestMethod.GET )
	@ResponseBody
	public LianjiuResult sendSms(@PathVariable String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = walletService.sendSms(userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 2017年10月17日 descrption:获取的短信验证码验证
	 * @param username
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkSms/check", method = RequestMethod.POST )
	@ResponseBody
	public LianjiuResult checkSms(String excellentId, String userId, String check, String price) {
		if(Util.isEmpty(excellentId)){
			return LianjiuResult.build(401, "请传入正确的excellentId");
		}
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		if(Util.isEmpty(check)){
			return LianjiuResult.build(403, "验证码不能为空");
		}
		if(Util.isEmpty(price)){
			return LianjiuResult.build(404, "请传入正确的price");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = walletService.checkSms(excellentId,userId,check,price);
			return result;
		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("error:"+e.getMessage());
			return LianjiuResult.build(400, "未知错误！");
		}
	}
	/**
	 * 2017年10月17日 descrption:提现获取短信验证码
	 * @param {userId}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "withdrawals/sendSms", method = RequestMethod.POST )
	@ResponseBody
	public LianjiuResult wSendSms(String userId) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = walletService.wSendSms(userId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 2017年10月17日 descrption:验证提现获取短信验证码
	 * @param username/{userId}
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "withdrawals/checkWsms", method = RequestMethod.POST )
	@ResponseBody
	public LianjiuResult checkWsms(String userId,String code) {
		if(Util.isEmpty(userId)){
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if(Util.isEmpty(code)){
			return LianjiuResult.build(401, "请传入正确的code");
		}
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = walletService.checkWsms(userId,code);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
}

