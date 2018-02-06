package com.lianjiu.payment.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.wxh5.RequestHandler;

public class WXUtil {
	@Autowired
	private static Logger logger  = Logger.getLogger(WXUtil.class);
	//公众号 h5
	public static String appId1 = "wx801dbcf0056597c9";
	public static String mch_id1 = "1489873322";
	public static String key1 = "b2382f1e41559a2e855462446affd2b5";
	
	//所有的app商户api密匙统一设置为下边的这个
	public static String partnerkey = "FFB42680CDABAE725B6C3FAA43C259A4";
	//ios加盟商app
	public static String mch_id="1492506362";
	public static String appId="wxdbb404784649a663";
	public static String key="9ab8bb1517b8914aef4b7926353524bc";
	//ios二手精品 链旧app
	public static String mch_idJP="1492499632";
	public static String appIdJP="wx567ca296081c4142";
	public static String keyJP="41038de2813c51f3a4b9ac104d899a31";
	//android加盟商app
	public static String appIdAn="wx60b696775220fa1a";
	public static String keyAn="8add87c624b40bc1f713b724383f6822";
	public static String mch_idAn="1493057822";
	//android二手精品 链旧app
	public static String appIdJPAn="wxd65e1a42250d8aa1";
	public static String keyJPAn="9682d0d5b7597e3e2593b34fa1a59093";
	public static String mch_idJPAn="1492886892";

	public static final String WXGZH = "1";//微信公众号
	public static final String WXH5 = "2";//微信H5
	public static final String WXIOSJMS = "3";//微信ios加盟商
	public static final String WXIOSJP = "4";//微信ios链旧精品
	public static final String WXAndroidJMS = "5";//微信Android加盟商
	public static final String WXAndroidJP = "6";//微信Android链旧精品
	
	
	
	public static String getrechargeXML(String notifyUrl,WxOrder order,String spbill_create_ip,RequestHandler reqHandler) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		System.out.println("下单的localip:"+localip);
		String attach="微信app支付ios";
		String body="链旧回收";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="JSAPI";
		SortedMap<String,String> parameters = new TreeMap<String,String>();
		
		parameters.put("appid", appId1);
		parameters.put("attach", attach);
		parameters.put("body", body);
		parameters.put("mch_id", mch_id1);
		parameters.put("nonce_str", nonce_str);
		parameters.put("notify_url", notifyUrl);
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", localip);
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", tardeType);
		
		//sign=MD5Util.wxmd5JM(parameters,key1);
		reqHandler.init(appId1, key1, partnerkey);
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml1(parameters);
		
	}
	
	
	public static String getrechargeXMLH5(String notifyUrl,WxOrder order,String spbill_create_ip,RequestHandler reqHandler) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		System.out.println("下单的localip:"+localip);
		String attach="微信H5支付";
		String body="链旧回收";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="MWEB";
		SortedMap<String,String> parameters = new TreeMap<String,String>();
		
		parameters.put("appid", appId1);
		parameters.put("attach", attach);
		parameters.put("body", body);
		parameters.put("mch_id", mch_id1);
		parameters.put("nonce_str", nonce_str);
		parameters.put("notify_url", notifyUrl);
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", spbill_create_ip);
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", tardeType);
		
		//sign=MD5Util.wxmd5JM(parameters,key1);
		reqHandler.init(appId1, key1, partnerkey);
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml1(parameters);
		
	}
	//ios加盟商app
		public static String getrechargeXMLJM(String notifyUrl,WxOrder order,RequestHandler reqHandler) throws UnknownHostException{
			InetAddress ia=InetAddress.getLocalHost();
			String localip=ia.getHostAddress();
			String attach="微信app支付ios";
			String body="链旧回收";
			String nonce_str=Common.getUuid();
			String sign="";
			String tardeType="APP";
			SortedMap<String,String> parameters = new TreeMap<String,String>();
			
			parameters.put("appid", appId);
			parameters.put("attach", attach);
			parameters.put("body", body);
			parameters.put("mch_id", mch_id);
			parameters.put("nonce_str", nonce_str);
			parameters.put("notify_url", notifyUrl);
			parameters.put("out_trade_no", order.getOutTradeNo());
			parameters.put("spbill_create_ip", localip);
			parameters.put("total_fee", order.getTotalFee());
			parameters.put("trade_type", tardeType);
			
			reqHandler.init(appId, key, partnerkey);
			
			// 创建md5摘要
			sign = reqHandler.createSign(parameters);
			parameters.put("sign", sign);
			order.setSign(sign);
			
			return xml1(parameters);
			
		}
	//ios二手精品 链旧app
	public static String getrechargeXMLJP(String notifyUrl,WxOrder order,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		String attach="微信app支付ios";
		String body="链旧回收";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="APP";
		SortedMap<String,String> parameters = new TreeMap<String, String>();
		
		parameters.put("appid", appIdJP);
		parameters.put("attach", attach);
		parameters.put("body", body);
		parameters.put("mch_id", mch_idJP);
		parameters.put("nonce_str", nonce_str);
		parameters.put("notify_url", notifyUrl);
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", localip);
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", tardeType);
		
		//sign=MD5Util.wxmd5JM(parameters,keyJP);
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appIdJP, keyJP, partnerkey);
		
		// 创建md5摘要
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml1(parameters);
		
	}
	//android加盟商app
	public static String getrechargeXMLAndroid(String notifyUrl,WxOrder order,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		String attach="微信app支付android";
		String body="链旧回收";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="APP";
		SortedMap<String,String> parameters = new TreeMap<String, String>();
		
		parameters.put("appid", appIdAn);
		parameters.put("attach", attach);
		parameters.put("body", body);
		parameters.put("mch_id", mch_idAn);
		parameters.put("nonce_str", nonce_str);
		parameters.put("notify_url", notifyUrl);
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", localip);
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", tardeType);
		
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appIdAn, keyAn, partnerkey);
		
		// 创建md5摘要
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml1(parameters);
		
	}
	//android二手精品 链旧app
	public static String getrechargeXMLAndroidJP(String notifyUrl,WxOrder order,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		String attach="微信app支付android";
		String body="链旧回收";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="APP";
		SortedMap<String,String> parameters = new TreeMap<String, String>();
		
		parameters.put("appid", appIdJPAn);
		parameters.put("attach", attach);
		parameters.put("body", body);
		parameters.put("mch_id", mch_idJPAn);
		parameters.put("nonce_str", nonce_str);
		parameters.put("notify_url", notifyUrl);
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", localip);
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", tardeType);
		
		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(appIdJPAn, keyJPAn, partnerkey);
		
		// 创建md5摘要
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml1(parameters);
		
	}
	public static String getrechargeXMLpc(String notifyUrl,WxOrder order) throws UnknownHostException{
		
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		String attach="QR扫码支付";
		String body="链旧网支付";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="NATIVE";
		SortedMap<Object,Object> parameters = new TreeMap<Object, Object>();
		
		parameters.put("appid", appId);
		parameters.put("attach", attach);
		parameters.put("body", body);
		parameters.put("mch_id", mch_id);
		parameters.put("nonce_str", nonce_str);
		parameters.put("notify_url", notifyUrl);
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", localip);
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", tardeType);
		
		sign=MD5Util.wxmd5(parameters);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml(parameters);
		
	}
	
	public static String getQueryXML(String out_trade_no, RequestHandler reqHandler) throws UnknownHostException{
		String nonce_str=Common.getUuid();
		String sign="";
		SortedMap<String,String> parameters = new TreeMap<String,String>();
		
		parameters.put("appid", appId1);
		parameters.put("mch_id", mch_id1);
		parameters.put("nonce_str", nonce_str);
		parameters.put("out_trade_no", out_trade_no);
		
		reqHandler.init(appId1, key1, partnerkey);
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		return xml1(parameters);
	}
	
	public static String getRefundXML(WxOrder order1,WxOrder order2) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String nonce_str=Common.getUuid();
		String sign="";
		SortedMap<Object,Object> parameters = new TreeMap<Object, Object>();
		
		parameters.put("appid", appId);
		parameters.put("mch_id", mch_id);
		parameters.put("nonce_str", nonce_str);
		parameters.put("op_user_id", mch_id);
		parameters.put("out_refund_no", order1.getOutTradeNo());
		parameters.put("out_trade_no", order2.getOutTradeNo());
		parameters.put("refund_fee", order1.getTotalFee());
		parameters.put("total_fee", order2.getTotalFee());
		parameters.put("transaction_id", "");

		
		sign=MD5Util.wxmd5(parameters);
		parameters.put("sign", sign);
		order1.setSign(sign);
		
		return xml(parameters);
		
	}
	private void recharge() {
		// TODO Auto-generated method stub
		
	}
	public static String xml(SortedMap<Object, Object> parameters){
		 StringBuffer sb = new StringBuffer();  
         sb.append("<xml>");  
         Set es = parameters.entrySet();  
         Iterator it = es.iterator();  
         while(it.hasNext()) {  
              Map.Entry entry = (Map.Entry)it.next();  
              String k = (String)entry.getKey();  
              String v = (String)entry.getValue();  
              if(!StringUtil.isEmpty(v)) {  
                  sb.append("<" + k +">" + v + "</" + k + ">\n");  
              }  
          }  
         sb.append("</xml>");
         return sb.toString();
	}
	public static String xml1(SortedMap<String, String> parameters){
		 StringBuffer sb = new StringBuffer();  
        sb.append("<xml>");  
        Set es = parameters.entrySet();  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
             Map.Entry entry = (Map.Entry)it.next();  
             String k = (String)entry.getKey();  
             String v = (String)entry.getValue();  
             if(!StringUtil.isEmpty(v)) {  
                 sb.append("<" + k +">" + v + "</" + k + ">\n");  
             }  
         }  
        sb.append("</xml>");
        return sb.toString();
	}

 
    public final static String getIpAddr(HttpServletRequest request) throws IOException {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  
        if (logger.isInfoEnabled()) {  
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);  
        }  
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("X-Real-IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - X-Real-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("X-Forwarded-For");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("PROXY_FORWARDED_FOR");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - PROXY_FORWARDED_FOR - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);  
                }  
            }  
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip))  {
                try {  
                    ip = InetAddress.getLocalHost().getHostAddress();  
                    System.out.println("最后的真实ip"+ip);
                }  
                catch (UnknownHostException unknownhostexception) {  
                }  
            }  
        } 
        return ip;  
    }


	public static String checkOrderResource(String resource,String type) {
		if(StringUtil.isEmpty(resource)||resource.length()>3){
			return "pass";
		}
		if(type.equals("1")){
			if(resource.equals("1")){
				return "pass";
			}else if(resource.equals("2")){
				return "您提交并取消支付的订单不能在微信内支付，请返回浏览器重新支付";
			}else if(Integer.parseInt(resource)>2&&Integer.parseInt(resource)<6){
				return "您提交并取消支付的订单不能在微信内支付，请返回链旧APP重新支付";
			}else if(resource.equals("0")){
				return "您提交并取消支付的订单不能在微信内支付，请返回电脑端重新支付";
			}else{
				return "pass";
			}
		}
		if(type.equals("2")){
			if(resource.equals("1")){
				return "您提交并取消支付的订单不能在浏览器支付，请返回微信内重新支付";
			}else if(resource.equals("2")){
				return "pass";
			}else if(Integer.parseInt(resource)>2&&Integer.parseInt(resource)<6){
				return "您提交并取消支付的订单不能在浏览器支付，请返回链旧APP重新支付";
			}else if(resource.equals("0")){
				return "您提交并取消支付的订单不能在浏览器支付，请返回电脑端重新支付";
			}else{
				return "pass";
			}
		}
		if(type.equals("3")){
			if(resource.equals("1")){
				return "您提交并取消支付的订单不能在链旧APP支付，请返回微信内重新支付";
			}else if(resource.equals("2")){
				return "您提交并取消支付的订单不能在链旧APP支付，请返回浏览器重新支付";
			}else if(Integer.parseInt(resource)>2&&Integer.parseInt(resource)<6){
				return "pass";
			}else if(resource.equals("0")){
				return "您提交并取消支付的订单不能在链旧APP支付，请返回电脑端重新支付";
			}else{
				return "pass";
			}
		}
		if(type.equals("0")){
			if(resource.equals("1")){
				return "您提交并取消支付的订单不能在电脑端支付，请返回微信内重新支付";
			}else if(resource.equals("2")){
				return "您提交并取消支付的订单不能在电脑端支付，请返回浏览器重新支付";
			}else if(Integer.parseInt(resource)>2&&Integer.parseInt(resource)<6){
				return "您提交并取消支付的订单不能在电脑端支付，请返回链旧APP重新支付";
			}else if(resource.equals("0")){
				return "pass";
			}else{
				return "pass";
			}
		}
		
		return resource;
		
	} 
	
    
    
}
