package com.lianjiu.service.RefoundConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.lianjiu.common.utils.Common;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.model.WxOrder;

public class WXUtils {
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
		

	@SuppressWarnings("unused")
	public static String getRefundXML(WxOrder order1,WxOrder order2,int num, RequestHandler reqHandler) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String nonce_str=Common.getUuid();
		String sign="";
		SortedMap<String,String> parameters = new TreeMap<String,String>();
		if(num==1||num==2){
			parameters.put("appid", appId1);
			parameters.put("mch_id", mch_id1);
			parameters.put("nonce_str", nonce_str);
			parameters.put("op_user_id", mch_id1);
			parameters.put("out_refund_no", order1.getOutTradeNo());
			parameters.put("out_trade_no", order2.getOutTradeNo());
			parameters.put("refund_fee", order1.getTotalFee());
			parameters.put("total_fee", order2.getTotalFee());
			parameters.put("transaction_id", "");
			reqHandler.init(appId1, key1, partnerkey);
		}else if(num==3){
			parameters.put("appid", appId);
			parameters.put("mch_id", mch_id);
			parameters.put("nonce_str", nonce_str);
			parameters.put("op_user_id", mch_id);
			parameters.put("out_refund_no", order1.getOutTradeNo());
			parameters.put("out_trade_no", order2.getOutTradeNo());
			parameters.put("refund_fee", order1.getTotalFee());
			parameters.put("total_fee", order2.getTotalFee());
			parameters.put("transaction_id", "");
			reqHandler.init(appId, key, partnerkey);
		}else if(num==4){
			parameters.put("appid", appIdJP);
			parameters.put("mch_id", mch_idJP);
			parameters.put("nonce_str", nonce_str);
			parameters.put("op_user_id", mch_idJP);
			parameters.put("out_refund_no", order1.getOutTradeNo());
			parameters.put("out_trade_no", order2.getOutTradeNo());
			parameters.put("refund_fee", order1.getTotalFee());
			parameters.put("total_fee", order2.getTotalFee());
			parameters.put("transaction_id", "");
			reqHandler.init(appIdJP, keyJP, partnerkey);
		}else if(num==5){
			parameters.put("appid", appIdAn);
			parameters.put("mch_id", mch_idAn);
			parameters.put("nonce_str", nonce_str);
			parameters.put("op_user_id", mch_idAn);
			parameters.put("out_refund_no", order1.getOutTradeNo());
			parameters.put("out_trade_no", order2.getOutTradeNo());
			parameters.put("refund_fee", order1.getTotalFee());
			parameters.put("total_fee", order2.getTotalFee());
			parameters.put("transaction_id", "");
			reqHandler.init(appIdAn, keyAn, partnerkey);
		}else if(num == 6){
			parameters.put("appid", appIdJPAn);
			parameters.put("mch_id", mch_idJPAn);
			parameters.put("nonce_str", nonce_str);
			parameters.put("op_user_id", mch_idJPAn);
			parameters.put("out_refund_no", order1.getOutTradeNo());
			parameters.put("out_trade_no", order2.getOutTradeNo());
			parameters.put("refund_fee", order1.getTotalFee());
			parameters.put("total_fee", order2.getTotalFee());
			parameters.put("transaction_id", "");
			reqHandler.init(appIdJPAn, keyJPAn, partnerkey);
		}
		// 创建md5摘要
		sign = reqHandler.createSign(parameters);
		parameters.put("sign", sign);
		
		return xml1(parameters);
		
	}
	
	
	public static String getrechargeXML(String notifyUrl,WxOrder order) throws UnknownHostException{
		InetAddress ia=InetAddress.getLocalHost();
		String localip=ia.getHostAddress();
		String attach="微信app支付ios";
		String body="链旧回收";
		String nonce_str=Common.getUuid();
		String sign="";
		String tardeType="APP";
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
		
		sign=MD5Util.wxmd5JM(parameters,key);
		parameters.put("sign", sign);
		order.setSign(sign);
		
		return xml(parameters);
		
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
	
	@SuppressWarnings("unused")
	private void recharge() {
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
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
