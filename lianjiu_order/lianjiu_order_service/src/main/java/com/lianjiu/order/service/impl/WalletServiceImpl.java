package com.lianjiu.order.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.order.service.WalletService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

/**
 * 链旧钱包
 * 
 * @author zxy
 *
 */
@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;

	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	/**
	 * 添加钱款
	 */
	@Override
	public LianjiuResult insertWallet(WalletLianjiu walletLianjiu) {
		if (walletLianjiu.getUserId() == null) {
			return LianjiuResult.build(501, "请指定添加的钱包数据");
		}
		// 判断钱包是否存在,不存在直接添加
		if (walletLianjiuMapper.selectWalletByUserId(walletLianjiu.getUserId()) == null) {
			walletLianjiu.setWalletId(IDUtils.genGUIDs());
			walletLianjiu.setWalletCreated(new Date());
			walletLianjiu.setPayment(MD5Util.md5(walletLianjiu.getPayment()));
			walletLianjiuMapper.insert(walletLianjiu);
		}
		String payment = walletLianjiuMapper.selectPaymentByUserId(walletLianjiu.getUserId());
		// 判断密码是否正确,密码正确
		if (payment.equals(MD5Util.md5(walletLianjiu.getPayment()))) {
			int i = walletLianjiuMapper.addWalletMoney(walletLianjiu);
			if (i == 1) {
				return LianjiuResult.ok("钱款添加成功");
			}
		} else {
			return LianjiuResult.build(404, "密码错误");
		}
		return LianjiuResult.build(404, "钱款添加失败");
	}

	@Override
	public LianjiuResult selectWalletByUserId(String userId) {
		if (StringUtil.isEmpty(userId)) {
			return LianjiuResult.build(501, "请传递指定的用户");
		}
		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(userId);
		LianjiuResult result = new LianjiuResult(walletLianjiu);
		return result;
	}

	@Override
	public LianjiuResult reduceWallet(WalletLianjiu walletLianjiu) {
		if (walletLianjiu.getUserId() == null) {
			return LianjiuResult.build(501, "请指定添加的钱包数据");
		}
		String payment = walletLianjiuMapper.selectPaymentByUserId(walletLianjiu.getUserId());
		// 判断密码是否正确,密码正确
		if (payment.equals(MD5Util.md5(walletLianjiu.getPayment()))) {
			WalletLianjiu walletLia = walletLianjiuMapper.selectWalletByUserId(walletLianjiu.getUserId());
			if (Integer.parseInt(walletLia.getWalletMoney()) < Integer.parseInt(walletLianjiu.getWalletMoney())) {
				return LianjiuResult.build(501, "账户剩余金额不足支出");
			}
			int i = walletLianjiuMapper.reduceWalletMoney(walletLianjiu);
			if (i != 1) {
				return LianjiuResult.build(404, "钱款减少失败");
			}
		}
		return LianjiuResult.ok("钱款减少成功");
	}

	/**
	 * 支付短信验证码
	 */
	@Override
	public LianjiuResult sendSms(String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(500, "请传入用户编号");
		}
		String phone = userMapper.getPhoneById(userId);
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(400, "该用户编号错误");
		}
		System.out.println("用户" + userId + "的手机号：" + phone);
		System.out.println("开始获取短信验证码");
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("6", "+86", phone, code);
		// 生成token
		// String token = UUID.randomUUID().toString();
		// 把用户信息写入redis
		jedisClient.set(userId + "_LIAN_JIU_WALLET_CODE", code);
		// 设置session的过期时间
		jedisClient.expire(userId + "_LIAN_JIU_WALLET_CODE", 300);
		// 设置验证码redis键为全局变量
		// GlobalValueUtil.phoneCodeToken = token;
		// System.out.println(GlobalValueUtil.phoneCodeToken);
		return LianjiuResult.ok("发送成功");
	}

	@Override	
	public LianjiuResult checkSms(String excellentId,String userId,String check,String price) {
		System.out.println("userId:"+userId);
		System.out.println("check:"+check);
		System.out.println("price:"+price);
		LianjiuResult result = new LianjiuResult();
		String userCheck = jedisClient.get(userId + "_LIAN_JIU_WALLET_CODE");
		//Map<String,Object> map = new HashMap<String,Object>();
		if (null == userCheck || "".equals(userCheck) || "null".equals(userCheck)) {
			return LianjiuResult.build(501, "验证码已失效，请重新获取验证码");
		} else {
			Byte state = ordersExcellentMapper.getExcellentByState(excellentId);
			System.out.println("订单状态："+state);
			if(null != state && state== GlobalValueUtil.ORDER_STATUS_PAY_NO){
				if(check.equals(userCheck)){
					jedisClient.del(userId + "_LIAN_JIU_WALLET_CODE");
					//int count = userMapper.getUserMoneyCheck(userId,price);
					String userPrice = userMapper.getUserPrice(userId);
					BigDecimal price1 = new BigDecimal(userPrice);
					BigDecimal price2 = new BigDecimal(price);
					String lastPrice = price1.subtract(price2).toString();
					String checks = lastPrice.substring(0,1);
					if(!checks.equals("-")){
						int relativePriceCount = userMapper.relativePrice(userId,lastPrice,new Date());
						OrdersExcellent ordersExcellent = new OrdersExcellent();
						ordersExcellent.setOrExcellentId(excellentId);
						ordersExcellent.setOrExcellentUpdated(new Date());
						ordersExcellent.setOrExcellentStatus((byte) 2);
						int modifyExcellentStateCount =  ordersExcellentMapper.modifyExcellentState(ordersExcellent);
						//添加流水单数据
						UserWalletRecord user = new UserWalletRecord();
						user.setRecordId(IDUtils.genGUIDs());
						user.setRecordPrice(price);
						user.setRelativeId(excellentId);
						user.setRecordCreated(new Date());
						user.setRecordUpdated(new Date());
						user.setUserId(userId);
						user.setIsIncome((byte) 4);
						OrdersExcellent order1 = ordersExcellentMapper.selectByPrimaryKey(excellentId);
						user.setRecordName(order1.getOrItemsNamePreview());
						userWalletRecordMapper.insertSelective(user);
						
						System.out.println("修改金额："+relativePriceCount+"条数据");
						System.out.println("修改状态："+modifyExcellentStateCount+"条数据");
						//map.put("payText","恭喜您，支付成功！");
						//map.put("payStatus","true");
						System.out.println("恭喜您，支付成功");
						result.setStatus(200);
						result.setMsg("支付成功");
					}else{
						//map.put("payText","链旧钱包余额不足！");
						//map.put("payStatus","false");
						System.out.println("链旧钱包余额不足！");
						result.setStatus(501);
						result.setMsg("余额不足");
					}
					//map.put("validate", "true");
					//result.setMsg("验证成功");
				}else{
					result.setMsg("短信验证失败");
					//map.put("validate", "false");
					result.setStatus(502);
				}
			}else{
				result.setMsg("订单已支付，无需再次付款");
				//map.put("validate", "false");
				result.setStatus(503);
			}
			
		}
		//result.setLianjiuData(map);
		return result;
	}
	// 提现获取验证码
	@Override
	public LianjiuResult wSendSms(String userId) {
		String phone = userMapper.getPhoneById(userId);
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(400, "该用户编号错误");
		}
		System.out.println("用户" + userId + "的手机号：" + phone);
		System.out.println("开始获取短信验证码");
		// 获取随机短信验证码
		String code = IDUtils.genPWDId();
		// 给用户发送随机验证码
		sendSMS.sendMessage("13", "+86", phone, code);
		// 生成token
		// String token = UUID.randomUUID().toString();
		// 把用户信息写入redis
		jedisClient.set(userId + "_LIAN_JIU_WALLET_CODE", code);
		// 设置session的过期时间
		jedisClient.expire(userId + "_LIAN_JIU_WALLET_CODE", 300);
		// 设置验证码redis键为全局变量
		// GlobalValueUtil.phoneCodeToken = token;
		// System.out.println(GlobalValueUtil.phoneCodeToken);
		return LianjiuResult.ok("发送成功");
	}

	// 验证提现验证码
	@Override
	public LianjiuResult checkWsms(String userId, String code) {
		String oCode = jedisClient.get(userId + "_LIAN_JIU_WALLET_CODE");
		if (Util.isEmpty(oCode)) {
			return LianjiuResult.build(500, "验证码已过期");
		}
		System.out.println("oCode:" + oCode);
		System.out.println("code:" + code);
		if (oCode.equals(code)) {
			jedisClient.del(userId + "_LIAN_JIU_WALLET_CODE");
			return LianjiuResult.build(200, "验证成功");
		}
		return LianjiuResult.build(501, "验证码错误");
	}

	public static void main(String[] args) {
		String x = "1";
		String y  = "10";
		BigDecimal price1 = new BigDecimal(x);
		BigDecimal price2 = new BigDecimal(y);
		String lastPrice = price1.subtract(price2).toString();
		System.out.println(lastPrice.substring(0,1));
		System.out.println(!lastPrice.substring(0,1).equals("-"));
		System.out.println("lastPrice:"+lastPrice);
	}
}
