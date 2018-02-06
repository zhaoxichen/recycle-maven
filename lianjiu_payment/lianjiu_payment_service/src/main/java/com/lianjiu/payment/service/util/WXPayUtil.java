package com.lianjiu.payment.service.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.StringUtil;
import com.lianjiu.model.WxOrder;
import com.lianjiu.payment.dao.wxh5.RequestHandler;

public class WXPayUtil {
		// 公众号商户相关资料
		public static final String APPID = "wx801dbcf0056597c9";
		// 公众号应用密钥
		public static final String APPSECRET = "b2382f1e41559a2e855462446affd2b5";
		// 公众号商户号
		public static final String MCH_ID = "1489873322";
		// 公众号商户密钥
		public static final String PARTNERKEY = "FFB42680CDABAE725B6C3FAA43C259A4";
		
		//微信支付
		public final static String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		// 企业付款
		public static final String TRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers"; 

		// 企业付款查询
		public static final String TRANSFERS_PAY_QUERY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo"; 
		
		//正式script
		public static final String API_SECRET = "b2382f1e41559a2e855462446affd2b5";
		
		//本机ip
		public static final String LOCAL_IP = "127.0.0.1";
		//服务器ip
		public static final String SERVER_IP="172.17.222.186";
		//回调链接
		public static final String ROLLBACK_URL = "https://payment.lianjiuhuishou.com/payment/vxController/vxPayReturn";

		public static final String WXGZH = "1";//微信公众号
		public static final String WXH5 = "2";//微信H5
		public static final String WXIOSJMS = "3";//微信ios加盟商
		public static final String WXIOSJP = "4";//微信ios链旧精品
		public static final String WXAndroidJMS = "5";//微信Android加盟商
		public static final String WXAndroidJP = "6";//微信Android链旧精品
		
		
		
		public static String getrechargeXML(String notify_url, WxOrder order, String spbill_create_ip,
			RequestHandler reqHandler) {
			InetAddress ia = null;
			try {
				ia = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String localip=ia.getHostAddress();
			System.out.println("下单的localip:"+localip);
			String attach="微信app支付ios";
			String body="链旧回收";
			String nonce_str=Common.getUuid();
			String sign="";
			String tardeType="JSAPI";
			SortedMap<String,String> parameters = new TreeMap<String,String>();
			
			parameters.put("appid", APPID);
			parameters.put("attach", attach);
			parameters.put("body", body);
			parameters.put("mch_id", MCH_ID);
			parameters.put("nonce_str", nonce_str);
			parameters.put("notify_url", notify_url);
			parameters.put("out_trade_no", order.getOutTradeNo());
			parameters.put("spbill_create_ip", localip);
			parameters.put("total_fee", order.getTotalFee());
			parameters.put("trade_type", tardeType);
			
			//sign=MD5Util.wxmd5JM(parameters,key1);
			reqHandler.init(APPID, MCH_ID, PARTNERKEY);
			sign = reqHandler.createSign(parameters);
			parameters.put("sign", sign);
			order.setSign(sign);
			
			return xml1(parameters);

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
}









