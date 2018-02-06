package com.lianjiu.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.CalculateStringNum;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MathUtil;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersExcellentRefund;
import com.lianjiu.model.OrdersPayment;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.WxOrder;
import com.lianjiu.rest.mapper.OrdersPaymentMapper;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentRefundMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.service.RefoundService;
import com.lianjiu.service.RefoundConfig.AlipayUtil;
import com.lianjiu.service.RefoundConfig.PayUtil;
import com.lianjiu.service.RefoundConfig.RequestHandler;
import com.lianjiu.service.RefoundConfig.ResponseData;
import com.lianjiu.service.RefoundConfig.WXUtils;

@Service
@Transactional
public class RefoundServiceImpl implements RefoundService {
	private static final Logger LOG = Logger.getLogger(RefoundServiceImpl.class);
	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private OrdersExcellentRefundMapper ordersExcellentRefundMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private OrdersPaymentMapper ordersPaymentMapper;

	/**
	 * 微信退款
	 * 
	 * @param money
	 *            :金额
	 * @param userId
	 *            :用户id
	 * @param outTradeNo
	 *            :商户订单号
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	public LianjiuResult wxRefund(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 查询支付单信息
			WxOrder wo = wxOrderMapper.selectByPrimaryKey(outTradeNo);
			// 添加预支付
			OrdersPayment payment = new OrdersPayment(IDUtils.genPaymentId(), outTradeNo, money, userId);
			ordersPaymentMapper.insert(payment);
			// 查询用户所有成功订单
			WxOrder order = new WxOrder();
			order.setUserId(userId);

			Double refee = Double.valueOf(money);// 退款金额
			Double ReMoney = 0.00;// 已退款金额
			Double accountMoney = Double.valueOf(wo.getTotalFee());// 支付的金额
			Double wxRefundMoney = 0.00;
			if (refee > accountMoney) {
				// 退款金额错误
				// result = iresult.getResult(ResultBiz.money);
				return LianjiuResult.build(501, "退款金额错误");
			}
			String ormoney = money;
			float sessionmoney = Float.parseFloat(money);
			String finalmoney = String.format("%.2f", sessionmoney);
			finalmoney = finalmoney.replace(".", "");
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// 总金额以分为单位，不带小数点
			int total_fee = Integer.parseInt(money);
			// 指定读取证书格式为PKCS12
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			// 读取本机存放的PKCS12证书文件
			// 测试地址
			int num = Integer.parseInt(wo.getResource());
			SSLContext sslcontext = null;
			if (num == 1 || num == 2) {
				// String keyStoreurl =
				// RefoundServiceImpl.class.getClassLoader().getResource("gzhapiclient_cert.p12").getPath();
				String keyStoreurl = "/usr/local/lianjiu-server-cluod/manager-tomcat/webapps/ROOT/gzhapiclient_cert.p12";
				System.out.println("证书文件路径" + keyStoreurl);
				FileInputStream instream = new FileInputStream(new File(keyStoreurl));
				try {
					// 指定PKCS12的密码(商户ID)
					keyStore.load(instream, "1489873322".toCharArray());
				} finally {
					instream.close();
				}
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1489873322".toCharArray()).build();
			} else if (num == 3) {
				// String keyStoreurl =
				// RefoundServiceImpl.class.getClassLoader().getResource("iosjmsapiclient_cert.p12").getPath();
				String keyStoreurl = "/usr/local/lianjiu-server-cluod/manager-tomcat/webapps/ROOT/iosjmsapiclient_cert.p12";
				System.out.println("证书文件路径" + keyStoreurl);
				FileInputStream instream = new FileInputStream(new File(keyStoreurl));
				try {
					// 指定PKCS12的密码(商户ID)
					keyStore.load(instream, WXUtils.mch_id.toCharArray());
				} finally {
					instream.close();
				}
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WXUtils.mch_id.toCharArray()).build();
			} else if (num == 4) {
				String keyStoreurl = RefoundServiceImpl.class.getClassLoader().getResource("iosjpapiclient_cert.p12")
						.getPath();
				System.out.println("证书文件路径" + keyStoreurl);
				FileInputStream instream = new FileInputStream(new File(keyStoreurl));
				try {
					// 指定PKCS12的密码(商户ID)
					keyStore.load(instream, WXUtils.mch_idJP.toCharArray());
				} finally {
					instream.close();
				}
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WXUtils.mch_idJP.toCharArray()).build();
			} else if (num == 5) {
				// String keyStoreurl =
				// RefoundServiceImpl.class.getClassLoader().getResource("anjmsapiclient_cert.p12").getPath();
				String keyStoreurl = "/usr/local/lianjiu-server-cluod/manager-tomcat/webapps/ROOT/anjmsapiclient_cert.p12";
				System.out.println("证书文件路径" + keyStoreurl);
				FileInputStream instream = new FileInputStream(new File(keyStoreurl));
				try {
					// 指定PKCS12的密码(商户ID)
					keyStore.load(instream, WXUtils.mch_idAn.toCharArray());
				} finally {
					instream.close();
				}
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WXUtils.mch_idAn.toCharArray()).build();
			} else if (num == 6) {
				// String keyStoreurl =
				// RefoundServiceImpl.class.getClassLoader().getResource("anjpapiclient_cert.p12").getPath();
				String keyStoreurl = "/usr/local/lianjiu-server-cluod/manager-tomcat/webapps/ROOT/anjpapiclient_cert.p12";
				System.out.println("证书文件路径" + keyStoreurl);
				FileInputStream instream = new FileInputStream(new File(keyStoreurl));
				try {
					// 指定PKCS12的密码(商户ID)
					keyStore.load(instream, WXUtils.mch_idJPAn.toCharArray());
				} finally {
					instream.close();
				}
				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WXUtils.mch_idJPAn.toCharArray()).build();
			}
			if (sslcontext == null) {
				return LianjiuResult.build(501, "证书验证失败");
			}

			// 指定TLS版本
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			// 设置httpclient的SSLSocketFactory
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

			HttpPost httpPost = new HttpPost(url);// 退款接口
			// System.out.println("executing request" +
			// httpPost.getRequestLine());

			// 设置类型

			Double ReMoneySu = 0.00;
			WxOrder wo1 = new WxOrder();
			wo1.setTotalFee(Integer.toString(total_fee));
			wo1.setOutTradeNo(IDUtils.genOrdersId());
			WxOrder wo2 = new WxOrder();
			String accountM = String.valueOf((int) (accountMoney * 100));
			wo2.setTotalFee(accountM);
			wo2.setOutTradeNo(outTradeNo);

			RequestHandler reqHandler = new RequestHandler(request, response);
			String data = WXUtils.getRefundXML(wo1, wo2, num, reqHandler);
			wo1.setTotalFee(Double.toString(refee));
			wo1.setType("2");// 退款
			wo1.setUserId(userId);
			wo1.setMessage(data);
			wo1.setDate(DateUtils.getTime());
			wo1.setResource(outTradeNo);
			System.out.println("data:" + data);

			StringEntity reqEntity = new StringEntity(data);
			reqEntity.setContentType("text/xml;charset=UTF-8");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse responses = httpclient.execute(httpPost);
			String message = EntityUtils.toString(responses.getEntity(), "utf-8");
			System.out.println("返回消息：" + message);
			// method.releaseConnection();
			// client.getHttpConnectionManager().closeIdleConnections(0);
			org.dom4j.Document document = DocumentHelper.parseText(message);
			System.out.println("document :" + document);
			System.out.println("return_code:" + document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())) {
				wo1.setState("2");// 请求成功
				// 保存订单信息
				wxOrderMapper.insertWXOrder(wo1);
				ReMoneySu = MathUtil.add(ReMoneySu, (Double.valueOf(wo1.getTotalFee()) / 100));
				// 改变退款单状态
				OrdersExcellentRefund record = new OrdersExcellentRefund();
				record.setOrExceProductStatus((byte) 2);
				record.setOrExceRefundUpdated(new Date());
				record.setOrExcellentId(outTradeNo);
				int i = ordersExcellentRefundMapper.modifyStatus(record);
				if (i == 1) {
					LOG.info("退款单状态改变成功");
				}
				// 更改订单状态
				OrdersExcellent ordersExcellent = new OrdersExcellent();
				ordersExcellent.setOrExcellentId(outTradeNo);
				// 修改订单状态为 已经退款
				ordersExcellent.setOrExcellentUpdated(new Date());
				ordersExcellent.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_REFUND_YES);
				int f = ordersExcellentMapper.updateByPrimaryKeySelective(ordersExcellent);
				if (f == 1) {
					LOG.info("订单状态改变成功");
				}

				// 用户的流水账单
				UserWalletRecord user = new UserWalletRecord();
				user.setRecordId(IDUtils.genGUIDs());
				user.setRecordPrice(ormoney);
				user.setRecordCreated(new Date());
				user.setRecordUpdated(new Date());
				user.setIsIncome((byte) 7);
				user.setUserId(userId);
				user.setRelativeId(outTradeNo);
				user.setRecordName("微信");
				userWalletRecordMapper.insertSelective(user);

				return LianjiuResult.ok("退款成功");
			} else {
				// 保存订单信息 ****
				wo1.setState("1");// 请求失败
				wxOrderMapper.insertWXOrder(wo1);
				// 发生错误停止
				return LianjiuResult.build(401, "发生错误,退款失败");
			}
		} catch (Exception e) {
			// result = iresult.getResult(ResultBiz.SYSTEM_BUSY);
			e.printStackTrace();
			return LianjiuResult.build(400, "异常");
		}
	}

	/**
	 * 支付宝退款
	 * 
	 * @param request
	 * @param response
	 * @param tradeno
	 *            支付宝交易订单号
	 * @param orderno
	 *            商家交易订单号
	 * @param callback
	 */
	public LianjiuResult orderPayRefund(String tradeno, String orderno, String amount) {
		LOG.info("订单退款:[/pay/refund]");
		if (StringUtil.isEmpty(tradeno) && StringUtil.isEmpty(orderno)) {
			return LianjiuResult.build(501, "订单号不能为空");
		}

		// 查询支付单信息
		WxOrder wo = wxOrderMapper.selectByPrimaryKey(orderno);
		// 查询用户所有成功订单
		WxOrder order = new WxOrder();
		order.setUserId(wo.getUserId());
		WxOrder wo1 = new WxOrder();
		wo1.setTotalFee(amount);
		wo1.setOutTradeNo(IDUtils.genOrdersId());
		wo1.setType("2");// 退款
		wo1.setUserId(wo.getUserId());
		wo1.setDate(DateUtils.getTime());
		wo1.setResource(orderno);

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
			wo1.setMessage(alipayResponse.getBody());
			System.out.println("data:" + alipayResponse.getBody());
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
					LOG.info("订单退款结果：退款成功");
					wo1.setState("2");// 请求成功
					// 保存订单信息
					wxOrderMapper.insertWXOrder(wo1);

					// 改变退款单状态
					OrdersExcellentRefund record = new OrdersExcellentRefund();
					record.setOrExceProductStatus((byte) 2);
					record.setOrExceRefundUpdated(new Date());
					record.setOrExcellentId(orderno);
					int i = ordersExcellentRefundMapper.modifyStatus(record);
					if (i == 1) {
						LOG.info("退款单状态改变成功");
					}
					// 更改订单状态
					OrdersExcellent ordersExcellent = new OrdersExcellent();
					ordersExcellent.setOrExcellentId(orderno);
					// 修改订单状态为 已经退款
					ordersExcellent.setOrExcellentUpdated(new Date());
					ordersExcellent.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_REFUND_YES);
					int f = ordersExcellentMapper.updateByPrimaryKeySelective(ordersExcellent);
					if (f == 1) {
						LOG.info("订单状态改变成功");
					}
					// 用户的流水账单
					UserWalletRecord user = new UserWalletRecord();
					user.setRecordId(IDUtils.genGUIDs());
					user.setRecordPrice(amount);
					user.setRecordCreated(new Date());
					user.setRecordUpdated(new Date());
					user.setIsIncome((byte) 7);
					user.setUserId(wo.getUserId());
					user.setRelativeId(orderno);
					user.setRecordName("支付宝");
					userWalletRecordMapper.insertSelective(user);
				} else {
					LOG.info("订单查询失败：" + alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg());
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

	/**
	 * 链旧钱包退款
	 */
	@Override
	public LianjiuResult walletPayBack(UserWalletRecord userwallet,String money) {
		LOG.info("订单退款:walletPayBack  链旧钱包退款");

		// 减去钱包里边对应的金额 失败再转回去
		WalletLianjiu wall = new WalletLianjiu();
		wall.setUserId(userwallet.getUserId());
		//wall.setWalletMoney(userwallet.getRecordPrice());
		wall.setWalletMoney(money);
		int i = walletLianjiuMapper.addWalletMoney(wall);
		if (i == 1) {
			LOG.info("退款记录添加成功");
		}
		// 改变退款单状态
		OrdersExcellentRefund record = new OrdersExcellentRefund();
		record.setOrExceProductStatus((byte) 2);
		record.setOrExceRefundUpdated(new Date());
		record.setOrExcellentId(userwallet.getRelativeId());
		int f = ordersExcellentRefundMapper.modifyStatus(record);
		if (f == 1) {
			LOG.info("退款单状态改变成功");
		}
		
		String moneys = walletLianjiuMapper.selectUserMoney(userwallet.getUserId());
		// userMoney = 用户余额    price = 用户退款金额
		BigDecimal userMoney = new BigDecimal(moneys);
		System.out.println("userMoney:"+userMoney);
		BigDecimal price = new BigDecimal(money);
		System.out.println("price:"+price);
		moneys = userMoney.add(price).toString();
		System.out.println("moneys:"+moneys);
		int count = walletLianjiuMapper.updateUserWalletMoney(moneys, userwallet.getUserId());
		System.out.println("修改了："+count+"条数据");
		
		// 用户的流水账单
		UserWalletRecord user = new UserWalletRecord();
		user.setRecordUpdated(new Date());
		user.setIsIncome((byte) 8);
		// user.setRecordName("");
		user.setRecordId(userwallet.getRecordId());
		userWalletRecordMapper.updateByPrimaryKeySelective(user);

		// 更改订单状态
		OrdersExcellent ordersExcellent = new OrdersExcellent();
		ordersExcellent.setOrExcellentId(userwallet.getRelativeId());
		// 修改订单状态为 已经退款
		ordersExcellent.setOrExcellentUpdated(new Date());
		ordersExcellent.setOrExcellentStatus(GlobalValueUtil.ORDER_STATUS_REFUND_YES);
		int h = ordersExcellentMapper.updateByPrimaryKeySelective(ordersExcellent);
		if (h == 1) {
			LOG.info("订单状态改变成功");
		}

		return LianjiuResult.ok("退款成功");
	}

	/**
	 * 二手优品的退款
	 */
	@Override
	public LianjiuResult payBack(String outTradeNo, String money, HttpServletRequest request,
			HttpServletResponse response) {
		/**
		 * 查看订单的金额
		 */
		String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
		if (0 > CalculateStringNum.compareTo(ePrice, money)) {
			return LianjiuResult.build(501, "退款金额与订单金额不符合");
		}
		/**
		 * 查询微信的支付记录
		 */
		WxOrder wxOrders = wxOrderMapper.selectByPrimaryKey(outTradeNo);
		if (wxOrders != null) {
//			if (wxOrders.getResource().length() > 2) {
//				return LianjiuResult.build(501, "订单参数有误");
//			}
//			int re = Integer.parseInt(wxOrders.getResource());
//			if (re <= 6) {
//				if (!wxOrders.getState().equals("4")) {
//					return LianjiuResult.build(501, "支付没成功");
//				}
//				return this.wxRefund(money, wxOrders.getUserId(), outTradeNo, request, response);
//			} else {
//				String reMessage = wxOrders.getReMessage();
//				if (reMessage == null && !wxOrders.getState().equals("4")) {
//					return LianjiuResult.build(501, "支付没成功");
//				}
//				if (Double.valueOf(wxOrders.getTotalFee()) < Double.valueOf(money)) {
//					return LianjiuResult.build(501, "金额不应大于总额");
//				}
//				Map maps = (Map) JSON.parse(reMessage);
//				String tradeNo = (String) maps.get("trade_no");
//				return this.orderPayRefund(tradeNo, outTradeNo, money);
//			}

			return LianjiuResult.build(501, "微信支付宝退款维护中~");
		} else {
			UserWalletRecord use = userWalletRecordMapper.selectWalletMoneyidByOrderid(outTradeNo);
			if (use != null) {
				return this.walletPayBack(use,money);
			} else {
				return LianjiuResult.build(501, "此支付订单不存在");
			}

		}
	}

}
