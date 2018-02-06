package com.lianjiu.payment.tranconfig;

public class WeChatConfig {
	//请求接口
	public static final String  POST_URL ="https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	public static final String  CHARSET = "UTF-8";//编码格式
	//证书存放目录
	//public static final String  CA_LICENSE = "D:/gzhapiclient_cert.p12";
	public static final String  CA_LICENSE = "/usr/local/lianjiu-server-cluod/order-tomcat/webapps/ROOT/gzhapiclient_cert.p12";
	//注意商户平台需要与微信公众号有关联。通常申请流程是从公众号->微信支付，进入申请
	//商户apikey
	public static final String  API_KEY = "FFB42680CDABAE725B6C3FAA43C259A4";//32位，，登录商户平台（账号格式为：129******@129******，密码通常六位）-》账户设置-》API安全-》设置API密钥
	//商户平台：https://pay.weixin.qq.com/index.php/home/login
	//商户平台号
	public static final String  MCHID = "1489873322";
	
	//微信公众平台appid
	public static final String  MCH_APPID = "wx801dbcf0056597c9";//微信公众号-》开发-》基本配置：格式wx212************";
	//公众号网址：https://mp.weixin.qq.com/
	
}
