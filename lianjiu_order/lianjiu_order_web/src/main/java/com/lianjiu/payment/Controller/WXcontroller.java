package com.lianjiu.payment.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.internal.util.StringUtils;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.CookieUtils;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MathUtil;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.User;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.config.WXUtil;
import com.lianjiu.payment.wxh5.CommonUtil;
import com.lianjiu.payment.wxh5.GetWxOrderno;
import com.lianjiu.payment.wxh5.RequestHandler;
import com.lianjiu.payment.wxh5.Sha1Util;
import com.lianjiu.payment.wxh5.TenpayUtil;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;
import com.lianjiu.rest.mapper.user.UserMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;

import net.sf.json.JSONObject;

@RequestMapping("/weixinWeb/")
@Controller
@Transactional
public class WXcontroller {
	// 公众号商户相关资料
	private String appid = "wx801dbcf0056597c9";
	// 公众号应用密钥
	private String appsecret = "b2382f1e41559a2e855462446affd2b5";
	// 公众号商户号
	private String mch_id = "1489873322";
	// 公众号商户密钥
	private String partnerkey = "FFB42680CDABAE725B6C3FAA43C259A4";
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
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
	private static Logger loggerWechat = Logger.getLogger("wechat");

	/**
	 * 微信公众号 获取code接口
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxpayGZH", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxpayGZH(String userId, String money, String orderNo, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		loggerWechat.info("微信公众号获取code接口:wxpayGZH");
		if (StringUtil.hasEmpty(money, userId, orderNo)) {
			return LianjiuResult.build(404, "参数不要为空");
		}
		String ePrice = ordersExcellentMapper.slectPriceByOrderNo(orderNo);
		String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(orderNo);
		if (ePrice == null) {
			ePrice = "0";
		}
		if (fPrice == null) {
			fPrice = "0";
		}
		if (!Double.valueOf(fPrice).equals(Double.valueOf(money))
				&& !Double.valueOf(ePrice).equals(Double.valueOf(money))) {
			return LianjiuResult.build(501, "数据不正确,请重新输入");
		}
		String backUri = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxpayGZHreturn";
		/**
		 * 授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		 * 最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名, 比如 Sign = %3D%2F%CS%
		 */
		backUri = backUri + "?userId=" + userId + "&describe=test&money=" + money + "&orderNo=" + orderNo;
		// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		backUri = URLEncoder.encode(backUri);
		/***
		 * scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		 */
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appid + "&redirect_uri="
				+ backUri + "&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		loggerWechat.debug("url:--" + url);
		// response.sendRedirect(url);
		return LianjiuResult.ok(url);
	}

	/**
	 * 微信公众号支付
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "wxpayGZHreturn", method = RequestMethod.GET)
	@ResponseBody
	public synchronized void wxpayGZHreturn(HttpServletRequest request, HttpServletResponse response) {
		loggerWechat.info("微信公众号支付:wxpayGZHreturn");
		try {
			// 网页授权后获取传递的参数
			loggerWechat.debug("微信GZH return");
			String orderNo = request.getParameter("orderNo");
			String code = request.getParameter("code");
			String userId = request.getParameter("userId");
			String money = request.getParameter("money");
			loggerWechat.debug("微信code:" + code);
			if (StringUtil.isEmpty(code)) {
				throw new RuntimeException("获取code失败");
			}

			// 订单判断是否已提交 如果提交判断提交平台是否对口
			WxOrder wxOrder = wxOrderMapper.selectByPrimaryKey(orderNo);
			if (wxOrder != null && !wxOrder.getState().equals("4")) {
				String result = WXUtil.checkOrderResource(wxOrder.getResource(), "1");
				if (!result.equals("pass")) {
					loggerWechat.debug(result);
					response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?flush=false&msg="
							+ URLEncoder.encode(result));
					return;
				}
			}

			// 通过code获取公众号对应的用户唯一openid code 5分钟不用就会过期
			String openId = "";
			String access_token = "";
			String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret
					+ "&code=" + code + "&grant_type=authorization_code";
			loggerWechat.debug("微信openId获取前");
			// https请求
			JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
			loggerWechat.debug(jsonObject);
			if (null != jsonObject) {
				openId = jsonObject.getString("openid");
				access_token = jsonObject.getString("access_token");
				loggerWechat.debug("微信openId:" + openId);
				// 保存用户唯一openId
				userDetailsMapper.saveOpenId(openId, userId);
			}

			// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
			String currTime = TenpayUtil.getCurrTime();
			// 8位日期
			String strTime = currTime.substring(8, currTime.length());
			// 四位随机数
			String strRandom = TenpayUtil.buildRandom(4) + "";
			// 10位序列号,可以自行调整。
			// 随机数
			// String nonce_str = strTime + strRandom;
			String nonce_str = Common.getUuid();
			// 子商户号 非必输
			// String sub_mch_id="";
			// 设备号 非必输
			// String device_info="";
			// 商品描述
			// String body = describe;
			// 商品描述根据情况修改
			String body = "链旧回收";
			// 附加数据
			String attach = "微信公众号支付";
			// 商户订单号
			String out_trade_no = orderNo;
			// 金额转化为分为单位
			String orMoney = money;
			float sessionmoney = Float.parseFloat(money);
			String finalmoney = String.format("%.2f", sessionmoney);
			finalmoney = finalmoney.replace(".", "");
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// 总金额以分为单位，不带小数点
			int total_fee = Integer.parseInt(money);
			// 订单生成的机器 IP
			String spbill_create_ip = request.getRemoteAddr();
			// String spbill_create_ip = WXUtil.getIpAddr(request);
			// String spbill_create_ip = "121.35.0.139";
			// 订 单 生 成 时 间 非必输
			// String time_start ="";
			// 订单失效时间 非必输
			// String time_expire = "";
			// 商品标记 非必输
			// String goods_tag = "";

			// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
			String notify_url = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxRollBack";

			// 保存付款对象信息
			WxOrder order = new WxOrder();
			order.setOutTradeNo(out_trade_no);
			order.setType("1");// 付款
			order.setTotalFee(money);
			order.setUserId(userId);
			order.setNonce_str(Common.getUuid());
			order.setUser_ip(spbill_create_ip);
			RequestHandler reqHandler = new RequestHandler(request, response);
			String data = WXUtil.getrechargeXML(notify_url, order, spbill_create_ip, reqHandler);// 获取XML报文

			order.setTotalFee(orMoney);
			order.setMessage(data);
			order.setDate(DateUtils.getTime());
			order.setResource(WXUtil.WXGZH);

			String trade_type = "JSAPI";
			String openid = openId;
			// 非必输
			// String product_id = "";
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			packageParams.put("appid", appid);
			packageParams.put("mch_id", mch_id);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("body", body);
			packageParams.put("attach", attach);
			packageParams.put("out_trade_no", out_trade_no);

			// 这里写的金额为1 分到时修改
			packageParams.put("total_fee", String.valueOf(total_fee));
			// packageParams.put("total_fee", "finalmoney");
			packageParams.put("spbill_create_ip", spbill_create_ip);
			packageParams.put("notify_url", notify_url);

			packageParams.put("trade_type", trade_type);
			packageParams.put("openid", openid);

			// RequestHandler：微信支付服务器签名支付请求请求类
			RequestHandler reqHandler1 = new RequestHandler(request, response);
			reqHandler1.init(appid, appsecret, partnerkey);

			// 创建md5摘要
			String sign = reqHandler.createSign(packageParams);
			String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>" + mch_id + "</mch_id>" + "<nonce_str>"
					+ nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body + "]]></body>"
					+ "<attach>" + attach + "</attach>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
					// 金额，这里写的1 分到时修改
					"<total_fee>" + String.valueOf(total_fee) + "</total_fee>" +
					// "<total_fee>"+finalmoney+"</total_fee>"+
					"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + "<notify_url>" + notify_url
					+ "</notify_url>" + "<trade_type>" + trade_type + "</trade_type>" + "<openid>" + openid
					+ "</openid>" + "</xml>";
			loggerWechat.debug(xml);
			String allParameters = "";
			try {
				// 获取package的签名包
				allParameters = reqHandler.genPackage(packageParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 统一支付接口url
			String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String prepay_id = "";
			try {

				prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
				loggerWechat.debug("prepay_id:" + prepay_id);
				if (prepay_id.trim().isEmpty()) {
					// request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
					String keyword1 = "获取预支付订单出错,请重新支付";
					loggerWechat.debug("获取预支付订单出错,请重新支付");
					response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?flush=false&msg="
							+ URLEncoder.encode(keyword1));
					return;
				} else if (!prepay_id.startsWith("wx") && !prepay_id.startsWith("WX")) {
					loggerWechat.debug(prepay_id);
					response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?flush=false&msg="
							+ URLEncoder.encode(prepay_id));
					return;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 保存订单信息 ***
			order.setState("1");
			WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
			if (wo == null) {
				wxOrderMapper.insertWXOrder(order);
			} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
				throw new RuntimeException("订单已支付成功,请勿重复提交");
			} else {
				wxOrderMapper.updateByPrimaryKeySelective(order);
			}

			SortedMap<String, String> finalpackage = new TreeMap<String, String>();
			String appid2 = appid;
			// 获取当前时间戳
			String timestamp = Sha1Util.getTimeStamp();
			// 随机数
			String nonceStr2 = nonce_str;
			// 预支付ID
			String prepay_id2 = "prepay_id=" + prepay_id;
			String packages = prepay_id2;
			finalpackage.put("appId", appid2);
			finalpackage.put("timeStamp", timestamp);
			finalpackage.put("nonceStr", nonceStr2);
			finalpackage.put("package", packages);
			// 签名类型
			finalpackage.put("signType", "MD5");
			String finalsign = reqHandler.createSign(finalpackage);
			finalpackage.put("sign", finalsign);
			// 定向返回给前端
			System.out
					.println("https://m.lianjiuhuishou.com/pay?id=" + out_trade_no + "&appid=" + appid2 + "&timeStamp="
							+ timestamp + "&nonceStr=" + nonceStr2 + "&package=" + packages + "&sign=" + finalsign);
			response.sendRedirect(
					"https://m.lianjiuhuishou.com/pay?id=" + out_trade_no + "&appid=" + appid2 + "&timeStamp="
							+ timestamp + "&nonceStr=" + nonceStr2 + "&package=" + packages + "&sign=" + finalsign);
			loggerWechat.debug("请求成功");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 微信h5支付 统一下单接口
	 * 
	 * @param money
	 * @param vip
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxpayh5", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxpayH5return(HttpServletRequest request, HttpServletResponse response,
			String money, String userId, String orderNo) {
		loggerWechat.info("微信h5支付 统一下单接口:wxpayh5");
		try {
			if (StringUtil.hasEmpty(money, userId, orderNo)) {
				return LianjiuResult.build(404, "参数不要为空");
			}
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(orderNo);
			String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(orderNo);
			if (ePrice == null) {
				ePrice = "0";
			}
			if (fPrice == null) {
				fPrice = "0";
			}
			if (!Double.valueOf(fPrice).equals(Double.valueOf(money))
					&& !Double.valueOf(ePrice).equals(Double.valueOf(money))) {
				return LianjiuResult.build(501, "数据不正确,请重新输入");
			}

			// 订单判断是否已提交 如果提交判断提交平台是否对口
			WxOrder wxOrder = wxOrderMapper.selectByPrimaryKey(orderNo);
			if (wxOrder != null && !wxOrder.getState().equals("4")) {
				String result = WXUtil.checkOrderResource(wxOrder.getResource(), "2");
				if (!result.equals("pass")) {
					loggerWechat.debug(result);
					return LianjiuResult.build(501, result);
				}
			}

			// 网页授权后获取传递的参数
			String code = request.getParameter("code");
			// 商户订单号
			// String orderNo = Common.getczid();
			// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
			// 随机数
			String nonce_str = Common.getUuid();
			// 子商户号 非必输
			// String sub_mch_id="";
			// 设备号 非必输
			// String device_info="";
			// 商品描述根据情况修改
			String body = "链旧回收";
			// 附加数据
			String attach = "微信h5支付";
			// 商户订单号
			String out_trade_no = orderNo;
			String orMoney = money;
			// 金额转化为分为单位
			float sessionmoney = Float.parseFloat(money);
			String finalmoney = String.format("%.2f", sessionmoney);
			finalmoney = finalmoney.replace(".", "");
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// 总金额以分为单位，不带小数点
			int total_fee = Integer.parseInt(money);
			// 订单生成的机器 IP
			// String spbill_create_ip = request.getRemoteAddr();
			// String spbill_create_ip = IPUtil.getIp2(request);
			// String spbill_create_ip =
			// InetAddress.getLocalHost().getHostAddress();
			String spbill_create_ip = WXUtil.getIpAddr(request);
			if (spbill_create_ip.equalsIgnoreCase("127.0.0.1") || spbill_create_ip.equalsIgnoreCase("172.17.222.186")) {
				spbill_create_ip = "121.35.0.31";
			}
			loggerWechat.debug("最终的spbill_create_ip:" + spbill_create_ip);
			// 订 单 生 成 时 间 非必输
			// String time_start ="";
			// 订单失效时间 非必输
			// String time_expire = "";
			// 商品标记 非必输
			// String goods_tag = "";
			// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
			String notify_url = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxRollBack";
			String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://m.lianjiuhuishou.com\",\"wap_name\": \"lianjiuhuishou\"}}";
			// 保存用户数据
			WxOrder order = new WxOrder();
			order.setType("1");// 充值
			order.setTotalFee(orMoney);
			order.setUserId(userId);
			order.setNonce_str(nonce_str);
			order.setUser_ip(spbill_create_ip);
			RequestHandler reqHandler = new RequestHandler(request, response);
			String data = WXUtil.getrechargeXMLH5(notify_url, order, spbill_create_ip, reqHandler);// 获取XML报文
			order.setMessage(data);
			order.setDate(DateUtils.getTime());
			order.setOutTradeNo(out_trade_no);
			order.setResource(WXUtil.WXH5);
			String trade_type = "MWEB";
			// 非必输
			// String product_id = "";
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			packageParams.put("appid", appid);
			packageParams.put("attach", attach);
			packageParams.put("body", body);
			packageParams.put("mch_id", mch_id);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("notify_url", notify_url);
			packageParams.put("out_trade_no", out_trade_no);
			packageParams.put("scene_info", scene_info);
			// 这里写的金额为1 分到时修改
			packageParams.put("spbill_create_ip", spbill_create_ip);
			packageParams.put("total_fee", String.valueOf(total_fee));
			// packageParams.put("total_fee", "finalmoney");
			packageParams.put("trade_type", trade_type);

			// RequestHandler：微信支付服务器签名支付请求请求类
			RequestHandler reqHandler1 = new RequestHandler(request, response);
			reqHandler1.init(appid, appsecret, partnerkey);

			// 创建md5摘要
			String sign = reqHandler.createSign(packageParams);
			loggerWechat.debug("后续spbill_create_ip:" + spbill_create_ip);
			String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<attach>" + attach + "</attach>"
					+ "<body><![CDATA[" + body + "]]></body>" + "<mch_id>" + mch_id + "</mch_id>" + "<nonce_str>"
					+ nonce_str + "</nonce_str>" + "<notify_url>" + notify_url + "</notify_url>" + "<out_trade_no>"
					+ out_trade_no + "</out_trade_no>" + "<scene_info>" + scene_info + "</scene_info>" + "<sign>" + sign
					+ "</sign>" + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + "<total_fee>"
					+ total_fee + "</total_fee>" + "<trade_type>" + trade_type + "</trade_type>" + "</xml>";
			loggerWechat.debug("最终xml:" + xml);
			String allParameters = "";
			try {
				// 获取package的签名包
				allParameters = reqHandler.genPackage(packageParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 统一支付接口url
			String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String mweb_url = "";
			try {
				// h5支付返回的mvwb_url就是封装好的支付链接数据,直接发给前端,转发就好
				mweb_url = GetWxOrderno.getPayNo1(createOrderURL, xml);
				if (mweb_url.equals("")) {
					return LianjiuResult.build(501, "统一支付接口获取预支付订单出错");
					// response.sendRedirect("error.jsp");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 添加成功支付后回调地址
			String redirect_url = URLEncoder
					.encode("https://m.lianjiuhuishou.com/settlementofsuccess2?orderNo=" + out_trade_no);
			mweb_url = mweb_url + "&redirect_url=" + redirect_url;
			// 保存订单信息 ***
			order.setMessage(xml);
			order.setState("1");
			WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
			if (wo == null) {
				wxOrderMapper.insertWXOrder(order);
			} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
				return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
			} else {
				wxOrderMapper.updateByPrimaryKeySelective(order);
			}
			// 返回mweb_url即可
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("weixin", mweb_url);
			loggerWechat.debug("返回成功::" + map);
			return LianjiuResult.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		loggerWechat.info("微信app支付统一下单接口:wxpayapp");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
				return LianjiuResult.build(404, "参数不要为空");
			}
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
			String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
			if (ePrice == null) {
				ePrice = "0";
			}
			if (fPrice == null) {
				fPrice = "0";
			}
			if (!Double.valueOf(fPrice).equals(Double.valueOf(money))
					&& !Double.valueOf(ePrice).equals(Double.valueOf(money))) {
				return LianjiuResult.build(501, "数据不正确,请重新输入");
			}
			// 增加10%的汇率
			Double cash = Double.valueOf(money);
			cash = cash * 1.1;
			money = String.format("%.2f", cash);
			String ormoney = money;
			// 微信下单接口
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// 回调这个接口的地址
			String notify_url = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxRollBack";
			String out_trade_no = outTradeNo;
			// 待定获取
			WxOrder order = new WxOrder();
			order.setOutTradeNo(out_trade_no);
			order.setType("1");// 支付
			order.setTotalFee(money);
			order.setUserId(userId);
			order.setNonce_str(Common.getUuid());
			order.setUser_ip(request.getRemoteAddr());
			RequestHandler reqHandler = new RequestHandler(request, response);
			String data = WXUtil.getrechargeXMLJM(notify_url, order, reqHandler);// 获取XML报文
			order.setMessage(data);
			order.setTotalFee(ormoney);
			order.setDate(DateUtils.getTime());
			order.setResource(WXUtil.WXIOSJMS);

			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.setRequestHeader("Connection", "keep-alive");
			method.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
			// HttpMethodParams p = new HttpMethodParams();
			// method.setParams(p);
			loggerWechat.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerWechat.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerWechat.debug("document:" + document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				// ehCacheMananger.put("wxOrderCache", out_trade_no, "1");

				order.setState("1");// 请求成功
				// 保存订单信息 ***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}

				String prepay_id = document.getRootElement().element("prepay_id").getText();
				loggerWechat.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", document.getRootElement().element("appid").getText());
				map.put("partnerid", document.getRootElement().element("mch_id").getText());
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerWechat.debug("请求成功");
				loggerWechat.debug(map);
				return LianjiuResult.ok(map);
			} else {
				order.setState("1");// 请求失败
				// 保存订单信息***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}
				return LianjiuResult.build(404, "调微信支付接口下单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(404, "统一下单接口异常");
		}
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
		loggerWechat.info("微信app支付统一下单接口:wxpayapp");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
				return LianjiuResult.build(404, "参数不要为空");
			}
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
			String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
			if (ePrice == null) {
				ePrice = "0";
			}
			if (fPrice == null) {
				fPrice = "0";
			}
			if (!Double.valueOf(fPrice).equals(Double.valueOf(money))
					&& !Double.valueOf(ePrice).equals(Double.valueOf(money))) {
				return LianjiuResult.build(501, "数据不正确,请重新输入");
			}

			// 订单判断是否已提交 如果提交判断提交平台是否对口
			WxOrder wxOrder = wxOrderMapper.selectByPrimaryKey(outTradeNo);
			if (wxOrder != null && !wxOrder.getState().equals("4")) {
				String result = WXUtil.checkOrderResource(wxOrder.getResource(), "3");
				if (!result.equals("pass")) {
					loggerWechat.debug(result);
					return LianjiuResult.build(501, result);
				}
			}

			// 微信下单接口
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String ormoney = money;
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// String notifyUrl = ub.manageUrl("wxcallbackURL",
			// "wxcallbackURL");
			// 调这个接口的地址
			String notify_url = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxRollBack";
			String out_trade_no = outTradeNo;

			String nonceStr = Common.getUuid();
			// 待定获
			WxOrder order = new WxOrder();
			order.setOutTradeNo(out_trade_no);
			order.setType("1");// 充值
			order.setTotalFee(money);
			order.setUserId(userId);
			order.setNonce_str(nonceStr);
			order.setUser_ip(request.getRemoteAddr());
			String data = WXUtil.getrechargeXMLJP(notify_url, order, request, response);// 获取XML报文
			// String data = WXUtil.getrechargeXML(notify_url, order);// 获取XML报文
			order.setMessage(data);
			order.setTotalFee(ormoney);
			order.setDate(DateUtils.getTime());
			order.setResource(WXUtil.WXIOSJP);

			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.setRequestHeader("Connection", "keep-alive");
			method.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
			loggerWechat.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerWechat.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				// result = iresult.getResult(ResultBiz.FAIL);
				// jsonObj(response,result.toJson());
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerWechat.debug(document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				// ehCacheMananger.put("wxOrderCache", out_trade_no, "1");

				order.setState("1");// 请求成功
				// 保存订单信息 ***
				loggerWechat.debug("保存订单信息:");
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}

				String prepay_id = document.getRootElement().element("prepay_id").getText();
				loggerWechat.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", WXUtil.appIdJP);
				map.put("partnerid", WXUtil.mch_idJP);
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(WXUtil.appIdJP, WXUtil.keyJP, partnerkey);
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerWechat.debug("请求成功");
				loggerWechat.debug(map);
				return LianjiuResult.ok(map);

			} else {
				order.setState("1");// 请求失败
				// 保存订单信息 ***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}
				return LianjiuResult.build(404, "调微信支付接口下单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(404, "统一下单接口异常");
		}
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
		loggerWechat.info("微信app支付统一下单接口:wxpayapp");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
				return LianjiuResult.build(404, "参数不要为空");
			}
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
			String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
			if (ePrice == null) {
				ePrice = "0";
			}
			if (fPrice == null) {
				fPrice = "0";
			}
			if (!Double.valueOf(fPrice).equals(Double.valueOf(money))
					&& !Double.valueOf(ePrice).equals(Double.valueOf(money))) {
				return LianjiuResult.build(501, "数据不正确,请重新输入");
			}
			// 增加10%的汇率
			Double cash = Double.valueOf(money);
			cash = cash * 1.1;
			money = String.format("%.2f", cash);
			// 微信下单接口
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String ormoney = money;
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// String notifyUrl = ub.manageUrl("wxcallbackURL",
			// "wxcallbackURL");
			// 调这个接口的地址
			String notify_url = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxRollBack";
			String out_trade_no = outTradeNo;
			// 待定获取
			WxOrder order = new WxOrder();
			order.setOutTradeNo(out_trade_no);
			order.setType("1");// 充值
			order.setTotalFee(money);
			order.setUserId(userId);
			order.setNonce_str(Common.getUuid());
			order.setUser_ip(request.getRemoteAddr());
			String data = WXUtil.getrechargeXMLAndroid(notify_url, order, request, response);// 获取XML报文
			order.setMessage(data);
			order.setTotalFee(ormoney);
			order.setDate(DateUtils.getTime());
			order.setResource(WXUtil.WXAndroidJMS);
			// 跨域请求下单接口
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.setRequestHeader("Connection", "keep-alive");
			method.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
			loggerWechat.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerWechat.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				// result = iresult.getResult(ResultBiz.FAIL);
				// jsonObj(response,result.toJson());
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerWechat.debug(document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				// ehCacheMananger.put("wxOrderCache", out_trade_no, "1");

				order.setState("1");// 请求成功
				// 保存订单信息 ***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}
				String prepay_id = document.getRootElement().element("prepay_id").getText();
				loggerWechat.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", WXUtil.appIdAn);
				map.put("partnerid", WXUtil.mch_idAn);
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(WXUtil.appIdAn, WXUtil.keyAn, partnerkey);
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerWechat.debug("请求成功");
				loggerWechat.debug(map);
				return LianjiuResult.ok(map);

			} else {
				order.setState("1");// 请求失败
				// 保存订单信息 ***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}
				return LianjiuResult.build(404, "调微信支付接口下单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(404, "统一下单接口异常");
		}
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
		loggerWechat.info("微信app支付统一下单接口:wxpayapp");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
			if (StringUtil.hasEmpty(money, userId, outTradeNo)) {
				return LianjiuResult.build(404, "参数不要为空");
			}
			String ePrice = ordersExcellentMapper.slectPriceByOrderNo(outTradeNo);
			String fPrice = ordersFacefaceDetailsMapper.slectPriceByOrderNo(outTradeNo);
			if (ePrice == null) {
				ePrice = "0";
			}
			if (fPrice == null) {
				fPrice = "0";
			}
			if (!Double.valueOf(fPrice).equals(Double.valueOf(money))
					&& !Double.valueOf(ePrice).equals(Double.valueOf(money))) {
				return LianjiuResult.build(501, "数据不正确,请重新输入");
			}

			// 订单判断是否已提交 如果提交判断提交平台是否对口
			WxOrder wxOrder = wxOrderMapper.selectByPrimaryKey(outTradeNo);
			if (wxOrder != null && !wxOrder.getState().equals("4")) {
				String result = WXUtil.checkOrderResource(wxOrder.getResource(), "3");
				if (!result.equals("pass")) {
					loggerWechat.debug(result);
					return LianjiuResult.build(501, result);
				}
			}

			// 微信下单接口
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String ormoney = money;
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// String notifyUrl = ub.manageUrl("wxcallbackURL",
			// "wxcallbackURL");
			// 调这个接口的地址
			String notify_url = "https://orders.lianjiuhuishou.com/orders/weixinWeb/wxRollBack";
			String out_trade_no = outTradeNo;
			// 待定获取
			WxOrder order = new WxOrder();
			order.setOutTradeNo(out_trade_no);
			order.setType("1");// 充值
			order.setTotalFee(money);
			order.setUserId(userId);
			order.setNonce_str(Common.getUuid());
			order.setUser_ip(request.getRemoteAddr());
			String data = WXUtil.getrechargeXMLAndroidJP(notify_url, order, request, response);// 获取XML报文
			order.setMessage(data);
			order.setTotalFee(ormoney);
			order.setDate(DateUtils.getTime());
			order.setResource(WXUtil.WXAndroidJP);

			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.setRequestHeader("Connection", "keep-alive");
			method.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
			loggerWechat.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerWechat.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				// result = iresult.getResult(ResultBiz.FAIL);
				// jsonObj(response,result.toJson());
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerWechat.debug(document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				// ehCacheMananger.put("wxOrderCache", out_trade_no, "1");

				order.setState("1");// 请求成功
				// 保存订单信息 ***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}

				String prepay_id = document.getRootElement().element("prepay_id").getText();
				loggerWechat.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", WXUtil.appIdJPAn);
				map.put("partnerid", WXUtil.mch_idJPAn);
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(WXUtil.appIdJPAn, WXUtil.keyJPAn, partnerkey);
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerWechat.debug("请求成功");
				loggerWechat.debug(map);
				return LianjiuResult.ok(map);

			} else {
				order.setState("1");// 请求失败
				// 保存订单信息***
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}
				return LianjiuResult.build(404, "调微信支付接口下单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(404, "统一下单接口异常");
		}
	}

	/**
	 * 微信支付统一回调接口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("wxRollBack")
	@ResponseBody
	public synchronized void wxRollBack(HttpServletRequest request, HttpServletResponse response) {
		loggerWechat.debug("微信支付回调接口");
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader reader = null;
		SortedMap<Object, Object> parameter = new TreeMap<Object, Object>();
		try {
			reader = request.getReader();
			while (null != (line = reader.readLine())) {
				data.append(line);
			}
			Document document = DocumentHelper.parseText(data.toString());
			loggerWechat.debug(document.getRootElement().element("return_code").getText());
			Element element = document.getRootElement();
			if ("SUCCESS".equals(element.element("return_code").getText())) {
				loggerWechat.debug("回调成功success");
				// 商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
				// 同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
				String outTradeNo = element.element("out_trade_no").getText();
				WxOrder order = new WxOrder();
				order.setOutTradeNo(outTradeNo);
				order = wxOrderMapper.queryWXOrder(order);
				if (!"SUCCESS".equals(element.element("result_code").getText())) {
					loggerWechat.debug("返回faill失败");
					jedisClient.del("orderCache");
					// ehCacheMananger.remove("orderCache", outTradeNo);
					parameter.put("return_code", "fail");
					parameter.put("return_msg", "失败");
					order.setState("3");// 回调失败
					order.setReMessage(data.toString());
					wxOrderMapper.updateByPrimaryKeySelective(order);
					jedisClient.hset("wxOrderCache", order.getOutTradeNo(), "3");
					return;
				}
				// 更改订单表的订单状态
				OrdersFaceface orders = new OrdersFaceface();
				orders.setOrFacefaceId(outTradeNo);
				orders.setOrFacefaceStatus((byte) 3);
				OrdersExcellent excellent = new OrdersExcellent();
				excellent.setOrExcellentStatus((byte) 2);
				excellent.setOrExcellentId(outTradeNo);
				excellent.setOrExcellentUpdated(new Date());
				int f = ordersFacefaceMapper.updateFaceFaceFinishState(orders);
				int e = ordersExcellentMapper.modifyExcellentFinishState(excellent);
				if (f + e < 2) {
					throw new RuntimeException("订单状态修改失败");
				}
				// 加盟商
				if (f >= 2) {
					// 修改加盟商订单每星期状态
					int i = allianceBusinessDetailsMapper.modifyWeeklyState(outTradeNo);
					if (i == 1) {
						loggerWechat.info("加盟商订单每星期状态修改成功");
					} else {
						loggerWechat.info("加盟商订单每星期状态修改失败");
					}
					// 增加加盟商流水单
					String money = element.element("total_fee").getText();
					Double num = Double.valueOf(money) / 100;
					money = Double.toString(num);
					loggerWechat.debug("加盟商支付现金money:" + money);
					UserWalletRecord user = new UserWalletRecord();
					user.setRecordId(IDUtils.genGUIDs());
					user.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));
					user.setRelativeId(outTradeNo);
					user.setRecordCreated(new Date());
					user.setRecordUpdated(new Date());
					user.setIsIncome((byte) 3);
					OrdersFaceface order1 = ordersFacefaceMapper.selectByPrimaryKey(outTradeNo);
					user.setUserId(order1.getOrFacefaceAllianceId());
					user.setRecordName(order1.getOrItemsNamePreview());
					userWalletRecordMapper.insertSelective(user);
					// 增加用户流水单
					Double cash = Double.valueOf(money);
					cash = cash / 1.1;
					money = String.format("%.2f", cash);

					loggerWechat.debug("用户支付现金money:" + money);
					user.setRecordId(IDUtils.genGUIDs());
					user.setUserId(order1.getUserId());
					user.setRecordPrice(String.format("%.2f", Float.parseFloat(money)));
					user.setIsIncome((byte) 5);
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
						loggerWechat.debug("打款失败");
					}
				}

				order.setState("4");// 回调成功
				order.setSign(document.getRootElement().element("sign").getText());
				order.setReMessage(data.toString());
				wxOrderMapper.updateByPrimaryKeySelective(order);
				String vip = order.getUserId();
				Double money = Double.parseDouble(String.valueOf((Double.valueOf(order.getTotalFee()) / 100)));// 转化为单位元字符串
				// 校验钱数

				String bank = document.getRootElement().element("bank_type").getText();

				parameter.put("return_code", "SUCCESS");
				parameter.put("return_msg", "OK");

			} else {
				parameter.put("return_code", "SUCCESS");
				parameter.put("return_msg", "OK");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/json; charset=UTF-8");
			try {
				response.getWriter().write(WXUtil.xml(parameter));
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

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
	@RequestMapping(value = "wxpayRefound", method = RequestMethod.POST)
	@ResponseBody
	public synchronized LianjiuResult wxRefund(String money, String userId, String outTradeNo,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// 查询支付单信息
			WxOrder wo = wxOrderMapper.selectByPrimaryKey(outTradeNo);
			// 查询用户所有成功订单
			WxOrder order = new WxOrder();
			order.setUserId(userId);
			List<WxOrder> wxList = wxOrderMapper.queryWXOrderList(order);

			Double refee = Double.valueOf(money);// 退款金额
			Double ReMoney = 0.00;// 已退款金额
			Double accountMoney = Double.valueOf(wo.getTotalFee());// 支付的金额
			Double wxRefundMoney = 0.00;
			if (refee > accountMoney) {
				// 退款金额错误
				// result = iresult.getResult(ResultBiz.money);
				return LianjiuResult.build(400, "退款金额错误");
			}

			// 指定读取证书格式为PKCS12
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			// 读取本机存放的PKCS12证书文件
			// String keyStoreurl = ub.manageUrl("keyStore", "keyStore");
			String keyStoreurl = "/apiclient_cert.p12";
			FileInputStream instream = new FileInputStream(new File(keyStoreurl));
			try {
				// 指定PKCS12的密码(商户ID)
				keyStore.load(instream, "1489873322".toCharArray());
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1489873322".toCharArray()).build();
			// 指定TLS版本
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			// 设置httpclient的SSLSocketFactory
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

			HttpPost httpPost = new HttpPost(url);// 退款接口
			// loggerWechat.debug("executing request" +
			// httpPost.getRequestLine());

			// 设置类型

			Double ReMoneySu = 0.00;
			WxOrder wo1 = new WxOrder();
			wo1.setTotalFee(Double.toString(refee * 100));
			wo1.setOutTradeNo(IDUtils.genOrdersId());
			WxOrder wo2 = new WxOrder();
			wo2.setTotalFee(Double.toString(accountMoney));
			wo2.setOutTradeNo(outTradeNo);
			String data = WXUtil.getRefundXML(wo1, wo2);
			wo1.setType("2");// 退款
			wo1.setUserId(userId);
			wo1.setMessage(data);
			wo1.setDate(DateUtils.getTime());
			wo1.setResource(outTradeNo);
			loggerWechat.debug("data:" + data);

			StringEntity reqEntity = new StringEntity(data);
			reqEntity.setContentType("text/xml;charset=UTF-8");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse responses = httpclient.execute(httpPost);
			String message = EntityUtils.toString(responses.getEntity(), "utf-8");
			loggerWechat.debug("返回消息：" + message);
			// method.releaseConnection();
			// client.getHttpConnectionManager().closeIdleConnections(0);
			org.dom4j.Document document = DocumentHelper.parseText(message);
			loggerWechat.debug("document :" + document);
			loggerWechat.debug("return_code:" + document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())) {
				wo1.setState("2");// 请求成功
				// 保存订单信息
				wxOrderMapper.insertWXOrder(wo1);
				ReMoneySu = MathUtil.add(ReMoneySu, (Double.valueOf(wo1.getTotalFee()) / 100));
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
	 * 微信支付查询
	 * 
	 * @param out_trade_no
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "querywxpay/{out_trade_no}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult querywxpay(@PathVariable String out_trade_no, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			RequestHandler reqHandler = new RequestHandler(request, response);
			String data = WXUtil.getQueryXML(out_trade_no, reqHandler);
			String url = "https://api.mch.weixin.qq.com/pay/orderquery";
			org.apache.http.client.HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);// 查询接口
			StringEntity reqEntity = new StringEntity(data);
			reqEntity.setContentType("text/xml;charset=UTF-8");
			httpPost.setEntity(reqEntity);
			HttpResponse responses = httpClient.execute(httpPost);
			String message = EntityUtils.toString(responses.getEntity(), "utf-8");
			loggerWechat.debug("返回消息：" + message);
			// method.releaseConnection();
			// client.getHttpConnectionManager().closeIdleConnections(0);
			Document document = DocumentHelper.parseText(message);
			loggerWechat.debug(document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				if (document.getRootElement().element("trade_state").getText().equals("NOTPAY")) {
					return LianjiuResult.build(501, "订单未支付");
				}
				// 微信支付成功 状态修改
				WxOrder wx = new WxOrder();
				wx.setOutTradeNo(out_trade_no);
				;
				wx.setState("2");
				wxOrderMapper.updateByPrimaryKeySelective(wx);

				return LianjiuResult.ok();
			} else {
				if (document.getRootElement().element("trade_state_desc") != null) {
					loggerWechat.debug(
							"结果信息trade_state_desc:" + document.getRootElement().element("trade_state_desc").getText());
					return LianjiuResult.build(501, document.getRootElement().element("trade_state_desc").getText());
				}
				return LianjiuResult.build(501, "支付失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(501, "系统异常");
		}
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
	public LianjiuResult ModifyStatus(String out_trade_no, String status, HttpServletRequest request,
			HttpServletResponse response) {
		loggerWechat.debug("微信支付状态改变");
		if (!StringUtils.areNotEmpty(out_trade_no, status)) {
			return LianjiuResult.build(501, "订单号不能为空");
		}
		WxOrder wx = new WxOrder();
		wx.setOutTradeNo(out_trade_no);
		wx.setState(status);
		WxOrder wx1 = wxOrderMapper.selectByPrimaryKey(out_trade_no);
		if (!wx1.getState().equals("4")) {
			int i = wxOrderMapper.updateByPrimaryKeySelective(wx);
			if (i < 1) {
				return LianjiuResult.build(501, "支付状态修改失败");
			}
		}

		return LianjiuResult.ok();
	}
}
