package com.lianjiu.payment.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.User;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.dao.config.CommonUtil;
import com.lianjiu.payment.dao.config.WXUtil;
import com.lianjiu.payment.dao.wxh5.GetWxOrderno;
import com.lianjiu.payment.dao.wxh5.RequestHandler;
import com.lianjiu.payment.dao.wxh5.Sha1Util;
import com.lianjiu.payment.service.VXService;
import com.lianjiu.payment.service.util.WXPayUtil;
import com.lianjiu.payment.service.utils.ConfigUtil;
import com.lianjiu.payment.service.utils.PayCommonUtil;
import com.lianjiu.payment.service.utils.XMLUtil;
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

@Service
public class VXServiceImpl implements VXService{
	

	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private UserDetailsMapper userDetailsMapper;

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
	
	@Autowired
	private static Logger loggerVisit = Logger.getLogger("wechat");
	//微信外支付
	@Override
	public LianjiuResult vxOutPay(HttpServletRequest request, HttpServletResponse response, String money, String userId,
			String orderNo) {
		loggerVisit.info("进入微信外支付：vxOutPay   userId:"+userId+",money:"+money+",orderNo:"+orderNo);
		// 订单判断是否已提交 如果提交判断提交平台是否对口
		WxOrder wxOrder = wxOrderMapper.selectByPrimaryKey(orderNo);
		if (wxOrder != null && !wxOrder.getState().equals("4")) {
			String result = WXUtil.checkOrderResource(wxOrder.getResource(), "2");
			if (!result.equals("pass")) {
				//loggerVisit.debug(result);
				return LianjiuResult.build(501, result);
			}
		}
		
		
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		String notify_url = WXPayUtil.ROLLBACK_URL;
		String nonce_str = Common.getUuid();
		parameters.put("appid", WXPayUtil.APPID);
		parameters.put("body", "微信支付");  
		parameters.put("mch_id", WXPayUtil.MCH_ID);  
		parameters.put("nonce_str", nonce_str);  
		parameters.put("notify_url", notify_url); 
		parameters.put("out_trade_no", orderNo); //订单id
		//parameters.put("spbill_create_ip",request.getRemoteAddr());
		parameters.put("trade_type","MWEB");
		String spbill_create_ip = request.getRemoteAddr();
		//WXUtil.getIpAddr(request);
		//System.out.println("request ip:"+spbill_create_ip);
		if(spbill_create_ip.equalsIgnoreCase(WXPayUtil.LOCAL_IP)||spbill_create_ip.equalsIgnoreCase(WXPayUtil.SERVER_IP)){
			spbill_create_ip = "121.35.0.31";
		}	
		
		//System.out.println("处理完之后的 ip:"+spbill_create_ip);
		parameters.put("spbill_create_ip",spbill_create_ip);
		String moneys = money;
		float sessionmoney = Float.parseFloat(money);
		String finalmoney = String.format("%.2f", sessionmoney);
		finalmoney = finalmoney.replace(".", "");
		money = String.valueOf((int) (Double.valueOf(money) * 100));
		// 总金额以分为单位，不带小数点
		int total_fee = Integer.parseInt(money);
		
		parameters.put("total_fee", String.valueOf(total_fee));  
		
		//设置签名
		String sign = PayCommonUtil.createSign("UTF-8",parameters);
		parameters.put("sign", sign);
		//请求XML
		String requestXML = PayCommonUtil.getRequestXml(parameters);  
		//调用统一下单接口
		System.out.println("requestXML："+requestXML);
		//请求过后 返回的XML
		String result = PayCommonUtil.httpsRequest(WXPayUtil.WX_PAY_URL, "POST", requestXML);
		System.out.println("结果："+result);
		
		String mweb_url = "";
		mweb_url = GetWxOrderno.getPayNo1(WXPayUtil.WX_PAY_URL, requestXML);
		System.out.println("开始获取 mweb_url:"+mweb_url);
		if (null == mweb_url || mweb_url.equals("")) {
			System.out.println("返回501     统一支付接口获取预支付订单出错：");
			return LianjiuResult.build(501, "订单号重复，同一笔交易不能多次提交");
		}
		mweb_url=mweb_url+"&redirect_url=https://m.lianjiuhuishou.com/settlementofsuccess2?orderNo="+orderNo;
		try {
			/**统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay**/
			Map<String, String> resultMap = XMLUtil.doXMLParse(result);
			SortedMap<Object, Object> parameterMap2 = new TreeMap<Object, Object>();  
			parameterMap2.put("appid", ConfigUtil.APPID);  
			parameterMap2.put("partnerid", ConfigUtil.MCH_ID);  
			parameterMap2.put("prepayid", resultMap.get("prepay_id"));  
			parameterMap2.put("package", "Sign=WXPay");  
			parameterMap2.put("noncestr", PayCommonUtil.CreateNoncestr());  
			parameterMap2.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis()).toString()));  
			String sign2 = PayCommonUtil.createSign("UTF-8",parameterMap2);
			parameterMap2.put("sign", sign2); 
			System.out.println("sign:"+sign);
			System.out.println("parameterMap2:"+parameterMap2.toString());
			
			WxOrder order = new WxOrder();
			order.setType("1");// 充值
			order.setTotalFee(moneys);
			order.setUserId(userId);
			order.setNonce_str(nonce_str);
			order.setUser_ip(spbill_create_ip);
			order.setMessage(requestXML);
			order.setReMessage(result);
			order.setDate(DateUtils.getTime());
			order.setOutTradeNo(orderNo);
			order.setResource(WXUtil.WXH5);
			order.setState("1");
			order.setSign(sign2);
			WxOrder wo = wxOrderMapper.selectByPrimaryKey(orderNo);
			System.out.println("微信外支付  进行查询："+wo);
			if (wo == null) {
				System.out.println("未查询到      开始进行添加："+order.toString());
				wxOrderMapper.insertWXOrder(order);
			} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
				System.out.println("微信外支付  进行查询："+wo);
				return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
			} else {
				System.out.println("查询到数据      开始进行修改："+order.toString());
				wxOrderMapper.updateByPrimaryKeySelective(order);
			}
			
		} catch (JDOMException e) {
			System.out.println("error:"+e.getMessage());
			return LianjiuResult.build(501, "付款时异常，请稍后尝试");
		} catch (IOException e) {
			System.out.println("error:"+e.getMessage());
			return LianjiuResult.build(501, "付款时异常，请稍后尝试");
		}
		String url = ConfigUtil.UNIFIED_ORDER_URL;
		url = url + "&redirect_url=https://m.lianjiuhuishou.com/settlementofsuccess2?orderNo="+orderNo;
		//return LianjiuResult.ok(url);
		System.out.println("最终url："+mweb_url);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("weixin", mweb_url);
		System.out.println("返回成功::"+map);			
		return LianjiuResult.ok(map);
	}
	//微信支付回调
	@Override
	public LianjiuResult vxPayReturn(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("开始进行微信支付回调-----------------------1");
		
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader reader = null;
		SortedMap<Object, Object> parameter = new TreeMap<Object, Object>();
		
		try {
			reader = request.getReader();
			System.out.println("reader:"+reader+"\n");
			while (null != (line = reader.readLine())) {
				System.out.println("line:"+line+"\n");
				data.append(line);
			}
			System.out.println("data:"+data);
			Document document = DocumentHelper.parseText(data.toString());
			System.out.println("return_code:"+document.getRootElement().element("return_code").getText());
			Element element = document.getRootElement();
			if ("SUCCESS".equals(element.element("return_code").getText())) {
				System.out.println("回调成功success");
				// 商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
				// 同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。

				String outTradeNo = element.element("out_trade_no").getText();
				WxOrder order = wxOrderMapper.selectByPrimaryKey(outTradeNo);
				try {
					if(null == order){
						order = new WxOrder();
					}
					order.setOutTradeNo(outTradeNo);
				} catch (NullPointerException e) {
					System.out.println("捕捉到空指针！");
					order = new WxOrder();
					order.setOutTradeNo(outTradeNo);
				}
				//= new WxOrder();
				//System.out.println("odrderNo:"+o);
				//order = wxOrderMapper.queryWXOrder(order);
				if (!"SUCCESS".equals(element.element("result_code").getText())) {
					System.out.println("返回faill失败");
					jedisClient.del("orderCache");
					// ehCacheMananger.remove("orderCache", outTradeNo);
					parameter.put("return_code", "fail");
					parameter.put("return_msg", "失败");
					order.setState("3");// 回调失败
					order.setReMessage(data.toString());
					wxOrderMapper.updateByPrimaryKeySelective(order);
					jedisClient.hset("wxOrderCache", order.getOutTradeNo(), "3");
					//return;
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
				System.out.println("修改ordersFacefaceMapper："+f+"条数据");
				System.out.println("修改ordersExcellentMapper："+e+"条数据");
				
				// 加盟商
				if (f >= 2) {
					// 修改加盟商订单每星期状态
					int i = allianceBusinessDetailsMapper.modifyWeeklyState(outTradeNo);
					if (i == 1) {
						loggerVisit.info("加盟商订单每星期状态修改成功");
					} else {
						loggerVisit.info("加盟商订单每星期状态修改失败");
					}
					// 增加加盟商流水单
					String money = element.element("total_fee").getText();
					Double num = Double.valueOf(money) / 100;
					money = Double.toString(num);
					System.out.println("加盟商支付现金money:" + money);
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
					

					loggerVisit.info("用户支付现金money:" + money);
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
						loggerVisit.info("打款失败");
					}
				}

				order.setState("4");// 回调成功
				order.setSign(document.getRootElement().element("sign").getText());
				order.setReMessage(data.toString());
				wxOrderMapper.updateByPrimaryKeySelective(order);
				System.out.println("------------      order:"+order.toString());
				System.out.println("order:"+order.getTotalFee());
//				String vip = order.getUserId();
//				Double money = Double.parseDouble(String.valueOf((Double.valueOf(order.getTotalFee()) / 100)));// 转化为单位元字符串
//				// 校验钱数
//				String bank = document.getRootElement().element("bank_type").getText();

				parameter.put("return_code", "SUCCESS");
				parameter.put("return_msg", "OK");

			} else {
				parameter.put("return_code", "SUCCESS");
				parameter.put("return_msg", "OK");

			}
			
		} catch (IOException | DocumentException e1) {
			e1.printStackTrace();
		}finally {

			System.out.println("开始进行微信支付回调-over----------------------4");
			
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
		
		return LianjiuResult.ok("回调成功~");
	}
	//微信支付查询
	@Override
	public LianjiuResult vxPayQuery(String out_trade_no, HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("进微信支付查询 vxPayQuery");
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
			System.out.println("返回消息：" + message);
			// method.releaseConnection();
			// client.getHttpConnectionManager().closeIdleConnections(0);
			Document document = DocumentHelper.parseText(message);
			System.out.println(document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				if (document.getRootElement().element("trade_state").getText().equals("NOTPAY")) {
					System.out.println("返回501     订单未支付：");
					return LianjiuResult.build( 501, "订单未支付");
				}
				//微信支付成功 状态修改
				WxOrder wx = new WxOrder();
				wx.setOutTradeNo(out_trade_no);;
				wx.setState("2");
				//wxOrderMapper.updateByPrimaryKeySelective(wx);
				loggerVisit.info("微信支付查询 vxPayQuery 结束");
				return LianjiuResult.ok();
			} else {
				if (document.getRootElement().element("trade_state_desc") != null) {
					
					System.out.println(
							"结果信息trade_state_desc:" + document.getRootElement().element("trade_state_desc").getText());
					return LianjiuResult.build(501, document.getRootElement().element("trade_state_desc").getText());
				}
				System.out.println("返回501     支付失败：");
				return LianjiuResult.build(501, "支付失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("返回501     系统异常：");
			return LianjiuResult.build(501, "系统异常");
		}
	}
	//微信内支付
	@Override
	public LianjiuResult vxInnerPay(HttpServletRequest request, HttpServletResponse response, String money,
			String userId, String orderNo) {
		// TODO Auto-generated method stub
		loggerVisit.info("进入微信内获取code：getCode   userId:"+userId+",money:"+money+",orderNo:"+orderNo);
		/*
			做一个价格验证
		*/
		String backUri = "https://payment.lianjiuhuishou.com/payment/vxController/vxInnerPayReturn";
		// 授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		// 最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
		// 比如 Sign = %3D%2F%CS%
		// money=String.valueOf((int)(Double.valueOf(money)*100));
		backUri = backUri + "?userId=" + userId + "&describe=test&money=" + money + "&orderNo=" + orderNo;
		// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		try {
			backUri = URLEncoder.encode(backUri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			loggerVisit.info("error："+e.getMessage());
			return LianjiuResult.build(501,"编码转换出错");
		}
		// scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + WXPayUtil.APPID + "&redirect_uri="
				+ backUri + "&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		System.out.println("url:--"+url);
		// response.sendRedirect(url);
		loggerVisit.info("返回的url:"+url);
		loggerVisit.info("进入微信内获取code：getCode over");
		return LianjiuResult.ok(url);
	}
	//微信内支付回调
	@Override
	public LianjiuResult vxInnerPayReturn(HttpServletRequest request, HttpServletResponse response) {
		loggerVisit.info("微信公众号支付:vxInnerPayReturn");
		String orderNo = request.getParameter("orderNo");
		String code = request.getParameter("code");
		String userId = request.getParameter("userId");
		String money = request.getParameter("money");
		System.out.println("微信code:" + code);
		System.out.println("微信orderNo:" + orderNo);
		System.out.println("微信userId:" + userId);
		System.out.println("微信money:" + money);
		if (StringUtil.isEmpty(code)) {
			return LianjiuResult.build(501,"获取code失败");
		}else if(StringUtil.isEmpty(orderNo)){
			return LianjiuResult.build(501,"获取orderNo失败");
		}else if(StringUtil.isEmpty(userId)){
			return LianjiuResult.build(501,"获取userId失败");
		}else if(StringUtil.isEmpty(money)){
			return LianjiuResult.build(501,"获取money失败");
		}
		// 订单判断是否已提交 如果提交判断提交平台是否对口
		WxOrder wxOrder = wxOrderMapper.selectByPrimaryKey(orderNo);
		if (wxOrder != null && !wxOrder.getState().equals("4")) {
			String result = WXUtil.checkOrderResource(wxOrder.getResource(), "1");
			if (!result.equals("pass")) {
				System.out.println(result);
				try {
					response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?flush=false&msg="
							+ URLEncoder.encode(result,"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					System.out.println("error:"+e.getMessage());
					return LianjiuResult.build(504, "error");
				} catch (IOException e) {
					System.out.println("error:"+e.getMessage());
					return LianjiuResult.build(504, "error");
				}
				return LianjiuResult.build(503, "订单支付中");
			}
		}
		
		// 通过code获取公众号对应的用户唯一openid code 5分钟不用就会过期
		String openId = "";
		String access_token = "";
		String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXPayUtil.APPID + "&secret=" + WXPayUtil.APPSECRET
				+ "&code=" + code + "&grant_type=authorization_code";
		System.out.println("微信openId获取前");

		// https请求
		JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
		System.out.println(jsonObject);
		if (null != jsonObject) {
			openId = jsonObject.getString("openid");
			access_token = jsonObject.getString("access_token");
			System.out.println("微信openId:" + openId);
			// 保存用户唯一openId
			userDetailsMapper.saveOpenId(openId, userId);
		}
		
		// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
		// 随机数
		String nonce_str = Common.getUuid();
		// 商品描述根据情况修改
		String body = "链旧回收";
		// 附加数据
		String attach = "微信公众号支付";
		// 商户订单号
		String out_trade_no = orderNo;
		// 金额转化为分为单位(微信默认需要*100)
		String orMoney = money;
		float sessionmoney = Float.parseFloat(money);
		String finalmoney = String.format("%.2f", sessionmoney);
		finalmoney = finalmoney.replace(".", "");
		money = String.valueOf((int) (Double.valueOf(money) * 100));
		int total_fee = Integer.parseInt(money);
		// 订单生成的机器 IP
		String spbill_create_ip = request.getRemoteAddr();
		// 这里notify_url是 支付完成后回调
		String notify_url = WXPayUtil.ROLLBACK_URL;
		// 保存付款对象信息
		WxOrder order = new WxOrder();
		order.setOutTradeNo(out_trade_no);
		order.setType("1");// 付款
		order.setTotalFee(money);
		order.setUserId(userId);
		order.setNonce_str(Common.getUuid());
		order.setUser_ip(spbill_create_ip);
		RequestHandler reqHandler = new RequestHandler(request, response);
		String data = WXPayUtil.getrechargeXML(notify_url, order, spbill_create_ip, reqHandler);// 获取XML报文
		order.setTotalFee(orMoney);
		order.setMessage(data);
		order.setDate(DateUtils.getTime());
		order.setResource(WXPayUtil.WXGZH);
		
		String trade_type = "JSAPI";
		String openid = openId;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", WXPayUtil.APPID);
		packageParams.put("mch_id", WXPayUtil.MCH_ID);
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
		reqHandler1.init(WXPayUtil.APPID, WXPayUtil.APPSECRET, WXPayUtil.PARTNERKEY);
		// 创建md5摘要
		String sign = reqHandler.createSign(packageParams);
		order.setSign(sign);
		
		String xml = "<xml>" + "<appid>" + WXPayUtil.APPID + "</appid>" + "<mch_id>" + WXPayUtil.MCH_ID + "</mch_id>" + "<nonce_str>"
				+ nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body + "]]></body>"
				+ "<attach>" + attach + "</attach>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
				// 金额，这里写的1 分到时修改
				"<total_fee>" + String.valueOf(total_fee) + "</total_fee>" +
				"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type + "</trade_type>" + "<openid>" + openid
				+ "</openid>" + "</xml>";
		//PayCommonUtil.getRequestXml(parameters);   拼接XML  待测
		System.out.println("0--------------------------拼接完成后的xml："+xml);
		String allParameters = "";
		try {
			// 获取package的签名包 为了获取预支付id
			allParameters = reqHandler.genPackage(packageParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String createOrderURL = WXPayUtil.WX_PAY_URL;
		String prepay_id = "";
		
		try {
			Map<String,String> map = GetWxOrderno.getPayNo(createOrderURL, xml);
			prepay_id = map.get("prepay_id");
			if(null == prepay_id){
				return LianjiuResult.build(503, "获取预prepay_id出错");
			}
			order.setReMessage("json");
			System.out.println("prepay_id:" + prepay_id);
			if (prepay_id.trim().isEmpty()) {
				// request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
				System.out.println("获取预支付订单出错,请重新支付");
				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess2?orderNo="+orderNo);
				return LianjiuResult.build(501, "获取预支付订单出错,请重新支付");
			}
//			else if (!prepay_id.startsWith("wx") && !prepay_id.startsWith("WX")) {
//				System.out.println(prepay_id);
//				response.sendRedirect("https://m.lianjiuhuishou.com/settlementofsuccess?flush=false&msg="
//						+ URLEncoder.encode(prepay_id,"UTF-8"));
//				return;
//			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// 保存订单信息 ***
		order.setState("1");
		WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
		if (wo == null) {
			wxOrderMapper.insertWXOrder(order);
		} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
			//throw new RuntimeException("订单已支付成功,请勿重复提交");
			return LianjiuResult.build(503,"订单已支付成功,请勿重复提交");
		} else {
			wxOrderMapper.updateByPrimaryKeySelective(order);
		}
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String appid2 = WXPayUtil.APPID;
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
		System.out.println("https://m.lianjiuhuishou.com/pay?id=" + out_trade_no + "&appid=" + appid2 + "&timeStamp="
						+ timestamp + "&nonceStr=" + nonceStr2 + "&package=" + packages + "&sign=" + finalsign);
		try {
			response.sendRedirect(
					"https://m.lianjiuhuishou.com/pay?id=" + out_trade_no + "&appid=" + appid2 + "&timeStamp="
							+ timestamp + "&nonceStr=" + nonceStr2 + "&package=" + packages + "&sign=" + finalsign);
		} catch (IOException e) {
			System.out.println("error:"+e.getMessage());
			return LianjiuResult.build(503, "重定向给前端出现错误！");
		}
		System.out.println("请求成功");
		return LianjiuResult.ok("成功");
	}
	//修改订单状态
	@Override
	public LianjiuResult ModifyStatus(String out_trade_no, String status, HttpServletRequest request,
			HttpServletResponse response) {
		WxOrder wx = new WxOrder();
		wx.setOutTradeNo(out_trade_no);
		wx.setState(status);
		WxOrder wx1 = wxOrderMapper.selectByPrimaryKey(out_trade_no);
		if(!wx1.getState().equals("4")){
			int i=wxOrderMapper.updateByPrimaryKeySelective(wx);		
			if(i<1){
				System.out.println("返回付状态修改失bai ");
				return LianjiuResult.build(501,	 "支付状态修改失败");
			}
		}
		System.out.println("返回ok");
		return LianjiuResult.ok();
	}
	//微信app支付Android加盟商 统一下单接口
	@Override
	public LianjiuResult wxapppayAndroid(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response) {
		loggerVisit.info("微信app支付统一下单接口:wxpayapp");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
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
			String url = WXPayUtil.WX_PAY_URL;
			String ormoney = money;
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// String notifyUrl = ub.manageUrl("wxcallbackURL",
			// "wxcallbackURL");
			// 调这个接口的地址
			String notify_url = WXPayUtil.ROLLBACK_URL;
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
			loggerVisit.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerVisit.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				// result = iresult.getResult(ResultBiz.FAIL);
				// jsonObj(response,result.toJson());
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerVisit.debug(document.getRootElement().element("return_code").getText());
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
				loggerVisit.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", WXUtil.appIdAn);
				map.put("partnerid", WXUtil.mch_idAn);
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(WXUtil.appIdAn, WXUtil.keyAn, WXPayUtil.PARTNERKEY);
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerVisit.debug("请求成功");
				loggerVisit.debug(map);
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
				return LianjiuResult.build(503, "调微信支付接口下单失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(504, "统一下单接口异常");
		}
	}
	//微信app支付Android精品 统一下单接口
	@Override
	public LianjiuResult wxapppayAndroidJP(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response) {
		loggerVisit.info("进入微信app支付Android精品 统一下单接口：wxpayapp   userId:"+userId+",money:"+money+",orderNo:"+outTradeNo);
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			//String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
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
					loggerVisit.debug(result);
					return LianjiuResult.build(501, result);
				}
			}

			// 微信下单接口
			String url = WXPayUtil.WX_PAY_URL;
			String ormoney = money;
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// String notifyUrl = ub.manageUrl("wxcallbackURL",
			// "wxcallbackURL");
			// 调这个接口的地址
			String notify_url = WXPayUtil.ROLLBACK_URL;
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
			loggerVisit.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerVisit.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				// result = iresult.getResult(ResultBiz.FAIL);
				// jsonObj(response,result.toJson());
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerVisit.debug(document.getRootElement().element("return_code").getText());
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
				loggerVisit.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", WXUtil.appIdJPAn);
				map.put("partnerid", WXUtil.mch_idJPAn);
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(WXUtil.appIdJPAn, WXUtil.keyJPAn, WXPayUtil.PARTNERKEY);
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerVisit.debug("请求成功");
				loggerVisit.debug(map);
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
	
	@Override
	public LianjiuResult wxpayappJP(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response) {
		loggerVisit.info("进入微信ios支付Android精品 统一下单接口：wxpayappJP   userId:"+userId+",money:"+money+",orderNo:"+outTradeNo);
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
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
					loggerVisit.debug(result);
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
			String notify_url = WXPayUtil.ROLLBACK_URL;
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
			loggerVisit.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerVisit.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				loggerVisit.debug("调微信支付接口下单异常~  message is null");
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerVisit.debug(document.getRootElement().element("return_code").getText());
			if ("SUCCESS".equals(document.getRootElement().element("return_code").getText())
					&& "SUCCESS".equals(document.getRootElement().element("result_code").getText())) {
				// ehCacheMananger.put("wxOrderCache", out_trade_no, "1");

				order.setState("1");// 请求成功
				// 保存订单信息 ***
				loggerVisit.debug("保存订单信息:");
				WxOrder wo = wxOrderMapper.selectByPrimaryKey(out_trade_no);
				if (wo == null) {
					wxOrderMapper.insertWXOrder(order);
				} else if (wo.getState().equals("4") && wo.getType().equals("1")) {
					return LianjiuResult.build(501, "订单已支付成功,请勿重复提交");
				} else {
					wxOrderMapper.updateByPrimaryKeySelective(order);
				}

				String prepay_id = document.getRootElement().element("prepay_id").getText();
				loggerVisit.debug("生成prepay_id:" + prepay_id);
				SortedMap<String, String> map = new TreeMap<String, String>();
				map.put("appid", WXUtil.appIdJP);
				map.put("partnerid", WXUtil.mch_idJP);
				map.put("prepayid", prepay_id);
				map.put("package", "Sign=WXPay");
				map.put("noncestr", Common.getUuid());
				map.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
				// 签名类型
				RequestHandler reqHandler = new RequestHandler(request, response);
				reqHandler.init(WXUtil.appIdJP, WXUtil.keyJP, WXPayUtil.PARTNERKEY);
				String finalsign = reqHandler.createSign(map);
				map.put("sign", finalsign);
				loggerVisit.debug("请求成功");
				loggerVisit.debug(map);
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

	@Override
	public LianjiuResult wxpayapp(String money, String userId, String outTradeNo, HttpServletRequest request,
			HttpServletResponse response) {
		loggerVisit.info("微信app支付统一下单接口:wxpayapp");
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
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
			String url = WXPayUtil.WX_PAY_URL;
			// 单位为分
			money = String.valueOf((int) (Double.valueOf(money) * 100));
			// 回调这个接口的地址
			String notify_url = WXPayUtil.ROLLBACK_URL;
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
			loggerVisit.debug(data);
			method.setRequestBody(data);
			client.executeMethod(method);
			byte[] b = method.getResponseBody();
			String message = new String(b, "utf-8");
			loggerVisit.debug("返回消息：" + message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if (StringUtil.isEmpty(message)) {
				return LianjiuResult.build(404, "调微信支付接口下单异常");
			}
			Document document = DocumentHelper.parseText(message);
			loggerVisit.debug("document:" + document.getRootElement().element("return_code").getText());
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
				loggerVisit.debug("生成prepay_id:" + prepay_id);
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
				loggerVisit.debug("请求成功");
				loggerVisit.debug(map);
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
	
}
