package com.lianjiu.payment.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.User;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.dao.config.AlipayUtil;
import com.lianjiu.payment.dao.config.DatetimeUtil;
import com.lianjiu.payment.dao.config.JsonResult;
import com.lianjiu.payment.dao.config.PayUtil;
import com.lianjiu.payment.dao.config.ResponseData;
import com.lianjiu.payment.dao.config.SerializerFeatureUtil;
import com.lianjiu.payment.dao.config.WebUtil;
import com.lianjiu.payment.service.AliPayAppService;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

@Service
public class AliPayAppServiceImpl implements AliPayAppService {

	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private OrdersFacefaceDetailsMapper ordersFacefaceDetailsMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private AllianceBusinessDetailsMapper allianceBusinessDetailsMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private UserMapper userMapper;
	private static Logger loggerAlipay = Logger.getLogger("alipay");
	
	@Override
	public LianjiuResult orderPay(HttpServletRequest request, HttpServletResponse response, String cashnum,
			String userId, String outTradeNo) {
		
		String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
		String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
		if (ePrice == null) {
			ePrice = "0";
		}
		if (fPrice == null) {
			fPrice = "0";
		}
		if (!Double.valueOf(fPrice).equals(Double.valueOf(cashnum))
				&& !Double.valueOf(ePrice).equals(Double.valueOf(cashnum))) {
			return LianjiuResult.build(501, "数据不正确,请重新输入");
		}
		// 增加10%的汇率
		Double cash = Double.valueOf(cashnum);
		cash = cash * 1.1;
		cashnum = String.format("%.2f", cash);
		// cashnum = Double.toString(cash);
		String out_trade_no = outTradeNo;
		Map<String, String> param = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayUtil.ALIPAY_APPID);// 商户订单号
		// param.put("app_id", "2016090900467691");// 测试商户订单号
		param.put("charset", "utf-8");
		param.put("format", "JSON");
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("notify_url", "https://orders.lianjiuhuishou.com/orders/alipayApp/notify"); // 支付宝服务器主动通知商户服务
		param.put("sign_type", "RSA");
		param.put("timestamp", DatetimeUtil.formatDateTime(new Date()));
		param.put("version", "1.0");

		Map<String, Object> pcont = new HashMap<>();
		// 支付业务请求参数
		pcont.put("body", "app支付android");// 对交易或商品的描述
		pcont.put("out_trade_no", out_trade_no); // 商户订单号
		pcont.put("subject", "链旧支付宝支付"); // 订单标题
		pcont.put("timeout_express", "90m");
		pcont.put("total_amount", String.valueOf(cashnum));// 交易金额

		param.put("biz_content", JSON.toJSONString(pcont)); // 业务请求参数
															// 不需要对json字符串转义
		Map<String, String> payMap = new HashMap<>();
		param.put("sign", PayUtil.getSign(param, AlipayUtil.APP_PRIVATE_KEY)); // 业务请求参数
		payMap.put("orderStr", PayUtil.getSignEncodeUrl(param, true));
		WxOrder wo = wxOrderMapper.selectByPrimaryKey(outTradeNo);
		if (wo == null) {
			// String str = JSON.toJSONString(payMap);
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayAndroidJMS);
			wxOrderMapper.insertWXOrder(wxOrder);
			// jedisClient.set(key, value)
		} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
			return LianjiuResult.build(501, "此订单已支付,请勿重复提交");
		} else {
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayAndroidJMS);
			wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
		}

		return LianjiuResult.ok(payMap);
	}

	@Override
	public LianjiuResult orderPayJP(HttpServletRequest request, HttpServletResponse response, String cashnum,
			String userId, String outTradeNo) {
		
		String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
		String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
		if (ePrice == null) {
			ePrice = "0";
		}
		if (fPrice == null) {
			fPrice = "0";
		}
		if (!Double.valueOf(fPrice).equals(Double.valueOf(cashnum))
				&& !Double.valueOf(ePrice).equals(Double.valueOf(cashnum))) {
			return LianjiuResult.build(501, "数据不正确,请重新输入");
		}
		String out_trade_no = outTradeNo;
		Map<String, String> param = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayUtil.ALIPAY_APPID);// 商户订单号
		// param.put("app_id", "2016090900467691");// 测试商户订单号
		param.put("charset", "utf-8");
		param.put("format", "JSON");
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("notify_url", "https://orders.lianjiuhuishou.com/orders/alipayApp/notify"); // 支付宝服务器主动通知商户服务
		param.put("sign_type", "RSA");
		param.put("timestamp", DatetimeUtil.formatDateTime(new Date()));
		param.put("version", "1.0");

		Map<String, Object> pcont = new HashMap<>();
		// 支付业务请求参数
		pcont.put("body", "app支付android");// 对交易或商品的描述
		pcont.put("out_trade_no", out_trade_no); // 商户订单号
		pcont.put("product_code", "QUICK_MSECURITY_PAY");// 销售产品码
		pcont.put("subject", "链旧支付宝支付"); // 订单标题
		pcont.put("timeout_express", "90m");
		pcont.put("total_amount", String.valueOf(cashnum));// 交易金额

		param.put("biz_content", JSON.toJSONString(pcont)); // 业务请求参数
															// 不需要对json字符串转义
		Map<String, String> payMap = new HashMap<>();
		param.put("sign", PayUtil.getSign(param, AlipayUtil.APP_PRIVATE_KEY)); // 业务请求参数
		payMap.put("orderStr", PayUtil.getSignEncodeUrl(param, true));
		WxOrder wo = wxOrderMapper.selectByPrimaryKey(outTradeNo);
		if (wo == null) {
			// String str = JSON.toJSONString(payMap);
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayAndroidJP);
			wxOrderMapper.insertWXOrder(wxOrder);
			// jedisClient.set(key, value)
		} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
			return LianjiuResult.build(501, "此订单已支付,请勿重复提交");
		} else {
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayAndroidJP);
			wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
		}

		return LianjiuResult.ok(payMap);
	}

	@Override
	public LianjiuResult iosOrderPay(HttpServletRequest request, HttpServletResponse response, String cashnum,
			String userId, String outTradeNo) {
		String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
		String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
		if (ePrice == null) {
			ePrice = "0";
		}
		if (fPrice == null) {
			fPrice = "0";
		}
		if (!Double.valueOf(fPrice).equals(Double.valueOf(cashnum))
				&& !Double.valueOf(ePrice).equals(Double.valueOf(cashnum))) {
			return LianjiuResult.build(501, "数据不正确,请重新输入");
		}
		// 增加10%的汇率
		Double cash = Double.valueOf(cashnum);
		cash = cash * 1.1;
		cashnum = String.format("%.2f", cash);
		String out_trade_no = outTradeNo;
		Map<String, String> param = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayUtil.ALIPAY_APPID);// 商户订单号
		// param.put("app_id", "2016090900467691");// 测试商户订单号
		param.put("charset", "utf-8");
		param.put("format", "JSON");
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("notify_url", "https://orders.lianjiuhuishou.com/orders/alipayApp/notify"); // 支付宝服务器主动通知商户服务
		param.put("sign_type", "RSA");
		param.put("timestamp", DatetimeUtil.formatDateTime(new Date()));
		param.put("version", "1.0");

		Map<String, Object> pcont = new HashMap<>();
		// 支付业务请求参数
		pcont.put("body", "JMS/app支付ios");// 对交易或商品的描述
		pcont.put("out_trade_no", out_trade_no); // 商户订单号
		pcont.put("product_code", "QUICK_MSECURITY_PAY");// 销售产品码
		pcont.put("subject", "链旧支付宝支付"); // 订单标题
		pcont.put("timeout_express", "90m");
		pcont.put("total_amount", String.valueOf(cashnum));// 交易金额

		param.put("biz_content", JSON.toJSONString(pcont)); // 业务请求参数
															// 不需要对json字符串转义
		Map<String, String> payMap = new HashMap<>();
		param.put("sign", PayUtil.getSign(param, AlipayUtil.APP_PRIVATE_KEY)); // 业务请求参数
		payMap.put("orderStr", PayUtil.getSignEncodeUrl(param, true));
		WxOrder wo = wxOrderMapper.selectByPrimaryKey(outTradeNo);
		if (wo == null) {
			// String str = JSON.toJSONString(payMap);
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayIOSJMS);
			wxOrderMapper.insertWXOrder(wxOrder);
			// jedisClient.set(key, value)
		} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
			return LianjiuResult.build(501, "此订单已支付,请勿重复提交");
		} else {
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayIOSJMS);
			wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
		}

		return LianjiuResult.ok(payMap);
		
	}

	@Override
	public LianjiuResult iosOrderPayJP(HttpServletRequest request, HttpServletResponse response, String cashnum,
			String userId, String outTradeNo) {
		loggerAlipay.info("支付下订单 ios:iosPayment");
		String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
		String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
		if (ePrice == null) {
			ePrice = "0";
		}
		if (fPrice == null) {
			fPrice = "0";
		}
		if (!Double.valueOf(fPrice).equals(Double.valueOf(cashnum))
				&& !Double.valueOf(ePrice).equals(Double.valueOf(cashnum))) {
			return LianjiuResult.build(501, "数据不正确,请重新输入");
		}

		String out_trade_no = outTradeNo;
		Map<String, String> param = new HashMap<>();
		// 公共请求参数
		param.put("app_id", AlipayUtil.ALIPAY_APPID);// 商户订单号
		// param.put("app_id", "2016090900467691");// 测试商户订单号
		param.put("charset", "utf-8");
		param.put("format", "JSON");
		param.put("method", "alipay.trade.app.pay");// 交易金额
		param.put("notify_url", "https://orders.lianjiuhuishou.com/orders/alipayApp/notify"); // 支付宝服务器主动通知商户服务
		param.put("sign_type", "RSA");
		param.put("timestamp", DatetimeUtil.formatDateTime(new Date()));
		param.put("version", "1.0");

		Map<String, Object> pcont = new HashMap<>();
		// 支付业务请求参数
		pcont.put("body", "JP/app支付ios");// 对交易或商品的描述
		pcont.put("out_trade_no", out_trade_no); // 商户订单号
		pcont.put("product_code", "QUICK_MSECURITY_PAY");// 销售产品码
		pcont.put("subject", "链旧支付宝支付"); // 订单标题
		pcont.put("timeout_express", "90m");
		pcont.put("total_amount", String.valueOf(cashnum));// 交易金额

		param.put("biz_content", JSON.toJSONString(pcont)); // 业务请求参数
															// 不需要对json字符串转义
		Map<String, String> payMap = new HashMap<>();
		param.put("sign", PayUtil.getSign(param, AlipayUtil.APP_PRIVATE_KEY)); // 业务请求参数
		payMap.put("orderStr", PayUtil.getSignEncodeUrl(param, true));
		WxOrder wo = wxOrderMapper.selectByPrimaryKey(outTradeNo);
		if (wo == null) {
			// String str = JSON.toJSONString(payMap);
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayIOSJP);
			wxOrderMapper.insertWXOrder(wxOrder);
			// jedisClient.set(key, value)
		} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
			return LianjiuResult.build(501, "此订单已支付,请勿重复提交");
		} else {
			WxOrder wxOrder = new WxOrder();
			wxOrder.setOutTradeNo(out_trade_no);
			wxOrder.setTotalFee(cashnum);
			wxOrder.setUserId(userId);
			wxOrder.setType("1");
			wxOrder.setDate(DateUtils.getTime());
			wxOrder.setMessage(payMap.get("orderStr"));
			wxOrder.setState("1");
			wxOrder.setResource(AlipayUtil.AlipayIOSJP);
			wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
		}

		return LianjiuResult.ok(payMap);
	}


	@Override
	public void orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno, String orderno,
			String callback) {

		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest(); // 统一收单线下交易查询
		// 只需要传入业务参数
		Map<String, Object> param = new HashMap<>();
		param.put("out_trade_no", orderno); // 商户订单号
		param.put("trade_no", tradeno);// 交易金额
		alipayRequest.setBizContent(JSON.toJSONString(param)); // 不需要对json字符串转义

		Map<String, String> restmap = new HashMap<String, String>();// 返回提交支付宝订单交易查询信息
		boolean flag = false; // 查询状态
		try {
			AlipayTradeQueryResponse alipayResponse = AlipayUtil.getAlipayClient().execute(alipayRequest);
			if (alipayResponse.isSuccess()) {
				// 调用成功，则处理业务逻辑
				if ("10000".equals(alipayResponse.getCode())) {
					// 订单创建成功
					flag = true;
					restmap.put("order_no", alipayResponse.getOutTradeNo());
					restmap.put("trade_no", alipayResponse.getTradeNo());
					restmap.put("buyer_logon_id", alipayResponse.getBuyerLogonId());
					restmap.put("trade_status", alipayResponse.getTradeStatus());
					loggerAlipay.info("订单查询结果：" + alipayResponse.getTradeStatus());
				} else {
					loggerAlipay.info("订单查询失败：" + alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg());
				}
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		if (flag) {
			// 订单查询成功
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON.toJSONString(
					new JsonResult(1, "订单查询成功", new ResponseData(null, restmap)), SerializerFeatureUtil.FEATURES)));
		} else { // 订单查询失败
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单查询失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
		
	}

	@Override
	public void orderPayNotify(HttpServletRequest request, HttpServletResponse response) {
		// 获取到返回的所有参数 先判断是否交易成功trade_status 再做签名校验
		// 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		// 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		// 3、校验通知中的seller_id（或者seller_email)
		// 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
		// 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
		if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
			loggerAlipay.info("TRADE_SUCCESS");
			Enumeration<?> pNames = request.getParameterNames();
			Map<String, String> param = new HashMap<String, String>();
			try {
				while (pNames.hasMoreElements()) {
					String pName = (String) pNames.nextElement();
					param.put(pName, request.getParameter(pName));
				}
				loggerAlipay.debug(param);
				boolean signVerified = AlipaySignature.rsaCheckV1(param, AlipayUtil.ALIPAY_ZFB_KEY,
						AlipayConstants.CHARSET_UTF8); // 校验签名是否正确
				loggerAlipay.debug(signVerified);
				if (signVerified) {
					// TODO 验签成功后
					// 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
					loggerAlipay.info("订单支付成功：" + JSON.toJSONString(param));
					// 改变支付表的记录 状态
					WxOrder wxOrder = new WxOrder();
					String out_trade_no = request.getParameter("out_trade_no");
					wxOrder.setOutTradeNo(out_trade_no);
					wxOrder.setState("4");
					wxOrder.setReMessage(JSON.toJSONString(param));
					wxOrder.setSign(request.getParameter("sign"));
					wxOrderMapper.updateByPrimaryKeySelective(wxOrder);

					// 更改订单表的订单状态
					OrdersFaceface orders = new OrdersFaceface();
					orders.setOrFacefaceId(out_trade_no);
					orders.setOrFacefaceStatus((byte) 3);
					OrdersExcellent excellent = new OrdersExcellent();
					excellent.setOrExcellentStatus((byte) 2);
					excellent.setOrExcellentId(out_trade_no);
					excellent.setOrExcellentUpdated(new Date());
					int f = ordersFacefaceMapper.updateFaceFaceFinishState(orders);
					int e = ordersExcellentMapper.modifyExcellentFinishState(excellent);
					if (f + e < 2) {
						throw new RuntimeException("订单状态修改失败");
					}
					// 修改加盟商订单每星期状态
					if (f >= 2) {
						int i = allianceBusinessDetailsMapper.modifyWeeklyState(out_trade_no);
						if (i == 1) {
							loggerAlipay.info("加盟商订单每星期状态修改成功");
						} else {
							loggerAlipay.info("加盟商订单每星期状态修改失败");
						}
						// 增加加盟商流水单
						String money = request.getParameter("total_amount");
						UserWalletRecord user = new UserWalletRecord();
						user.setRecordId(IDUtils.genGUIDs());
						user.setRecordPrice(money);
						user.setRelativeId(out_trade_no);
						user.setRecordCreated(new Date());
						user.setRecordUpdated(new Date());
						user.setIsIncome((byte) 3);
						OrdersFaceface order1 = ordersFacefaceMapper.selectByPrimaryKey(out_trade_no);
						user.setUserId(order1.getOrFacefaceAllianceId());
						user.setRecordName(order1.getOrItemsNamePreview());
						userWalletRecordMapper.insertSelective(user);
						// 增加用户流水单
						user.setRecordId(IDUtils.genGUIDs());
						user.setUserId(order1.getUserId());
						user.setIsIncome((byte) 5);

						Double cash = Double.valueOf(money);
						cash = cash / 1.1;
						money = String.format("%.2f", cash);

						user.setRecordPrice(money);
						userWalletRecordMapper.insertSelective(user);

						// 给用户加钱
						WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(order1.getUserId());
						// 判断钱包是否存在,不存在直接添加
						if (walletLianjiu == null) {
							User use = userMapper.selectByPrimaryKey(order1.getUserId());
							walletLianjiu = new WalletLianjiu();
							walletLianjiu.setWalletId(IDUtils.genGUIDs());
							walletLianjiu.setWalletMoney(money);
							walletLianjiu.setWalletTotalcount(money);
							walletLianjiu.setWalletCreated(new Date());
							if (null != use) {
								walletLianjiu.setPayment(use.getUserPassword());
							}
							walletLianjiuMapper.insert(walletLianjiu);
						}
						walletLianjiu.setWalletMoney(money);
						int h = walletLianjiuMapper.updateWalletMoney(walletLianjiu);
						if (h == 0) {
							loggerAlipay.debug("打款失败");
						}

					}
					// jedisClient.set(out_trade_no,
					// request.getParameter("trade_no"));
					// return LianjiuResult.ok("订单支付成功：" +
					// JSON.toJSONString(param));
					response.getWriter().write("success");
					response.getWriter().close();
				} else {
					loggerAlipay.info("支付失败！");
					// TODO 验签失败则记录异常日志，并在response中返回failure.
					WxOrder wxOrder = new WxOrder();
					String out_trade_no = request.getParameter("out_trade_no");
					wxOrder.setOutTradeNo(out_trade_no);
					wxOrder.setState("3");
					wxOrder.setReMessage(JSON.toJSONString(param));
					wxOrder.setSign(request.getParameter("sign"));
					wxOrderMapper.updateByPrimaryKeySelective(wxOrder);
					response.getWriter().write("fail");
					response.getWriter().close();
					// return LianjiuResult.build(501, "订单验签失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public LianjiuResult orderPayRefund(HttpServletRequest request, HttpServletResponse response, String tradeno,
			String orderno, double amount) {
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest(); // 统一收单交易退款接口
		// 只需要传入业务参数
		Map<String, Object> param = new HashMap<>();
		param.put("out_trade_no", orderno); // 商户订单号
		param.put("trade_no", tradeno);// 交易金额
		param.put("refund_amount", amount);// 退款金额
		param.put("refund_reason", "测试支付退款");// 退款金额
		param.put("out_request_no", PayUtil.getRefundNo()); // 退款单号
		alipayRequest.setBizContent(JSON.toJSONString(param)); // 不需要对json字符串转义

		Map<String, Object> restmap = new HashMap<>();// 返回支付宝退款信息
		boolean flag = false; // 查询状态
		try {
			AlipayTradeRefundResponse alipayResponse = AlipayUtil.getAlipayClient().execute(alipayRequest);
			if (alipayResponse.isSuccess()) {
				// 调用成功，则处理业务逻辑
				if ("10000".equals(alipayResponse.getCode())) {
					// 订单创建成功
					flag = true;
					restmap.put("out_trade_no", alipayResponse.getOutTradeNo());
					restmap.put("trade_no", alipayResponse.getTradeNo());
					restmap.put("buyer_logon_id", alipayResponse.getBuyerLogonId());// 用户的登录id
					restmap.put("gmt_refund_pay", alipayResponse.getGmtRefundPay()); // 退看支付时间
					restmap.put("buyer_user_id", alipayResponse.getBuyerUserId());// 买家在支付宝的用户id
					loggerAlipay.info("订单退款结果：退款成功");
				} else {
					loggerAlipay.info("订单查询失败：" + alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg());
				}
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		if (flag) {
			return LianjiuResult.ok("订单退款成功:" + new ResponseData(null, restmap));
		} else { // 订单查询失败
			return LianjiuResult.build(501, "订单退款失败:" + new ResponseData());
		}
	}



	@Override
	public void orderPayRefundQuery(HttpServletRequest request, HttpServletResponse response, String orderno,
			String tradeno, String callback) {

		AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest(); // 统一收单交易退款查询
		// 只需要传入业务参数
		Map<String, Object> param = new HashMap<>();
		param.put("out_trade_no", orderno); // 商户订单号
		param.put("trade_no", tradeno);// 交易金额
		param.put("out_request_no", orderno);// 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
		alipayRequest.setBizContent(JSON.toJSONString(param)); // 不需要对json字符串转义

		Map<String, Object> restmap = new HashMap<>();// 返回支付宝退款信息
		boolean flag = false; // 查询状态
		try {
			AlipayTradeFastpayRefundQueryResponse alipayResponse = AlipayUtil.getAlipayClient().execute(alipayRequest);
			if (alipayResponse.isSuccess()) {
				// 调用成功，则处理业务逻辑
				if ("10000".equals(alipayResponse.getCode())) {
					// 订单创建成功
					flag = true;
					restmap.put("out_trade_no", alipayResponse.getOutTradeNo());
					restmap.put("trade_no", alipayResponse.getTradeNo());
					restmap.put("out_request_no", alipayResponse.getOutRequestNo());// 退款订单号
					restmap.put("refund_reason", alipayResponse.getRefundReason()); // 退款原因
					restmap.put("total_amount", alipayResponse.getTotalAmount());// 订单交易金额
					restmap.put("refund_amount", alipayResponse.getTotalAmount());// 订单退款金额
					loggerAlipay.info("订单退款结果：退款成功");
				} else {
					loggerAlipay.info("订单失败：" + alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg());
				}
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		if (flag) {
			// 订单查询成功
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON.toJSONString(
					new JsonResult(1, "订单退款成功", new ResponseData(null, restmap)), SerializerFeatureUtil.FEATURES)));
		} else { // 订单查询失败
			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
					.toJSONString(new JsonResult(-1, "订单退款失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
	}

}
