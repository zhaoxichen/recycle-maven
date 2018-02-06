package com.lianjiu.payment.service.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *  微信支付工具类
 * @author ligp
 *
 */
public class WeChatUtil {
	/**
	 * 该类放到时间公共类，为了代码完整我拿进来
	 *
	 */
	public static class DateUtil{
	    public static final String yyyyMMddHHmm = "yyyyMMddHHmm";
	    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
		/**
	     * 获取当前时间字符
	     * 
	     * @return 字符串 根据dateFormat 格式返回
	     */
	    public static String getStringDateTime(String dateFormat){
	        Date currentTime = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	        String dateString = formatter.format(currentTime);
	        return dateString;
	    }
	}

		/**
		 * 获取随机字符串
		 * @return 18位日期 + 四位随机数
		 */
		public static String getNonceStr() {
			// 随机数,18位日期yyyyMMddHHmmss
			String currTime = DateUtil.getStringDateTime(DateUtil.yyyyMMddHHmmss);
			// 四位随机数
			String strRandom =  String.valueOf(WeChatUtil.buildRandom(4));
		
			return currTime + strRandom;
		}
		/**
		 * 生成商户订单号
		 * @param nonceStr 12时间 + 10位提现ID，不足前面补0，+ 4位随机字符
		 * @param  applyForId 申请提现ID
		 * @return
		 */
		public static String getPartnerTradeNo(String applyForId){
			StringBuilder sb = new StringBuilder(applyForId);
			String timeStr = DateUtil.getStringDateTime(DateUtil.yyyyMMddHHmm);
			// 四位随机数
			String strRandom =  String.valueOf(WeChatUtil.buildRandom(4));
			for(int i=0; i<10-applyForId.length(); i++){
				sb.insert(0, "0");
			}
			sb.insert(0, timeStr);
			sb.append(strRandom);
			if(sb.length() > 32){
				sb.substring(0, 31);
			}
			return sb.toString();
		}
	
		/**
		 * 取出一个指定长度大小的随机正整数.
		 * 
		 * @param length  int 设定所取出随机数的长度。length小于11
		 * @return int 返回生成的随机数。
		 */
		public static int buildRandom(int length) {
			int num = 1;
			double random = Math.random();
			if (random < 0.1) {
				random = random + 0.1;
			}
			for (int i = 0; i < length; i++) {
				num = num * 10;
			}
			return (int) ((random * num));
		}

		/**
		 * 获取当地电脑ＩＰ
		 * @return
		 */
		public static String getIpAddr(HttpServletRequest request) {
			String ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			}
			return ip;
		}
		
	
		/**
		 * 把BigDecimal金额 取绝对值，  转换成 以分为单位的String格式，并去除小数点
		 * @param amount
		 * @return
		 */
		public static String bigDecimalToPoint(BigDecimal amount){
			BigDecimal oneHundred = new BigDecimal(100);
			amount = amount.abs();
			String amountRes =  amount.multiply(oneHundred).toString();// 这里金额乘以100换成分
			if (amountRes.indexOf(".") >= 0) {
				amountRes.substring(0, amountRes.indexOf("."));
			} 
			return amountRes;
		}
		
	    /**
	     * MAP类型数组转换成List<NameValuePair> 类型
	     * @param properties  MAP类型数组
	     * @return List<NameValuePair> 类型数组
	     */
	    public static List<NameValuePair> generatListNameValuePair(Map<String, String> properties){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	        for (Map.Entry<String, String> entry : properties.entrySet()) {
	        	nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	        }
	        return nvps;
	    }
	    
		
}
