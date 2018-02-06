package com.lianjiu.payment.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.DateUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.OrdersExcellent;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.dao.config.WXUtil;
import com.lianjiu.payment.dao.wxh5.CommonUtil;
import com.lianjiu.payment.service.WxPcService;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.WxOrderMapper;
import com.lianjiu.rest.mapper.orders.OrdersExcellentMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.user.UserDetailsMapper;

import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;
@Service
public class WxPcServiceImpl implements WxPcService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private WxOrderMapper wxOrderMapper;
	@Autowired
	private UserDetailsMapper userDetailsMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private OrdersExcellentMapper ordersExcellentMapper;
	
	@Override
	public void wxpayGZH(String money, HttpServletRequest request, HttpServletResponse response) {
		//共账号及商户相关参数
			String appid = "wx77f8b42d3163922d";
			String orderNo =  Common.getczid();
			String backUri = "http://www.lianjiuhuishou.com/WeixinPc/wxpayreturn";
			//授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
			//最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
			//比如 Sign = %3D%2F%CS% 
			backUri = backUri+"?userId=b88001&orderNo="+orderNo+"&describe=test&money="+money;
			//URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
			backUri = URLEncoder.encode(backUri);
			//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
					"appid=" + appid+
					"&redirect_uri=" +
					 backUri+
					"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public void wxpay(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId"); 	
		String orderNo = request.getParameter("orderNo"); 
		String money = request.getParameter("money");
		String code = request.getParameter("code");
		try{
			//保存openid
			String appsecret = "ad72a5189014adc5dbcb92fe97cb7f3b";
			String openId ="";
			String appid = "";
			String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
			JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
			System.out.println(jsonObject);
			if (null != jsonObject) {
				openId = jsonObject.getString("openid");
				//保存用户唯一openId
				userDetailsMapper.saveOpenId(openId,userId);
				
			}
			
			
			if (!StringUtil.isNotBlank(money)) {
				return;
			}
			String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
			//单位为分
			money=String.valueOf((int)(Double.valueOf(money)*100));
			String notifyUrl = "";
			String out_trade_no = Common.getczid();
			WxOrder order = new WxOrder();
			order.setOutTradeNo(out_trade_no);
			order.setType("1");//充值
			order.setTotalFee(money);
			order.setUserId(userId);
			String data=WXUtil.getrechargeXMLpc(notifyUrl, order);//获取XML报文
			order.setMessage(data);
			order.setDate(DateUtils.getTime());
			
			HttpClient client = new HttpClient();
			PostMethod  method=new PostMethod(url);
			method.setRequestHeader("Connection","keep-alive");
			method.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
//				HttpMethodParams p = new HttpMethodParams(); 
//				method.setParams(p);
			System.out.println(data);
			method.setRequestBody(data);
					//			NameValuePair[] data = {new NameValuePair("tId",tId),new NameValuePair("time",date),new NameValuePair("releaseId",fbr.getVip()),new NameValuePair("buyerId",tbr.getVip())};
//				method.setQueryString(data);
			client.executeMethod(method);
			byte[] b=method.getResponseBody(); 
//				System.out.println();
			String message=new String(b,"utf-8");
			System.out.println("返回消息："+message);
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
			if(!StringUtil.isNotBlank(message)){
				return;
			}
			Document document = DocumentHelper.parseText(message);
			System.out.println(document.getRootElement().element("return_code").getText());
			if("SUCCESS".equals(document.getRootElement().element("return_code").getText())&&"SUCCESS".equals(document.getRootElement().element("result_code").getText())){
					jedisClient.hset("wxOrderCache", out_trade_no, "1");
					
					order.setState("2");//请求成功
					//保存订单信息
					wxOrderMapper.insertWXOrder(order);
					//生成二维码
					int width = 200; // 图像宽度  
			        int height = 200; // 图像高度  
			        String format = "png";// 图像类型  
			        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
			        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
			        BitMatrix bitMatrix = new MultiFormatWriter().encode(document.getRootElement().element("code_url").getText(),  
			                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵  
			        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					ImageIO.write(image, format, baos);  
					byte[] bytes = baos.toByteArray();  
					String base64 =  new BASE64Encoder().encode(bytes);
//				        MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream());
//				        MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像  
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("img", base64);
					map.put("out_trade_no", out_trade_no);
			        System.out.println("输出成功.");  
			        return;
			}else{
				order.setState("1");//请求失败
				//保存订单信息
				wxOrderMapper.insertWXOrder(order);
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void wxRollBack(HttpServletRequest request, HttpServletResponse response) {
	 	StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        SortedMap<Object, Object> parameter = new TreeMap<Object, Object>();
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine())){
                data.append(line);
                }
            Document document = DocumentHelper.parseText(data.toString());
			System.out.println(document.getRootElement().element("return_code").getText());
			Element element = document.getRootElement();
			if("SUCCESS".equals(element.element("return_code").getText())){
				//商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
				//同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
				String outTradeNo=element.element("out_trade_no").getText();
				String token=jedisClient.set("orderCache", outTradeNo);
				WxOrder order = new WxOrder();
				order.setOutTradeNo(outTradeNo);
				order = wxOrderMapper.queryWXOrder(order);
				if(!"SUCCESS".equals(element.element("result_code").getText())){
					jedisClient.del("orderCache");
					//ehCacheMananger.remove("orderCache", outTradeNo);
					parameter.put("return_code", "SUCCESS");
					parameter.put("return_msg", "OK");
					order.setState("3");//回调失败
					order.setReMessage(data.toString());
					wxOrderMapper.updateByPrimaryKeySelective(order);
					jedisClient.hset("wxOrderCache", order.getOutTradeNo(),"3");
					return;
				}
				//这里幂等性验证也可以通过修改业务来避免
				if(!StringUtils.isEmpty(token)){
					jedisClient.del("orderCache");
					parameter.put("return_code", "SUCCESS");
					parameter.put("return_msg", "OK");
					return;
					
				}
				jedisClient.hset("orderCache", outTradeNo, outTradeNo);
				String totalFee= document.getRootElement().element("total_fee").getText();
				if(!order.getTotalFee().equals(totalFee)){
					parameter.put("return_code", "FAIL");
					parameter.put("return_msg", "金额错误");
					order.setState("3");//回调失败
					order.setReMessage(data.toString());
					wxOrderMapper.updateByPrimaryKeySelective(order);
					jedisClient.del("orderCache");
					return;
				}
				String sign1= document.getRootElement().element("sign").getText();
				SortedMap<Object, Object> map = new TreeMap<Object, Object>();
				for (Iterator iterator = document.getRootElement().elementIterator(); iterator.hasNext();) {  
			         Element e = (Element) iterator.next();  
			         map.put(e.getName(), e.getText());  
			      }
				map.remove("sign");
				String sign2 = MD5Util.wxmd5(map);
//							String notifyUrl = userService.manageUrl("wxcallbackURL", "wxcallbackURL");
//							String sign1=order.getSign();//报文签名
//							WXUtil.getrechargeXML(notifyUrl, order);
//							String sign2=order.getSign();//返回报文重新生成签名
				if(!sign1.equals(sign2)){
					parameter.put("return_code", "FAIL");
					parameter.put("return_msg", "签名失败");
					order.setSign(sign1);
					order.setState("3");//回调失败
					order.setReMessage(data.toString());
					wxOrderMapper.updateByPrimaryKeySelective(order);
					jedisClient.del("orderCache");
					return;
				}
				
				//更改订单表的订单状态
				OrdersFaceface orders = new OrdersFaceface();
				orders.setOrFacefaceId(outTradeNo);
				orders.setOrFacefaceStatus((byte)3);
				OrdersExcellent excellent = new OrdersExcellent();
				excellent.setOrExcellentStatus((byte)4);
				excellent.setOrExcellentId(outTradeNo);
				excellent.setOrExcellentUpdated(new Date());
				if(ordersFacefaceMapper.updateFaceFaceState(orders)+ordersExcellentMapper.modifyExcellentState(excellent)!=1){
					throw new RuntimeException("订单状态修改失败");
				}
				
				
				
				order.setState("4");//回调成功
				String vip = order.getUserId();
				Double money = Double.parseDouble(String.valueOf((Double.valueOf(order.getTotalFee())/100)));//转化为单位元字符串
				//校验钱数
				
				String bank= document.getRootElement().element("bank_type").getText();
				
			/*	TenderMiddle tm=new TenderMiddle(Common.getUuid(),vip,"1",order.getOutTradeNo(),DateUtils.getTime(),"2","微信支付",bank,"","","","",money,"");
				vipService.recharge(tm);
				//为账户增加金额
				tenderService.addAccountBalance(vip,money);
				//为统计表充值字段增加金额
				tenderService.addAmountCount(money);
				order.setRe_message(data.toString());
				userService.updateWXOrder(order);
				
				//推送
				List<String> list=new ArrayList<String>();
				list.add(order.getOut_trade_no());
				list.add(String.valueOf(money));
				String str=PushModel.pushModel("cn","wxchongzhi", list);
				Push p=new Push(Common.getUuid(), str.split("/")[0], str.split("/")[1], vip, "1", DateUtils.getTime(),"cn");
				pushService.SetPush(p);
				
				
				ehCacheMananger.put("wxOrderCache", order.getOut_trade_no(),"2");
				ehCacheMananger.remove("orderCache", outTradeNo);
				parameter.put("return_code", "SUCCESS");
				parameter.put("return_msg", "OK");*/
				
			}else{
				parameter.put("return_code", "SUCCESS");
				parameter.put("return_msg", "OK");
				
			}
			
	//			    <xml>
	//  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
	//  <attach><![CDATA[支付测试]]></attach>
	//  <bank_type><![CDATA[CFT]]></bank_type>
	//  <fee_type><![CDATA[CNY]]></fee_type>
	//  <is_subscribe><![CDATA[Y]]></is_subscribe>
	//  <mch_id><![CDATA[10000100]]></mch_id>
	//  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
	//  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
	//  <out_trade_no><![CDATA[1409811653]]></out_trade_no>
	//  <result_code><![CDATA[SUCCESS]]></result_code>
	//  <return_code><![CDATA[SUCCESS]]></return_code>
	//  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
	//  <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
	//  <time_end><![CDATA[20140903131540]]></time_end>
	//  <total_fee>1</total_fee>
	//  <trade_type><![CDATA[JSAPI]]></trade_type>
	//  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
	//	</xml>
	            
            
            
        } catch (Exception e) {
        	e.printStackTrace();
        } 
		
	}

}
